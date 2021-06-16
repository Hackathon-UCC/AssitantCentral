package me.juan.assistant.commands;

import me.juan.assistant.Application;
import me.juan.assistant.data.Cities;
import me.juan.assistant.form.FieldType;
import me.juan.assistant.form.Form;
import me.juan.assistant.form.field.Action;
import me.juan.assistant.form.field.SelectionInput;
import me.juan.assistant.persistence.entity.User;

import java.util.Arrays;
import java.util.Map;

public class CityCommand extends Command {

    public CityCommand() {
        super(Arrays.asList("ciudad", "cambiar campus"), "cambiar ciudad", "¿Quieres cambiar tu lugar de residencia?", null, "Cambiar Ciudad");
    }

    @Override
    public void execute(User user, String command) {
        Map<String, String> send = new Form("¿De que ciudad eres?",
                new SelectionInput().setLabel("Ingresa tu ciudad").setChoices(Cities.getCities()).setId("Ciudad"))
                .setActions(new Action(FieldType.ACTION_SUBMIT, "Listo").setStyle("positive"), new Action(FieldType.ACTION_SUBMIT, "Cancelar").setCancel()).send(user);
        if(send.getOrDefault("cancel", null) != null || send.getOrDefault("Ciudad", null) == null) {
            user.sendMessage("Operación cancelada.");
            return;
        }
        String ciudad = send.get("Ciudad");
        user.sendMessage("Ahora tu ciudad es "+ciudad);
        Application.getInstance().getUserRepository().save(user.setCity(ciudad));
    }

}
