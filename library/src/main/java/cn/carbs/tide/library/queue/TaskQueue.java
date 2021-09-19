package cn.carbs.tide.library.queue;

import java.util.LinkedList;
import java.util.Queue;

import cn.carbs.tide.library.producer.Task;

// 单例
public class TaskQueue {

    private Queue<Task> mQueue;

    private TaskQueue() {
        init();
    }

    private static volatile TaskQueue singleton = null;

    public static TaskQueue getInstance() {
        if (singleton == null) {
            synchronized (TaskQueue.class) {
                if (singleton == null) {
                    singleton = new TaskQueue();
                }
            }
        }
        return singleton;
    }

    private void init() {
        mQueue = new LinkedList<Task>();
    }

    // TODO put 的时候不要直接启动
    public void put(Task task) {
        if (mQueue == null) {
            mQueue = new LinkedList<Task>();
        }
        // 添加一个task
        mQueue.offer(task);
//        notifyLopper();
    }

    // TODO 是否使用观察者模式
    // 先简化，使用一个TaskLooper
    public void notifyLopper() {
        TaskLooper.getInstance().handleTasksInQueue(mQueue);
    }

}
