package cn.carbs.tide.library.consumer;

import java.util.ArrayList;

public class TaskPoolExecutor {

    private static ArrayList<TaskExecutor> sTaskExecutors = new ArrayList<>(4);

    public static TaskExecutor newFixedTaskExecutor(int maxConcurrentTaskCount) {
        for (TaskExecutor taskExecutor : sTaskExecutors) {
            if (taskExecutor != null) {
                if (taskExecutor.getMaxConcurrentTaskCount() == maxConcurrentTaskCount) {
                    return taskExecutor;
                }
            }
        }
        TaskExecutor taskExecutor = new TaskExecutor(maxConcurrentTaskCount);
        sTaskExecutors.add(taskExecutor);
        return taskExecutor;
    }

}
