package cn.carbs.tide.library.queue;

import java.util.Queue;

import cn.carbs.tide.library.Tide;
import cn.carbs.tide.library.consumer.TaskExecutor;
import cn.carbs.tide.library.consumer.TaskPoolExecutor;
import cn.carbs.tide.library.producer.Task;

public class TaskLooper {

    private TaskLooper() {
        init();
    }

    private static volatile TaskLooper singleton = null;

    public static TaskLooper getInstance() {
        if (singleton == null) {
            synchronized (TaskQueue.class) {
                if (singleton == null) {
                    singleton = new TaskLooper();
                }
            }
        }
        return singleton;
    }

    private void init() {

    }

    public TaskExecutor getAppropriateTaskExecutor(int maxConcurrentTaskCount) {
        return TaskPoolExecutor.newFixedTaskExecutor(maxConcurrentTaskCount);
    }

    public void handleTasksInQueue(Queue<Task> queue) {
        if (queue == null || queue.size() == 0) {
            return;
        }
        // TODO 最多多少个task一同执行，不应该用静态最大值
        TaskExecutor taskExecutor = getAppropriateTaskExecutor(Tide.getInstance().getMaxConcurrentTaskCount());

        // 如果taskExecutor空闲,或者可以继续执行
        int idlePositionCount = taskExecutor.getIdlePositionCount();
        if (idlePositionCount == 0) {
            // taskExecutor被占用了，暂时不处理，等待taskExecutor执行完毕后，通知taskLooper
            return;
        }
        while (idlePositionCount > 0) {
            Task task = queue.poll();
            if (task != null) {
                taskExecutor.submit(task);
            }
            idlePositionCount--;
        }
    }

}
