package me.juan.assistant.manager;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.connector.authentication.MicrosoftAppCredentials;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ConversationReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.Application;
import me.juan.assistant.persistence.entity.User;

import java.util.ArrayList;
import java.util.Arrays;

@Accessors(chain = true)
public class MessageManager {

    @Getter
    private static final ArrayList<MessageManager> messageManagers = new ArrayList<>();
    @Getter
    private final ArrayList<String> inputs;
    private final ConversationReference conversationReference;
    private final ArrayList<Activity> outPuts;
    private final User user;
    @Setter
    private TurnContext turnContext;

    public MessageManager(ConversationReference conversationReference, User user) {
        this.conversationReference = conversationReference;
        this.user = user;
        this.outPuts = new ArrayList<>();
        this.inputs = new ArrayList<>();
        messageManagers.add(this);
    }

    public void input(String input) {
        synchronized (inputs) {
            inputs.add(input);
            inputs.notifyAll();
        }
    }

    public String input() {
        user.getManager().setWaitingInput(true);
        synchronized (inputs) {
            while (inputs.isEmpty()) {
                try {
                    inputs.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return input();
                }
            }
            String message = inputs.get(0);
            inputs.remove(message);
            user.getManager().setWaitingInput(false);
            return message;
        }
    }

    public void sendMessage(String msg) {
        sendMessage(MessageFactory.text(msg));
    }

    public void sendMessage(String... a) {
        for (String s : a) { outPuts.add(MessageFactory.text(s)); }
    }

    public void sendMessage(ArrayList<Activity> activities) {
        outPuts.addAll(activities);
    }

    public void sendMessage(Activity msg) {
        outPuts.add(msg);
    }

    public void sendMessage(Activity... a) {
        outPuts.addAll(Arrays.asList(a));
    }

    public void send() {
        try {
            sendMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessages() {
        if (outPuts.isEmpty()) return;
        ArrayList<Activity> activities = new ArrayList<>(outPuts);
        outPuts.clear();
        if (turnContext != null) {
            turnContext.sendActivities(activities);
            return;
        }
        Application.getBotFrameworkHttpAdapter().continueConversation(Application.getConfigurationFile().getProperty(MicrosoftAppCredentials.MICROSOFTAPPID), conversationReference, turnContext -> turnContext.sendActivities(activities).thenApply(resourceResponse -> null));
    }
}
