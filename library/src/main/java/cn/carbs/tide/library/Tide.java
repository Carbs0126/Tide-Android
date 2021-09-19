package cn.carbs.tide.library;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import cn.carbs.tide.library.configuration.TideConfiguration;
import cn.carbs.tide.library.producer.Task;
import cn.carbs.tide.library.queue.TaskStack;
import cn.carbs.tide.library.scroll.OnRecyclerViewScrollListener;

public class Tide {

    private TideConfiguration mConfiguration;
    private OnRecyclerViewScrollListener mOnScrollListener;

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

    public static Tide with(RecyclerView recyclerView) {
        Tide tide = getInstance();
        if (recyclerView != null) {
            OnRecyclerViewScrollListener onRecyclerViewScrollListener = tide.getOnScrollListener();
            if (onRecyclerViewScrollListener.getRecyclerView() != recyclerView) {
                Log.d("wangwangwang", "=====> with addOnScrollListener");
                recyclerView.addOnScrollListener(onRecyclerViewScrollListener);
                onRecyclerViewScrollListener.setRecyclerView(recyclerView);
            }
        }
        return tide;
    }

    private void init() {
        initConfiguration();
        initOnScrollListener();
    }

    private void initConfiguration() {
        if (mConfiguration == null) {
            mConfiguration = new TideConfiguration();
        }
    }

    private void initOnScrollListener() {
        if (mOnScrollListener == null) {
            mOnScrollListener = new OnRecyclerViewScrollListener();
        }
    }

    public OnRecyclerViewScrollListener getOnScrollListener() {
        if (mOnScrollListener == null) {
            initOnScrollListener();
        }
        return mOnScrollListener;
    }

    public Tide skipCache() {
        mConfiguration.skipCache = true;
        return this;
    }

    public Tide skipTaskWhileScrolling() {
        mConfiguration.pauseTaskWhileScrolling = true;
        return this;
    }

    public Tide put(Task task) {
        task.setConfiguration(mConfiguration);
        TaskStack.getInstance().put(task);
        return this;
    }

    public void start() {
        TaskStack.getInstance().notifyLopper();
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
