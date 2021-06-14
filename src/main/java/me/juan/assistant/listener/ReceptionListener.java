package me.juan.assistant.listener;

import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.builder.teams.TeamsInfo;
import com.microsoft.bot.schema.ChannelAccount;
import me.juan.assistant.event.MessageEvent;
import me.juan.assistant.manager.UserManager;
import me.juan.assistant.persistence.entity.User;
import me.juan.event.EventHandler;
import me.juan.event.EventManager;
import me.juan.event.EventPriority;
import me.juan.event.Listener;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReceptionListener extends ActivityHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMessageEvent(MessageEvent e) {
        String message = e.getText().toLowerCase();
        User user = e.getUser();
        if (user.getManager().checkCommand(message)) return;
        user.sendMessage("No encontramos ningún comando asociado a esa accion.", "Prueba diciendo 'menu'");
    }

    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
        if(turnContext.getActivity().getConversationReference().getConversation().isGroup()) return super.onMessageActivity(turnContext);
        User user = UserManager.getOrCreateUser(TeamsInfo.getMember(turnContext, turnContext.getActivity().getFrom().getId()), turnContext);
        EventManager.callEvent(new MessageEvent(user, turnContext.getActivity().getText(), turnContext));
        return super.onMessageActivity(turnContext);
    }
}
