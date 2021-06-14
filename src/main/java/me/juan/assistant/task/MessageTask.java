package me.juan.assistant.task;

import me.juan.assistant.manager.UserManager;

import java.util.Collection;

public class MessageTask extends Task {

    public MessageTask(long milliseconds) {
        super(TaskType.EVERY, milliseconds);
    }

    @Override
    public void onExecute() {
        Collection<UserManager> values = UserManager.getUserManagers().values();
        for (UserManager value : values) {
            try {
                value.getMessageManager().send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
