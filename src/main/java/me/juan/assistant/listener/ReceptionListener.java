package me.juan.assistant.listener;

import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.builder.teams.TeamsInfo;
import com.microsoft.bot.schema.Activity;
import me.juan.assistant.event.MessageEvent;
import me.juan.assistant.form.Form;
import me.juan.assistant.form.FormResponse;
import me.juan.assistant.form.field.Action;
import me.juan.assistant.form.field.TextBlock;
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
        if (user.getManager().isWaitingInput()) {
            user.getMessageManager().input(message);
            return;
        }
        if (message.contains("{") || message.contains("}") || message.contains("=")) return;
        if (user.getManager().checkCommand(message.toLowerCase())) return;
        new Thread(() -> {
            FormResponse send = new Form(new TextBlock("**¿Quieres mirar el menu?**")).setActions(new Action("Si").addStyle(), new Action("No").setCancel()).setSpeak("¿Quieres entrar al menu?").send(user, "No te entendí muy bien...");
            if (!send.isCanceled()) user.getManager().checkCommand("menu");
        }).start();
    }

    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
        Activity activity = turnContext.getActivity();
        if (activity.getConversationReference().getConversation().isGroup())
            return super.onMessageActivity(turnContext);
        User user = UserManager.getOrCreateUser(TeamsInfo.getMember(turnContext, activity.getFrom().getId()), turnContext);
        String message = activity.getText() == null ? activity.getValue().toString() : activity.getText();
        if (user == null) return super.onMessageActivity(turnContext); //Cuando el usuario se registra por primera vez.
        if (user.isRegistering()) {
            user.getMessageManager().input(message);
            return super.onMessageActivity(turnContext);
        }
        EventManager.callEvent(new MessageEvent(user, message, activity.getFrom().getId(), turnContext));
        return super.onMessageActivity(turnContext);
    }
}
