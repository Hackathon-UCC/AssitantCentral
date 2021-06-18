package me.juan.assistant.commands;

import me.juan.assistant.form.FieldType;
import me.juan.assistant.form.Form;
import me.juan.assistant.form.FormResponse;
import me.juan.assistant.form.element.ColumnSet;
import me.juan.assistant.form.field.*;
import me.juan.assistant.persistence.entity.User;
import me.juan.assistant.utils.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class ReminderCommand extends Command {

    public ReminderCommand() {
        super(Collections.singletonList("recordatorio"), "crear recordatorio", "Crear un recordatorio", null, "Crear un recordatorio");
    }

    @Override
    public void execute(User user, String command) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(CommonUtil.getCurrentTimeMilis() * 1000 * 5));
        FormResponse responseForm = new Form("Crear una alarma", new ColumnSet()
                .addColumn(new TextInput().setPlaceholder("Nombre del recordatorio").setId("nombre"),
                new DateInput().setMin(currentDate).setPlaceholder("Fecha del recordatorio").setId("date"))
                .addColumn("100px", new ImageBlock().setUrl("https://i.ibb.co/j3sYHDd/icons8-clock-alert-720px.png").centerImage().setSeparator(true)))
                .setActions(new Action("Programar hora").addStyle(), new Action("Cancelar").setCancel()).send(user);
        String date = responseForm.get("date"), nombre = responseForm.get("nombre");
        if (responseForm.isCanceled() || date == null || nombre == null) {
            user.sendMessage("Se cancelo la programación de la alarma.");
            return;
        }
        String currentTime = new SimpleDateFormat("hh:mm").format(new Date(CommonUtil.getCurrentTimeMilis() * 1000 * 5));
        boolean minValue = date.equals(new SimpleDateFormat("yyyy-MM-dd").format(CommonUtil.getDate()));
        FormResponse response = new Form("'" + nombre + "'", new TimeInput().setMin(minValue ? currentTime : null).setPlaceholder("Hora del recordatorio").setId("time")).setActions(new Action(FieldType.ACTION_SUBMIT, "Programar alarma").addStyle(), new Action(FieldType.ACTION_SUBMIT, "Cancelar").setCancel()).send(user);
        String time = response.get("time");
        if (response.isCanceled() || time == null) {
            user.sendMessage("Se cancelo la programación de la alarma '" + nombre + "'.");
            return;
        }
        user.sendMessage("Alarma creada!");
    }
}
