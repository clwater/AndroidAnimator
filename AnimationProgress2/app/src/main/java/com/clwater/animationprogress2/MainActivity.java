package com.clwater.animationprogress2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        AnimationProgressTip animationProgressTip = findViewById(R.id.ap_main);
        EditText editProgress = findViewById(R.id.et_progress);

        //设置进度(100)
        findViewById(R.id.bt_progress_100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationProgressTip.setProgress(100);
            }
        });

        //设置进度(0)
        findViewById(R.id.bt_progress_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationProgressTip.setProgress(0);
            }
        });

        //设置进度(自定义)
        findViewById(R.id.bt_progress_set).setOnClickListener(v -> {
            int progress = Integer.parseInt(editProgress.getText().toString());
            if (progress > 100) {
                progress = 100;
            } else if (progress < 0) {
                progress = 0;
            }
            editProgress.setText("" + progress);
            animationProgressTip.setProgress(progress);
        });

        //设置进度(随机)
        findViewById(R.id.bt_progress_random).setOnClickListener(v -> {
            int progress = new Random().nextInt(100);
            editProgress.setText("" + progress);

            animationProgressTip.setProgress(progress);
        });
    }
}