package cn.carbs.tide.recyclerview.task;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import cn.carbs.tide.library.producer.Task;
import cn.carbs.tide.library.producer.TaskCallback;
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
                Log.d("wangwang", "onResponseComplete ---- current thread name : " + Thread.currentThread().getName());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("wangwang", "onResponseComplete Handler ---- current thread name : " + Thread.currentThread().getName());
                        onTaskDone(getId(), result, throwable);
                        runTaskCallback(result, throwable);
                    }
                });
            }
        });
    }

    @Override
    public void onTaskDone(Object id, Object result, Throwable throwable) {
        super.onTaskDone(id, result, throwable);
    }
}
