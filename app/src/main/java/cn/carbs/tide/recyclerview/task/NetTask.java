package cn.carbs.tide.recyclerview.task;

import cn.carbs.tide.library.producer.Task;
import cn.carbs.tide.library.producer.TaskCallback;
import cn.carbs.tide.library.queue.TaskQueue;
import cn.carbs.tide.request.ResponseCallback;
import cn.carbs.tide.request.RequestManager;

public class NetTask extends Task {

    public String netUrl;

    public NetTask(TaskCallback taskCallback) {
        super(taskCallback);
    }

    public NetTask(String netUrl, TaskCallback taskCallback) {
        this(taskCallback);
        this.netUrl = netUrl;
    }

    @Override
    public Object getId() {
        return netUrl;
    }

    @Override
    public void run() {
        // TODO 自定义
        RequestManager.getInstance().request(netUrl, new ResponseCallback() {
            @Override
            public void onResponseComplete(Object result, Throwable throwable) {
                onTaskDone(result, throwable);
                TaskCallback taskCallback = NetTask.this.getTaskCallback();
                if (taskCallback != null) {
                    if (throwable != null) {
                        taskCallback.onTaskError(NetTask.this, throwable);
                    } else {
                        taskCallback.onTaskCompleted(NetTask.this, result);
                    }
                }
            }
        });
    }

    @Override
    public void onTaskDone(Object result, Throwable throwable) {
        super.onTaskDone(result, throwable);
        TaskQueue.getInstance().notifyLopper();
    }
}
