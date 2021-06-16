package me.juan.assistant.commands;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.CardAction;
import lombok.Getter;
import me.juan.assistant.manager.UserManager;
import me.juan.assistant.persistence.entity.Role;
import me.juan.assistant.persistence.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
public abstract class Command {

    @Getter
    private static final ArrayList<Command> commands = new ArrayList<>();
    @Getter
    private static final ArrayList<User> UsersOnCommand = new ArrayList<>();
    private final List<String> aliases;
    private final String command, description;
    private final List<Role> roles;
    private final CardAction cardAction;

    public Command(List<String> aliases, String command, String description, List<Role> roles, String displayMenu) {
        this(aliases, command, description, roles, new CardAction(ActionTypes.IM_BACK, displayMenu, command));
    }

    public Command(List<String> aliases, String command, String description, List<Role> roles, CardAction cardAction) {
        this.aliases = aliases;
        this.command = command;
        this.description = description;
        this.roles = roles;
        this.cardAction = cardAction;
        commands.add(this);
    }

    public void onCall(User user, String command) {
        UsersOnCommand.add(user);
        new Thread(() -> {
            execute(user, command);
            if(Command.this.cardAction != null) {
                user.sendMessage(MessageFactory.suggestedCardActions(Arrays.asList(new CardAction(ActionTypes.IM_BACK, "Sí", "Si"), new CardAction(ActionTypes.IM_BACK, "No", "No")), "¿Te puedo ayudar en algo mas?"));
                String input = user.input();
                user.sendMessage(input.equalsIgnoreCase("no") ? "Dale!, que tengas un dia espectacular!" : (input.equalsIgnoreCase("si") ? "Es un placer!" : "No te entendí muy bien"));
                UsersOnCommand.remove(user);
                if(input.equalsIgnoreCase("si")) new Thread(() -> user.getManager().checkCommand("menu")).start();
                return;
            }
            UsersOnCommand.remove(user);
        }).start();
    }

    public abstract void execute(User user, String command);

    public boolean isAvailable(User user) {
        return roles == null || roles.contains(user.getRole());
    }


}
