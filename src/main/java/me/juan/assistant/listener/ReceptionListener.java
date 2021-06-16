package me.juan.assistant.listener;

import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.builder.teams.TeamsInfo;
import com.microsoft.bot.schema.Activity;
import me.juan.assistant.commands.Command;
import me.juan.assistant.event.MessageEvent;
import me.juan.assistant.manager.UserManager;
import me.juan.assistant.persistence.entity.User;
import me.juan.event.EventHandler;
import me.juan.event.EventManager;
import me.juan.event.EventPriority;
import me.juan.event.Listener;

import java.util.concurrent.CompletableFuture;

public class ReceptionListener extends ActivityHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMessageEvent(MessageEvent e) {
        String message = e.getText();
        User user = e.getUser();
        if (Command.getUsersOnCommand().contains(user)) {
            user.getMessageManager().input(message);
            return;
        }
        if (user.getManager().checkCommand(message.toLowerCase())) return;
        user.sendMessage("No encontramos ningún comando asociado a esa accion.", "Prueba diciendo 'menu'");
    }

    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
        Activity activity = turnContext.getActivity();
        if (activity.getConversationReference().getConversation().isGroup()) return super.onMessageActivity(turnContext);
        User user = UserManager.getOrCreateUser(TeamsInfo.getMember(turnContext, activity.getFrom().getId()), turnContext);
        String message = activity.getText() == null ? activity.getValue().toString() : activity.getText();
        EventManager.callEvent(new MessageEvent(user, message, turnContext));
        return super.onMessageActivity(turnContext);
    }
}
