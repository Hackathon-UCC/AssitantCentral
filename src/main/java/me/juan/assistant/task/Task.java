package me.juan.assistant.task;


import me.juan.assistant.Application;

public abstract class Task {

    private final TaskType taskType;
    private final long milliseconds;
    private final Thread thread;

    public Task(TaskType taskType, long milliseconds) {
        this.taskType = taskType;
        this.milliseconds = milliseconds;
        this.thread = new Thread(this::execute);
        thread.start();
    }

    private void execute() {
        do {
            try {
                Thread.sleep(milliseconds);
                onExecute();
            } catch (Exception e) {
                e.printStackTrace();
                cancel();
            }
        } while (taskType == TaskType.EVERY && !thread.isInterrupted());
    }

    public void cancel() {
        thread.interrupt();
    }

    public abstract void onExecute();

}
