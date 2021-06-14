package me.juan.assistant.commands;

import me.juan.assistant.persistence.entity.User;

import java.util.Arrays;

public class MenuCommand extends Command{

    public MenuCommand() {
        super(Arrays.asList("opciones", "mostrar menu", "que puedo hacer"), "menu", "Mirar el menu de action.", null, null);
    }

    @Override
    public void execute(User user, String command) {
        user.sendMessage("Este es el menu");
    }
}
