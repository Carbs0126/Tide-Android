package cn.carbs.tide.library.queue;

import android.util.Log;

import java.util.Stack;

import cn.carbs.tide.library.producer.Task;

// 单例
public class TaskStack {

    private Stack<Task> mStack;

    private TaskStack() {
        init();
    }

    private static volatile TaskStack singleton = null;

    public static TaskStack getInstance() {
        if (singleton == null) {
            synchronized (TaskStack.class) {
                if (singleton == null) {
                    singleton = new TaskStack();
                }
            }
        }
        return singleton;
    }

    private void init() {
        mStack = new Stack<>();
    }

    // TODO put 的时候不要直接启动
    public void put(Task task) {
        if (mStack == null) {
            mStack = new Stack<>();
        }
        // 添加一个task
        mStack.push(task);
    }

    // TODO 是否使用观察者模式
    // 先简化，使用一个TaskLooper
    public void notifyLopper() {
        Log.d("wangwang", "---> notifyLooper mStack : " + mStack.size());
        TaskLooper.getInstance().handleTasksInStack(mStack);
    }

}
