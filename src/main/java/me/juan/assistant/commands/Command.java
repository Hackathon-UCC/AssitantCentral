package me.juan.assistant.commands;

import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.CardAction;
import lombok.Getter;
import lombok.Setter;
import me.juan.assistant.form.Form;
import me.juan.assistant.form.FormResponse;
import me.juan.assistant.form.field.Action;
import me.juan.assistant.form.field.TextBlock;
import me.juan.assistant.persistence.entity.Role;
import me.juan.assistant.persistence.entity.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Command {

    @Getter
    private static final ArrayList<Command> commands = new ArrayList<>();
    private final List<String> aliases;
    private final String command, description;
    private final List<Role> roles;
    private final CardAction cardAction;
    @Setter
    private boolean disableMessage;

    public Command(List<String> aliases, String command, String description, List<Role> roles, String displayMenu) {
        this(aliases, command, description, roles, new CardAction(ActionTypes.IM_BACK, displayMenu, displayMenu));
    }

    public Command(List<String> aliases, String command, String description, List<Role> roles, CardAction cardAction) {
        this.aliases = aliases;
        this.command = command;
        this.description = description;
        this.roles = roles;
        this.cardAction = cardAction;
        commands.add(this);
    }

    public void onCall(User user, String command) {
        new Thread(() -> {
            execute(user, command);
            if (Command.this.cardAction != null && !disableMessage) {
                FormResponse send = new Form(new TextBlock("¿Te puedo ayudar en algo mas?")).setActions(new Action("Si").addStyle(), new Action("No").setCancel()).send(user);
                if (send.isCanceled()) {
                    user.sendMessage("Dale!, que tengas un dia espectacular!");
                    return;
                }
                user.getManager().checkCommand("menu");
            }
        }).start();
    }

    public abstract void execute(User user, String command);

    public boolean isAvailable(User user) {
        return roles == null || roles.contains(user.getRole());
    }


}
