package com.clwater.animationprogress2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        AnimationProgressTip animationProgressTip = findViewById(R.id.ap_main);
        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationProgressTip.setProgress(100);
            }
        });
        findViewById(R.id.bt_test_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationProgressTip.setProgress(0);
            }
        });
    }
}