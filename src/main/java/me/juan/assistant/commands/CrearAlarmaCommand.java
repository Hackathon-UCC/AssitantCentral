package me.juan.assistant.commands;

import me.juan.assistant.form.FieldType;
import me.juan.assistant.form.Form;
import me.juan.assistant.form.field.Action;
import me.juan.assistant.form.field.DateInput;
import me.juan.assistant.form.field.TextInput;
import me.juan.assistant.form.field.TimeInput;
import me.juan.assistant.persistence.entity.User;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class CrearAlarmaCommand extends Command {

    public CrearAlarmaCommand() {
        super(Arrays.asList("despertarme", "alarma"), "crear alarma", "Crear una alarma", null, "Crear una alarma");
    }

    @Override
    public void execute(User user, String command) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + 1000 * 60 * 5));
        Map<String, String> responseForm_1 = new Form("Crear una alarma", new TextInput().setValue("Alarma").setRequired("El nombre es requerido").setLabel("Nombre de la alarma").setPlaceholder("Nombre").setId("nombre"), new DateInput().setMin(currentDate).setValue(currentDate).setRequired("Fecha invalida.").setLabel("Ingresa la fecha.").setId("date")).setActions(new Action(FieldType.ACTION_SUBMIT, "Programar hora").addStyle(), new Action(FieldType.ACTION_SUBMIT, "Cancelar").setCancel()).send(user);
        if(responseForm_1.getOrDefault("cancel", null) != null) {
            user.sendMessage("Se cancelo la programación de la alarma.");
            return;
        }
        String date = responseForm_1.get("date"), nombre = responseForm_1.get("nombre");
        String currentTime = new SimpleDateFormat("hh:mm").format(new Date(System.currentTimeMillis() + 1000 * 60 * 5));
        boolean minValue = date.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Map<String, String> responseForm_2 = new Form("'"+nombre+"'", new TimeInput().setMin(minValue ? currentTime : null).setValue(currentTime).setRequired("Hora invalida.").setLabel("Ingresa la hora.").setId("time")).setActions(new Action(FieldType.ACTION_SUBMIT, "Programar alarma").addStyle(), new Action(FieldType.ACTION_SUBMIT, "Cancelar").setCancel()).send(user);
        if(responseForm_2.getOrDefault("cancel", null) != null) {
            user.sendMessage("Se cancelo la programación de la alarma '"+nombre+"'.");
            return;
        }
        user.sendMessage("Alarma creada!");
    }
}
