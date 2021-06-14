package me.juan.assistant.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.juan.assistant.persistence.entity.Role;
import me.juan.assistant.persistence.entity.User;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
public abstract class Command {

    private final ArrayList<String> aliases;
    private final String command, description;
    private final ArrayList<Role> roles;
    //private final HashMap<>

    public void onCall(User user, String command) {
        new Thread(() -> execute(user, command)).start();
    }

    public abstract void execute(User user, String command);

    public boolean isAvailable(User user) {
        return roles == null || roles.contains(user.getRole());
    }


}
