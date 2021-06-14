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
import java.util.Scanner;


public class MessageManager {

    private final ConversationReference conversationReference;
    private final ArrayList<Activity> outPuts;
    @Getter
    private final ArrayList<String> inputs;
    private TurnContext turnContext;

    public MessageManager(ConversationReference conversationReference) {
        this.conversationReference = conversationReference;
        this.outPuts = new ArrayList<>();
        this.inputs = new ArrayList<>();
    }

    public String input() {
        while (outPuts.isEmpty()) {

        }
        return inputs.get(0);

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
        if (outPuts.isEmpty()) return;
        Activity message = outPuts.get(0);
        outPuts.remove(message);
        if (turnContext != null) {
            turnContext.sendActivity(message);
            return;
        }
        Application.getBotFrameworkHttpAdapter().continueConversation(Application.getConfigurationFile().getProperty(
                MicrosoftAppCredentials.MICROSOFTAPPID),
                conversationReference,
                turnContext -> turnContext.sendActivity(message).thenApply(resourceResponse -> null));
    }

    public MessageManager setTurnContext(TurnContext turnContext) {
        this.turnContext = turnContext;
        return this;
    }
}
