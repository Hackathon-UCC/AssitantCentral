package me.juan.assistant.task;


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
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cancel();
            return;
        }
        onExecute();
        if (taskType == TaskType.EVERY) execute();
    }

    public void cancel() {
        thread.interrupt();
    }

    public abstract void onExecute();

}
