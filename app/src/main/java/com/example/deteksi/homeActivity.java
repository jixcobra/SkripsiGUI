package com.example.deteksi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homeActivity extends AppCompatActivity {

    private Button btnUji;
    private Button btnDeteksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setInit();
        setClickEvent();
    }

    private void setInit() {
        btnUji = findViewById(R.id.ujiDataset);
        btnDeteksi = findViewById(R.id.deteksi);
    }

    private void setClickEvent() {
        btnUji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(homeActivity.this, ActivityUji.class);
                startActivity(pindah);
            }
        });

        btnDeteksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(homeActivity.this, ActivityDeteksi.class);
                startActivity(pindah);
            }
        });
    }
}
