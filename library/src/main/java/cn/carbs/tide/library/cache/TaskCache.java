package cn.carbs.tide.library.cache;

import java.util.HashMap;

public class TaskCache {

    private HashMap<Object, Object> mMapOfIdAndResult;
    private TaskCache() {
        init();
    }

    private static volatile TaskCache singleton = null;

    public static TaskCache getInstance() {
        if (singleton == null) {
            synchronized (TaskCache.class) {
                if (singleton == null) {
                    singleton = new TaskCache();
                }
            }
        }
        return singleton;
    }

    private void init() {
        if (mMapOfIdAndResult == null) {
            mMapOfIdAndResult = new HashMap<>();
        }
    }

    public boolean putKeyAndValue(Object key, Object value) {
        if (key == null || value == null) {
            return false;
        }
        if (mMapOfIdAndResult == null) {
            mMapOfIdAndResult = new HashMap<>();
        }
        mMapOfIdAndResult.put(key, value);
        return true;
    }

    public Object getValueByKey(Object key) {
        if (key == null) {
            return null;
        }
        if (mMapOfIdAndResult == null) {
            mMapOfIdAndResult = new HashMap<>();
        }
        return mMapOfIdAndResult.get(key);
    }

}
