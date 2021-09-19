package cn.carbs.tide.request;

import cn.carbs.tide.library.queue.TaskQueue;

public class RequestManager {

    private RequestManager() {
        init();
    }

    private static volatile RequestManager singleton = null;

    public static RequestManager getInstance() {
        if (singleton == null) {
            synchronized (TaskQueue.class) {
                if (singleton == null) {
                    singleton = new RequestManager();
                }
            }
        }
        return singleton;
    }

    private void init() {

    }

    public void request(String url, ResponseCallback requestCallback) {
        // 线程延时
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (requestCallback != null) {
                    requestCallback.onResponseComplete("data---> : " + url, null);
                }
            }
        }).start();
    }
}
