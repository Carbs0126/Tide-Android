package cn.carbs.tide.library;

import cn.carbs.tide.library.configuration.TideConfiguration;
import cn.carbs.tide.library.producer.Task;
import cn.carbs.tide.library.queue.TaskQueue;

public class Tide {

    private TideConfiguration mConfiguration = new TideConfiguration();

    private Tide() {
        init();
    }

    private static volatile Tide singleton = null;

    public static Tide getInstance() {
        if (singleton == null) {
            synchronized (Tide.class) {
                if (singleton == null) {
                    singleton = new Tide();
                }
            }
        }
        return singleton;
    }

    private void init() {

    }

    public Tide skipCache() {
        mConfiguration.skipCache = true;
        return this;
    }

    public Tide skipTaskWhileScrolling() {
        mConfiguration.skipTaskWhileScrolling = true;
        return this;
    }

    public Tide put(Task task) {
        task.setConfiguration(mConfiguration);
        TaskQueue.getInstance().put(task);
        return this;
    }

    public void start() {
        TaskQueue.getInstance().notifyLopper();
    }

    public Tide setMaxConcurrentTaskCount(int maxConcurrentTaskCount) {
        mConfiguration.maxConcurrentTaskCount = maxConcurrentTaskCount;
        return this;
    }

    public int getMaxConcurrentTaskCount() {
        if (mConfiguration == null) {
            return 0;
        }
        return mConfiguration.maxConcurrentTaskCount;
    }

}
