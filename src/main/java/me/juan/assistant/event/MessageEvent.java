package me.juan.assistant.event;

import com.microsoft.bot.builder.TurnContext;
import lombok.Getter;
import me.juan.assistant.persistence.entity.User;
import me.juan.event.Cancellable;
import me.juan.event.Event;
import me.juan.event.HandlerList;

@Getter
public class MessageEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final TurnContext turnContext;
    private final String text;
    private final User user;

    public MessageEvent(User user, String text, TurnContext turnContext) {
        this.turnContext = turnContext;
        this.text = text;
        this.user = user;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}


