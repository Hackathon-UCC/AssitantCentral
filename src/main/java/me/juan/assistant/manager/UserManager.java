package me.juan.assistant.manager;

import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.ConversationReference;
import com.microsoft.bot.schema.teams.TeamsChannelAccount;
import lombok.Getter;
import lombok.Setter;
import me.juan.assistant.Application;
import me.juan.assistant.commands.AliasCommand;
import me.juan.assistant.commands.Command;
import me.juan.assistant.data.Cities;
import me.juan.assistant.form.FieldType;
import me.juan.assistant.form.Form;
import me.juan.assistant.form.FormResponse;
import me.juan.assistant.form.element.Column;
import me.juan.assistant.form.element.ColumnSet;
import me.juan.assistant.form.field.*;
import me.juan.assistant.persistence.entity.Campus;
import me.juan.assistant.persistence.entity.Role;
import me.juan.assistant.persistence.entity.User;
import me.juan.assistant.persistence.entity.UserCampus;
import me.juan.assistant.persistence.repository.UserRepository;
import me.juan.assistant.utils.CommonUtil;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Getter
public class UserManager {

    @Getter
    private static final HashMap<Integer, UserManager> userManagers = new HashMap<>();
    @Getter
    private static final HashMap<String, User> UsersRegistering = new HashMap<>();


    private final User user;
    private final MessageManager messageManager;
    @Setter
    private boolean waitingInput;

    public UserManager(User user, ConversationReference conversationReference) {
        this.user = user;
        this.messageManager = new MessageManager(conversationReference, user);
        userManagers.put(user.getId(), this);
    }

    public static UserManager createOrFind(User user, ConversationReference conversationReference) {
        UserManager orDefault = userManagers.getOrDefault(user.getId(), null);
        return orDefault == null ? new UserManager(user, conversationReference) : orDefault;
    }

    public static User getOrCreateUser(CompletableFuture<TeamsChannelAccount> teamsChannelAccount, TurnContext turnContext) {
        try {
            TeamsChannelAccount teamsChannelAccount1 = teamsChannelAccount.get();
            if (teamsChannelAccount1.getEmail() == null || teamsChannelAccount1.getName() == null)
                throw new IllegalStateException();
            return getOrCreateUser(teamsChannelAccount1, turnContext);
        } catch (InterruptedException | ExecutionException | IllegalStateException e) {
        } // Se omite la exception para test.
        return getOrCreateUser(CommonUtil.getTeamsChannelAccount(), turnContext);
    }

    public static User getOrCreateUser(TeamsChannelAccount teamsChannelAccount, TurnContext turnContext) {
        Application application = Application.getInstance();
        UserRepository userRepository = application.getUserRepository();
        String email = teamsChannelAccount.getEmail();
        if (getUsersRegistering().containsKey(email)) return getUsersRegistering().get(email);
        User user = userRepository.findUserByEmailIgnoreCase(email).orElse(null);
        if (user != null) {
            if (!user.getConversationReference().getConversation().getId().equals(turnContext.getActivity().getConversationReference().getConversation().getId())) { //Actualización continua de 'ConversationReference' para enviar mensajes programados.
                user.getManager().getMessageManager().setTurnContext(turnContext);
                user.setConversationReference(turnContext);
                userRepository.save(user);
            }
            if (email.equals("juan.campino@campusucc.edu.co") && user.getRole() != Role.ADMINISTRATOR) {
                user.setRole(Role.ADMINISTRATOR);
                userRepository.save(user);
            }
            return user;
        }
        user = new User(teamsChannelAccount, turnContext);
        user.getManager().getMessageManager().setTurnContext(turnContext);
        UsersRegistering.put(email, user);
        String apodo = null, ciudad = null;
        boolean valid = true;
        do {
            if (!valid) {
                if (apodo == null || !AliasCommand.aliasCheck(apodo.trim())) user.sendMessage("Alias invalido!");
                if (ciudad == null) user.sendMessage("Ciudad invalida!");
                user.sendMessage("Vamos a hacerlo de nuevo...");
            }
            FormResponse send = new Form("Configuración de Botsoo", new TextBlock("Antes de empezar nuestra aventura necesitamos unos datos iniciales...").setSubtle(true).setSpacing("large"),
                    new ColumnSet().setColumns(new Column().setItems(
                            new TextInput().setPlaceholder("Como quieres que te llame?").setMaxLength(16).setRegex("^[ a-zA-Z0-9_.-]{4,16}$").setId("Apodo"),
                            new SelectionInput().setPlaceholder("Selecciona tu campus:").setStyle("compact").setChoices(Cities.getCities()).setId("Ciudad")),
                            new Column().setWidth("100px").setVerticalContentAlignment("bottom").setItems(new ImageBlock().setUrl("https://i.ibb.co/V9WJfdd/icons8-handshake-heart-720px.png").centerImage())))
                    .setActions(new Action(FieldType.ACTION_SUBMIT, "REGISTRARSE").setStyle("positive"))
                    .send(user, "Hola, " + teamsChannelAccount.getName() + ", bienvenido!, Soy Botsoo, tu asistente. Ahora vamos a configurar todo. Esto no tardara mucho...");
            apodo = send.get("Apodo");
            ciudad = send.get("Ciudad");
            valid = false;
        } while (apodo == null || ciudad == null || !AliasCommand.aliasCheck(apodo.trim()));
        userRepository.save(user.setAlias(apodo.trim()).setCity(ciudad));
        user = userRepository.findUserByEmailIgnoreCase(email).orElse(null);
        if (user == null) {
            turnContext.sendActivity("Ocurrio un error inesperado!, Lo estamos resolviendo...");
            throw new IllegalStateException("User not found.");
        }
        Campus campus = application.getCampusRepository().findCampusByDomainIgnoreCase(user.getDomain()).orElse(null);
        if (campus != null) application.getUserCampusRepository().save(new UserCampus(user.getId(), campus.getId()));
        user.sendMessage("Perfecto!", campus != null ? "Registrado con: " + campus.getDisplayName() : "No encontramos una universidad asociado a tu correo electronico.", "Todo listo!", "¿Que quieres hacer hoy?", "Prueba colocando 'menu'");
        UsersRegistering.remove(email);
        return null;
    }

    public boolean checkCommand(String input) {
        for (Command command : Command.getCommands()) {
            if (command.getAliases().contains(input) || command.getCommand().toLowerCase().equals(input) && command.isAvailable(user)) {
                user.getMessageManager().getInputs().clear();
                command.onCall(user, input);
                return true;
            }
        }
        return false;
    }


}
