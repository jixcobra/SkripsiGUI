package com.example.deteksi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class ActivityDetail extends AppCompatActivity {

    private RecyclerView recyclerView;
    private detailAdapter adapter;
    private ArrayList<detail> detailArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailArrayList = getIntent().getExtras().getParcelableArrayList("detailArrayList");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new detailAdapter(detailArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityDetail.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        Log.e("detail",""+detailArrayList);
    }

//    private void getData() {
//        Log.w("detail",""+detailArrayList.get(0).getPackageName());
//    }
}
