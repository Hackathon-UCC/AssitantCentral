package me.juan.assistant.manager;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.*;
import com.microsoft.bot.schema.teams.TeamsChannelAccount;
import lombok.Getter;
import me.juan.assistant.Application;
import me.juan.assistant.persistence.entity.Campus;
import me.juan.assistant.persistence.entity.User;
import me.juan.assistant.persistence.entity.UserCampus;
import me.juan.assistant.persistence.repository.CampusRepository;
import me.juan.assistant.persistence.repository.UserCampusRepository;
import me.juan.assistant.persistence.repository.UserRepository;
import me.juan.assistant.utils.CommonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Getter
public class UserManager {

    @Getter
    private static final HashMap<Integer, UserManager> userManagers = new HashMap<>();

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
            return getOrCreateUser(teamsChannelAccount.get(), turnContext);
        } catch (InterruptedException | ExecutionException e) {
          //  e.printStackTrace();
        }
        return getOrCreateUser(CommonUtil.getTeamsChannelAccount(), turnContext);
    }

    public static User getOrCreateUser(TeamsChannelAccount teamsChannelAccount, TurnContext turnContext) {
        Application application = Application.getInstance();
        UserRepository userRepository = application.getUserRepository();
        CampusRepository campusRepository = application.getCampusRepository();
        UserCampusRepository userCampusRepository = application.getUserCampusRepository();
        String email = teamsChannelAccount.getEmail();
        User user = userRepository.findUserByEmailIgnoreCase(email).orElse(null);
        if (user != null) {
            if (!user.getConversationReference().getConversation().getId().equals(turnContext.getActivity().getConversationReference().getConversation().getId())) { //Actualizacion continua de 'ConversationReference' para enviar mensajes programados.
                user.getManager().getMessageManager().setTurnContext(turnContext);
                user.setConversationReference(turnContext);
                userRepository.save(user);
            }
            return user;
        }
        userRepository.save(new User(teamsChannelAccount, turnContext));
        user = userRepository.findUserByEmailIgnoreCase(email).orElse(null);
        if (user == null) {
            turnContext.sendActivity("Ocurrio un error inesperado!, Lo estamos resolviendo...");
            throw new IllegalStateException("User not found.");
        }
        MessageManager messageManager = user.getManager().getMessageManager().setTurnContext(turnContext).sendMessage("Bienvenido " + user.getAlias()+ ", ¿Como va tu dia?", "Estamos configurando todo. Esto no tardara mucho...");
        Campus campus = campusRepository.findCampusByDomainIgnoreCase(user.getDomain()).orElse(null);
        if (campus != null) userCampusRepository.save(new UserCampus(user.getId(), campus.getId()));
        messageManager.sendMessage(campus != null ? "Registrado con: " + campus.getDisplayName() : "No encontramos una universidad asociado a tu correo electronico.", "Todo listo!", "¿Que quieres hacer hoy?");
        return user;
    }

    public void sendMenu() {


        //HeroCard heroCard = new HeroCard();
        //heroCard.setTitle("TITLE");
      //  heroCard.setImages(new CardImage("https://media.kingston.com/kingston/articles/sdcs-diy5-ep96.jpg"),new CardImage("https://media.kingston.com/kingston/articles/sdcs-diy5-ep96.jpg"),new CardImage("https://media.kingston.com/kingston/articles/sdcs-diy5-ep96.jpg"));
        //heroCard.setButtons(new CardAction(ActionTypes.POST_BACK, "Action", "postBack"));

       // user.sendMessage(MessageFactory.carousel(Collections.singletonList(heroCard.toAttachment()), null));
    }

}
