package cn.carbs.tide.library.consumer;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Stack;

import cn.carbs.tide.library.Tide;
import cn.carbs.tide.library.cache.TaskCache;
import cn.carbs.tide.library.producer.Task;
import cn.carbs.tide.library.producer.TaskState;
import cn.carbs.tide.library.queue.TaskStack;
import cn.carbs.tide.library.scroll.OnRecyclerViewScrollListener;

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
    }

    public void executeTasks() {
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
                            Log.d("aaa", "===========> cached");
                            // TODO
                            TaskStack.getInstance().notifyLopper();
                            continue;
                        }
                    }
                    if (taskItem.getIfPauseTaskWhileScrolling()) {
                        // 判断是否需要在滑动时停止执行task
                        // TODO Tide 不应该有全局listener
                        OnRecyclerViewScrollListener scrollListener = Tide.getInstance().getOnScrollListener();
                        if (scrollListener != null
                                && scrollListener.getCurrentState() == RecyclerView.SCROLL_STATE_SETTLING) {
                            // 如果正在滑动
                            return;
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
            if (mTasks == null || mTasks.isEmpty()) {
                return;
            }
            int size = mTasks.size();
            for (int i = size - 1; i >= 0; i--) {
                Task task = mTasks.get(i);
                if (task != null && task.state == TaskState.Done) {
                    mTasks.remove(task);
                }
            }
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