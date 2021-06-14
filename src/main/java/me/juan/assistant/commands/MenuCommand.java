package me.juan.assistant.commands;

import me.juan.assistant.persistence.entity.Role;
import me.juan.assistant.persistence.entity.User;

import java.util.ArrayList;

public class MenuCommand extends Command{

    public MenuCommand(ArrayList<String> aliases, String command, String description, ArrayList<Role> roles) {
        super(aliases, "menu", "Mirar el menu de action.", null);
    }

    @Override
    public void execute(User user, String command) {

    }
}
