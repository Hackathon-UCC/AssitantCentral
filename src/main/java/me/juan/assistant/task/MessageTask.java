package me.juan.assistant.task;

import me.juan.assistant.manager.MessageManager;

public class MessageTask extends Task {

    public MessageTask(long milliseconds) {
        super(TaskType.EVERY, milliseconds);
    }

    @Override
    public void onExecute() {
        for (MessageManager messageManager : MessageManager.getMessageManagers()) {
            try {
                messageManager.send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
