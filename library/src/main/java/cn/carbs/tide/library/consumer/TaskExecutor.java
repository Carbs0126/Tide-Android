package cn.carbs.tide.library.consumer;

import java.util.Stack;

import cn.carbs.tide.library.cache.TaskCache;
import cn.carbs.tide.library.producer.Task;
import cn.carbs.tide.library.producer.TaskState;
import cn.carbs.tide.library.queue.TaskStack;

public class TaskExecutor {

    private int maxConcurrentTaskCount;

    public Stack<Task> mTasks;

    // 一旦确定就不能更改
    public TaskExecutor(int maxConcurrentTaskCount) {
        this.maxConcurrentTaskCount = maxConcurrentTaskCount;
        init();
    }

    private void init() {
        if (mTasks == null) {
            mTasks = new Stack<>();
        }
    }

    public int getMaxConcurrentTaskCount() {
        return maxConcurrentTaskCount;
    }

    public int getIdlePositionCount() {
        synchronized (this) {
            if (mTasks == null) {
                return maxConcurrentTaskCount;
            }
            int busyPosition = 0;
            for (Task taskItem : mTasks) {
                if (taskItem.state == TaskState.Done) {
                    continue;
                }
                busyPosition++;
            }
            return maxConcurrentTaskCount - busyPosition;
        }
    }

    // TODO 返回的应该是一个task
    public void submit(Task task) {
        addTask(task);
        task.setTaskExecutor(this);
        executeTasks();
    }

    private void executeTasks() {
        synchronized (this) {
            for (Task taskItem : mTasks) {
                if (taskItem.state != TaskState.Pending) {
                    continue;
                }
                if (taskItem != null) {
                    if (!taskItem.getIfSkipCache()) {
                        // 如果不跳过缓存
                        Object result = TaskCache.getInstance().getValueByKey(taskItem.getId());
                        if (result != null) {
                            taskItem.runTaskCallback(result, null);
                            taskItem.state = TaskState.Done;
                            // TODO
                            TaskStack.getInstance().notifyLopper();
                            continue;
                        }
                    }
                    taskItem.state = TaskState.Executing;
                    taskItem.run();
                }
            }
        }
    }

    private void removeCompletedTasks() {
        synchronized (this) {

        }
    }

    public void addTask(Task task) {
        synchronized (this) {
            if (mTasks == null) {
                mTasks = new Stack<>();
            }
            mTasks.push(task);
        }
    }

    // 一个task执行完毕之后，将会被从Executor中移除
    public void removeTask(Task task) {
        synchronized (this) {
            if (mTasks == null) {
                return;
            }
            mTasks.remove(task);
        }
    }
}