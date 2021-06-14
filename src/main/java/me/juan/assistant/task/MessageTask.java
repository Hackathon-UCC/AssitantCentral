package me.juan.assistant.task;

import me.juan.assistant.manager.UserManager;

public class MessageTask extends Task {

    public MessageTask(long milliseconds) {
        super(TaskType.EVERY, milliseconds);
    }

    @Override
    public void onExecute() {
        UserManager.getUserManagers().values().forEach((userManager) -> {
            try {
                userManager.getMessageManager().send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
