package me.juan.assistant.manager;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.connector.authentication.MicrosoftAppCredentials;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ConversationReference;
import lombok.Getter;
import me.juan.assistant.Application;

import java.util.ArrayList;
import java.util.Arrays;


public class MessageManager {

    @Getter
    private static final ArrayList<MessageManager> messageManagers = new ArrayList<>();
    @Getter
    private final ArrayList<String> inputs;
    private final ConversationReference conversationReference;
    private final ArrayList<Activity> outPuts;
    private TurnContext turnContext;

    public MessageManager(ConversationReference conversationReference) {
        this.conversationReference = conversationReference;
        this.outPuts = new ArrayList<>();
        this.inputs = new ArrayList<>();
        messageManagers.add(this);
    }

    public MessageManager sendMessage(String msg) {
        return sendMessage(MessageFactory.text(msg));
    }

    public MessageManager sendMessage(String... a) {
        for (String s : a) { outPuts.add(MessageFactory.text(s)); }
        return this;
    }

    public MessageManager sendMessage(Activity msg) {
        outPuts.add(msg);
        return this;
    }

    public MessageManager sendMessage(Activity... a) {
        outPuts.addAll(Arrays.asList(a));
        return this;
    }

    public void send() {
        try{ sendMessages(); }catch (Exception ignored){}
    }

    private void sendMessages() {
        if (outPuts.isEmpty()) return;
        ArrayList<Activity> activities = new ArrayList<>(outPuts);
        outPuts.removeAll(activities);
        if (turnContext != null) {
            turnContext.sendActivities(activities);
            return;
        }
        Application.getBotFrameworkHttpAdapter().continueConversation(Application.getConfigurationFile().getProperty(
                MicrosoftAppCredentials.MICROSOFTAPPID),
                conversationReference,
                turnContext -> turnContext.sendActivities(activities).thenApply(resourceResponse -> null));
    }

    public MessageManager setTurnContext(TurnContext turnContext) {
        this.turnContext = turnContext;
        return this;
    }
}
