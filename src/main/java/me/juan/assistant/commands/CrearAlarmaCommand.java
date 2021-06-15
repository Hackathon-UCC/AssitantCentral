package me.juan.assistant.commands;

import me.juan.assistant.persistence.entity.User;

import java.util.Arrays;

public class CrearAlarmaCommand extends Command {

    public CrearAlarmaCommand() {
        super(Arrays.asList("despertarme", "crear alarma") , "alarma", "Crar una alarma", null, null);
    }

    @Override
    public void execute(User user, String command) {
        user.sendMessage("¿Cuando quieres la alarma?", "Utiliza el formato (DD/MM/YY)");
        String fecha = null;
        while (fecha == null || !fecha.contains("/")) {
            if(fecha != null) user.sendMessage("Formato de fecha invalido. (DD/MM/YY)");
            fecha = user.input();
        }
        user.sendMessage("¿A que hora quieres la alarma?", "Ingresa la hora (HH:MM)");
        String hora = null;
        while (hora == null || !hora.contains(":")) {
            if(hora != null) user.sendMessage("Formato de hora invalido. (HH:MM)");
            hora = user.input();
        }
        user.sendMessage("Alarma programada para las "+hora+" de el dia "+fecha);
    }
}
