package com.clwater.progressview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private AnimatorProgressView animatorProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animatorProgressView = findViewById(R.id.ap_main);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress < 100){
                    animatorProgressView.setProgress(progress);
                    try {
                        Thread.sleep(1000);
                        progress += 10;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}