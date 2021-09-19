package cn.carbs.tide.recyclerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.carbs.tide.R;
import cn.carbs.tide.library.Tide;
import cn.carbs.tide.library.producer.Task;
import cn.carbs.tide.library.producer.TaskCallback;
import cn.carbs.tide.library.queue.TaskQueue;
import cn.carbs.tide.recyclerview.model.ItemData;
import cn.carbs.tide.recyclerview.task.NetTask;

public class ItemView extends RelativeLayout {

    private TextView mTVTitle;
    private TextView mTVContent1;
    private TextView mTVContent2;

    private ItemData mData;

    public ItemView(Context context) {
        super(context);
        init(context);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_view, this);
        mTVTitle = findViewById(R.id.tv_title);
        mTVContent1 = findViewById(R.id.tv_content_1);
        mTVContent2 = findViewById(R.id.tv_content_2);
    }

    public void update(ItemData data) {
        if (data == null) {
            return;
        }
        mData = data;
        mTVTitle.setText(mData.title);
        mTVContent1.setText("ID : " + mData.dataID);
        mTVContent2.setText("url : " + mData.url);
        doTask();
    }

    private void doTask() {

        Tide
                .getInstance()
                .setMaxConcurrentTaskCount(2)
                .skipTaskWhileScrolling()
                .put(new NetTask(mData.url, new TaskCallback() {
                    @Override
                    public void onTaskCompleted(Task task, Object result) {
                    }

                    @Override
                    public void onTaskError(Task task, Throwable throwable) {
                    }
                }))
                .start();
    }
}
