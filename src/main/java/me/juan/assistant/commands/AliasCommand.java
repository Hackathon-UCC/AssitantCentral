package me.juan.assistant.commands;

import me.juan.assistant.Application;
import me.juan.assistant.form.Form;
import me.juan.assistant.form.FormResponse;
import me.juan.assistant.form.element.Column;
import me.juan.assistant.form.element.ColumnSet;
import me.juan.assistant.form.field.Action;
import me.juan.assistant.form.field.ImageBlock;
import me.juan.assistant.form.field.TextInput;
import me.juan.assistant.persistence.entity.User;

import java.util.Collections;
import java.util.regex.Pattern;

public class AliasCommand extends Command {

    public AliasCommand() {
        super(Collections.singletonList("alias"), "cambiar alias", null, null, "Cambiar alias");
    }

    public static boolean aliasCheck(String aliasConverted) {
        return Pattern.matches("^[ a-zA-Z0-9_.-]{4,16}$", aliasConverted);
    }

    @Override
    public void execute(User user, String command) {
        FormResponse send = new Form("Cambiar apodo", new ColumnSet().setColumns(new Column().setItems(
                new TextInput().setMaxLength(16).setPlaceholder("Nuevo apodo").setId("Apodo")),
                new Column().setWidth("100px").setItems(new ImageBlock().setUrl("https://i.ibb.co/KLFfDfw/icons8-graduate-720px.png").centerImage())))
                .setActions(new Action("Listo").addStyle(), new Action("Cancelar").setCancel()).send(user);
        String apodo = send.get("Apodo");
        if (send.isCanceled() || apodo == null) {
            user.sendMessage("Operación cancelada.");
            return;
        }
        apodo = apodo.trim();
        if (!aliasCheck(apodo)) {
            if (!new Form("¿Quieres repetir el proceso?").setActions(new Action("Si").addStyle(), new Action("No").setCancel()).send(user, "El apodo es invalido :(").isCanceled()) {
                setDisableMessage(true);
                user.getManager().checkCommand(getCommand());
            }
            return;
        }
        user.setAlias(apodo);
        Application.getInstance().getUserRepository().save(user);
        user.sendMessage("Perfecto, " + apodo + "!", "Tu apodo fue cambiado!");
    }

}
