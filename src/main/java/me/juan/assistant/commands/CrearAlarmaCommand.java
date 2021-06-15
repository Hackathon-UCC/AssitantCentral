package me.juan.assistant.commands;

import me.juan.assistant.event.MessageEvent;
import me.juan.assistant.persistence.entity.User;
import me.juan.event.EventHandler;
import me.juan.event.EventManager;
import me.juan.event.EventPriority;
import me.juan.event.Listener;

import java.util.Arrays;
import java.util.Scanner;

public class CrearAlarmaCommand extends Command {

    public CrearAlarmaCommand() {
        super(Arrays.asList("despertarme", "crear alarma") , "alarma", "Crar una alarma", null, null);
    }

    @Override
    public void execute(User user, String command) {
        new Schedule(user);
    }

    public static class Schedule implements Listener {
        private final User user;
        private String fecha, hora;

        public Schedule(User user) {
            EventManager.registerEvent(this);
            this.user = user;
            user.sendMessage("A que hora quieres la alarma", "Utiliza el formato DD/MM/YY");
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onMessageEvent(MessageEvent e) {
            User user = e.getUser();
            if(user.equals(this.user)) {
                e.setCancelled(true);
                String text = e.getText();
                if (fecha != null) {
                    if(!text.contains(":")) {
                        user.sendMessage("Formato de hora invalida. (Ingresa la hora HH:MM)");
                        return;
                    }
                    hora = text;
                    user.sendMessage("Alarma programada para las "+hora+" de el dia "+fecha);
                    EventManager.unRegisterEvent(this);
                    return;
                }
                if(!text.contains("/")) {
                    user.sendMessage("Formato de fecha invalida. (DD/MM/YY)");
                    return;
                }
                fecha = text;
                user.sendMessage("Ingresa la hora HH:MM");
            }
        }
    }

}
