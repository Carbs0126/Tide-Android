package cn.carbs.tide.recyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.carbs.tide.recyclerview.model.ItemData;
import cn.carbs.tide.recyclerview.view.ItemView;


public class TheAdapter extends RecyclerView.Adapter<TheAdapter.TheViewHolder> {

    private Context mContext;
    private ArrayList<ItemData> mDataList;

    public TheAdapter(Context context, ArrayList<ItemData> list) {
        mContext = context;
        mDataList = list;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public TheAdapter.TheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TheViewHolder(new ItemView(mContext));
    }

    @Override
    public void onBindViewHolder(TheViewHolder holder, int position) {
        if (holder == null || holder.itemView == null) {
            return;
        }
        ItemData data = mDataList.get(position);
        if (data == null) {
            return;
        }
        ((ItemView) (holder.itemView)).update(data);
    }

    public ArrayList<ItemData> getDataList() {
        return mDataList;
    }

    class TheViewHolder extends RecyclerView.ViewHolder {
        public TheViewHolder(View itemView) {
            super(itemView);
        }
    }

}