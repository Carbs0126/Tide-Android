package cn.carbs.tide.request;

public interface ResponseCallback {

    void onResponseComplete(Object result, Throwable throwable);

}
