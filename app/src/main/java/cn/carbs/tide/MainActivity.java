package cn.carbs.tide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import cn.carbs.tide.recyclerview.adapter.TheAdapter;
import cn.carbs.tide.recyclerview.model.ItemData;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initData() {
        ArrayList<ItemData> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            ItemData itemData = new ItemData();
            itemData.dataID = i;
            itemData.title = "标题：" + i;
            itemData.url = "www.example.com/test?index=" + i;
            list.add(itemData);
        }
        TheAdapter adapter = new TheAdapter(this, list);
        mRecyclerView.setAdapter(adapter);
    }
}