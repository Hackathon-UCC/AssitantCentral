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

public class AliasCommand extends Command {

    public AliasCommand() {
        super(Collections.singletonList("alias"), "cambiar alias", null, null, "Cambiar alias");
    }

    @Override
    public void execute(User user, String command) {
        FormResponse send = new Form("Cambiar apodo", new ColumnSet().setColumns(new Column().setItems(
                new TextInput().setMaxLength(16).setErrorMessage("Apodo invalido.").setPlaceholder("Nuevo apodo").setRegex("^[ a-zA-Z0-9_.-]{4,16}$").setId("Apodo")),
                new Column().setWidth("100px").setItems(new ImageBlock().setUrl("https://i.ibb.co/KLFfDfw/icons8-graduate-720px.png").centerImage())))
                .setActions(new Action("Listo").addStyle(), new Action("Cancelar").setCancel()).send(user);
        if(send.isCanceled() || send.get("Apodo") == null) {
            user.sendMessage("Operación cancelada.");
            return;
        }
        String apodo = send.get("Apodo");
        user.setAlias(apodo);
        Application.getInstance().getUserRepository().save(user);
        user.sendMessage("Esta bien "+apodo+", tu apodo fue cambiado!");
    }
}
