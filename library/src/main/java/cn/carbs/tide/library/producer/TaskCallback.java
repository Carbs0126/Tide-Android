package cn.carbs.tide.library.producer;

public interface TaskCallback {

    void onTaskCompleted(Task task, Object result);

    void onTaskError(Task task, Throwable throwable);

}
