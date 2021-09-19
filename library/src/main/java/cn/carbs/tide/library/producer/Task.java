package cn.carbs.tide.library.producer;

import java.lang.ref.WeakReference;

import cn.carbs.tide.library.cache.TaskCache;
import cn.carbs.tide.library.configuration.TaskConfiguration;
import cn.carbs.tide.library.consumer.TaskExecutor;
import cn.carbs.tide.library.queue.TaskQueue;

// 非 runnable 类
public abstract class Task {

    private TaskConfiguration mConfiguration = new TaskConfiguration();

    public WeakReference<TaskExecutor> weakExecutor;

    public TaskState state = TaskState.Pending;

    private TaskCallback mTaskCallback;

    public Task(TaskCallback taskCallback) {
        mTaskCallback = taskCallback;
    }

    public abstract void run();

    public abstract Object getId();

    public TaskCallback getTaskCallback() {
        return mTaskCallback;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        if (taskExecutor != null) {
            weakExecutor = new WeakReference<TaskExecutor>(taskExecutor);
        }
    }

    public void onTaskDone(Object result, Throwable throwable) {
        if (throwable == null) {
            TaskCache.getInstance().putKeyAndValue(getId(), result);
        }
        // 从executor中移除
        if (weakExecutor != null) {
            TaskExecutor taskExecutor = weakExecutor.get();
            if (taskExecutor != null) {
                taskExecutor.removeTask(this);
            }
        }
        // queue继续执行
        TaskQueue.getInstance().notifyLopper();
    }

    public void setConfiguration(TaskConfiguration taskConfiguration) {
        if (taskConfiguration != null) {
            if (mConfiguration == null) {
                mConfiguration = new TaskConfiguration();
            }
            mConfiguration.maxConcurrentTaskCount = taskConfiguration.maxConcurrentTaskCount;
            mConfiguration.skipTaskWhileScrolling = taskConfiguration.skipTaskWhileScrolling;
            mConfiguration.skipCache = taskConfiguration.skipCache;
        }
    }

}
