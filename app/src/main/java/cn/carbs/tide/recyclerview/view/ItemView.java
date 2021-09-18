package cn.carbs.tide.recyclerview.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.carbs.tide.R;
import cn.carbs.tide.recyclerview.model.ItemData;

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
    }
}
