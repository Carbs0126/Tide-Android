package cn.carbs.tide.library.producer;

import java.lang.ref.WeakReference;

import cn.carbs.tide.library.cache.TaskCache;
import cn.carbs.tide.library.configuration.TaskConfiguration;
import cn.carbs.tide.library.configuration.TideConfiguration;
import cn.carbs.tide.library.consumer.TaskExecutor;
import cn.carbs.tide.library.queue.TaskStack;

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

    public void onTaskDone(Object id, Object result, Throwable throwable) {
        addToCache(result, throwable);
        removeTaskFromExecutor();
        notifyLooperToWorkAgain();
    }

    public void runTaskCallback(Object result, Throwable throwable) {
        TaskCallback taskCallback = getTaskCallback();
        if (taskCallback != null) {
            if (throwable != null) {
                taskCallback.onTaskError(this, throwable);
            } else {
                taskCallback.onTaskCompleted(this, result);
            }
        }
    }

    public void setConfiguration(TideConfiguration tideConfiguration) {
        if (tideConfiguration != null) {
            if (mConfiguration == null) {
                mConfiguration = new TaskConfiguration();
            }
            mConfiguration.pauseTaskWhileScrolling = tideConfiguration.pauseTaskWhileScrolling;
            mConfiguration.skipCache = tideConfiguration.skipCache;
        }
    }

    public boolean getIfSkipCache() {
        if (mConfiguration == null) {
            return false;
        }
        return mConfiguration.skipCache;
    }

    public boolean getIfPauseTaskWhileScrolling() {
        if (mConfiguration == null) {
            return false;
        }
        return mConfiguration.pauseTaskWhileScrolling;
    }

    private void addToCache(Object result, Throwable throwable) {
        // 添加进cache中
        if (result != null && throwable == null) {
            TaskCache.getInstance().putKeyAndValue(getId(), result);
        }
    }

    private void removeTaskFromExecutor() {
        this.state = TaskState.Done;
        // 从executor中移除
        if (weakExecutor != null) {
            TaskExecutor taskExecutor = weakExecutor.get();
            if (taskExecutor != null) {
                taskExecutor.removeTask(this);
            }
        }
    }

    private void notifyLooperToWorkAgain() {
        // queue继续执行
        TaskStack.getInstance().notifyLopper();
    }

}
