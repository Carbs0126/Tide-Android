package cn.carbs.tide.library.scroll;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

import cn.carbs.tide.library.Tide;

public class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private WeakReference<RecyclerView> mWeakRecyclerView;

    // public static final int SCROLL_STATE_IDLE = 0;
    // public static final int SCROLL_STATE_DRAGGING = 1;
    // public static final int SCROLL_STATE_SETTLING = 2;
    private int mCurrentState = RecyclerView.SCROLL_STATE_IDLE;

    public void setRecyclerView(RecyclerView recyclerView) {
        mWeakRecyclerView = new WeakReference<RecyclerView>(recyclerView);
    }

    public RecyclerView getRecyclerView() {
        if (mWeakRecyclerView == null) {
            return null;
        }
        return mWeakRecyclerView.get();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        Log.d("wangwangwang", "====> onScrollStateChanged newState : " + newState);
        // SCROLL_STATE_IDLE 或者 SCROLL_STATE_DRAGGING 仍可以继续task
        // SCROLL_STATE_SETTLING 停止 run task
        mCurrentState = newState;
        if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

        } else {
            Tide.getInstance().start();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    public int getCurrentState() {
        return mCurrentState;
    }
}
