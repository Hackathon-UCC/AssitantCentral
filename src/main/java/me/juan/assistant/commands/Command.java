package me.juan.assistant.commands;

import com.microsoft.bot.schema.CardAction;
import lombok.Getter;
import me.juan.assistant.persistence.entity.Role;
import me.juan.assistant.persistence.entity.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Command {

    @Getter
    private static final ArrayList<Command> commands = new ArrayList<>();
    @Getter
    private static final ArrayList<User> UsersOnCommand = new ArrayList<>();
    private final List<String> aliases;
    private final String command, description;
    private final ArrayList<Role> roles;
    private final CardAction cardAction;

    public Command(List<String> aliases, String command, String description, ArrayList<Role> roles, CardAction cardAction) {
        this.aliases = aliases;
        this.command = command;
        this.description = description;
        this.roles = roles;
        this.cardAction = cardAction;
        commands.add(this);
    }

    public void onCall(User user, String command) {
        UsersOnCommand.add(user);
        new Thread(() -> { execute(user, command);UsersOnCommand.remove(user); }).start();
    }

    public abstract void execute(User user, String command);

    public boolean isAvailable(User user) {
        return roles == null || roles.contains(user.getRole());
    }


}
