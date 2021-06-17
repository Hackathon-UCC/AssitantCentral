package me.juan.assistant.manager;

import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.ConversationReference;
import com.microsoft.bot.schema.teams.TeamsChannelAccount;
import lombok.Getter;
import me.juan.assistant.Application;
import me.juan.assistant.commands.Command;
import me.juan.assistant.data.Cities;
import me.juan.assistant.form.FieldType;
import me.juan.assistant.form.Form;
import me.juan.assistant.form.field.Action;
import me.juan.assistant.form.field.SelectionInput;
import me.juan.assistant.form.field.TextBlock;
import me.juan.assistant.form.field.TextInput;
import me.juan.assistant.persistence.entity.Campus;
import me.juan.assistant.persistence.entity.User;
import me.juan.assistant.persistence.entity.UserCampus;
import me.juan.assistant.persistence.repository.CampusRepository;
import me.juan.assistant.persistence.repository.UserCampusRepository;
import me.juan.assistant.persistence.repository.UserRepository;
import me.juan.assistant.utils.CommonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Getter
public class UserManager {

    @Getter
    private static final HashMap<Integer, UserManager> userManagers = new HashMap<>();
    @Getter
    private static final HashMap<String, User>  UsersRegistering = new HashMap<>();


    private final User user;
    private final MessageManager messageManager;

    public UserManager(User user, ConversationReference conversationReference) {
        this.user = user;
        this.messageManager = new MessageManager(conversationReference);
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
        if(getUsersRegistering().containsKey(email)) return getUsersRegistering().get(email);
        User user = userRepository.findUserByEmailIgnoreCase(email).orElse(null);
        if (user != null) {
            if (!user.getConversationReference().getConversation().getId().equals(turnContext.getActivity().getConversationReference().getConversation().getId())) { //Actualización continua de 'ConversationReference' para enviar mensajes programados.
                user.getManager().getMessageManager().setTurnContext(turnContext);
                user.setConversationReference(turnContext);
                userRepository.save(user);
            }
            return user;
        }
        user = new User(teamsChannelAccount, turnContext);
        UsersRegistering.put(email, user);
        MessageManager messageManager = user.getManager().getMessageManager().setTurnContext(turnContext);
        messageManager.sendMessage("Hola, "+teamsChannelAccount.getName()+", bienvenido!, Soy Bootsoo, tu asistente. Ahora vamos a configurar todo. Esto no tardara mucho...", "");
        Map<String, String> send = new Form("Configuracion de Botsoo", new TextBlock("Antes de empezar nuestra aventura necesitamos unos datos iniciales...")
                .setSubtle(true).setSpacing("large"),
                new TextInput().setLabel("¿Como quieres que te llame?").setPlaceholder("Como quieres que te llame?").setMaxLength(16).setRegex("^[ a-zA-Z0-9_.-]{6,16}$").setRequired("Apodo invalido.").setId("Apodo"),
                new TextInput().setLabel("¿Cual es tu numero celular?").setPlaceholder("¿Cual es tu numero celular?").setMaxLength(10).setRegex("^[0-9]{10,10}$").setStyle("tel").setRequired("Celular Invalido.").setId("Celular"),
                new SelectionInput().setLabel("Selecciona tu campus:").setPlaceholder("Selecciona tu campus:").setRequired("Campus invalido.").setChoices(Cities.getCities()).setId("Ciudad")).setActions(
                new Action(FieldType.ACTION_SUBMIT, "REGISTRARSE").setStyle("positive")).send(user);
        String apodo = send.get("Apodo"), celular = send.get("Celular"), ciudad = send.get("Ciudad");
        user.sendMessage("Perfecto, ahora vamos a validar tus datos...");
        userRepository.save(user.setAlias(apodo).setPhone(celular).setCity(ciudad));
        CampusRepository campusRepository = application.getCampusRepository();
        UserCampusRepository userCampusRepository = application.getUserCampusRepository();
        user = userRepository.findUserByEmailIgnoreCase(email).orElse(null);
        if (user == null) {
            turnContext.sendActivity("Ocurrio un error inesperado!, Lo estamos resolviendo...");
            throw new IllegalStateException("User not found.");
        }
        Campus campus = campusRepository.findCampusByDomainIgnoreCase(user.getDomain()).orElse(null);
        if (campus != null) userCampusRepository.save(new UserCampus(user.getId(), campus.getId()));
        messageManager.sendMessage(campus != null ? "Registrado con: " + campus.getDisplayName() : "No encontramos una universidad asociado a tu correo electronico.", "Todo listo!", "¿Que quieres hacer hoy?", "Prueba colocando 'menu'");
        UsersRegistering.remove(email);
        return null;
    }

    public boolean checkCommand(String input) {
        for (Command command : Command.getCommands()) {
            if (command.getAliases().contains(input) || command.getCommand().equals(input) && command.isAvailable(user)) {
                user.getMessageManager().getInputs().clear();
                command.onCall(user, input);
                return true;
            }
        }
        return false;
    }


}
