package com.clwater.progressview;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

import top.defaults.colorpicker.ColorPickerPopup;

public class MainActivity extends AppCompatActivity {

    private AnimatorProgressView animatorProgressView;
    private EditText etProgress;
    private MaterialTextView tvLineWidth;
    private MaterialTextView tvLineInterval;
    private MaterialTextView tv_line_animator;
    private MaterialTextView tv_line_animator_progress;
    private View vw_color_base_background;
    private int backgroundColor;
    private String progressBackgroundColor;
    private String LineColor;
    private MaterialTextView tv_color_base_background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
    }

    private void initView() {
        animatorProgressView = findViewById(R.id.ap_main);
        etProgress = findViewById(R.id.et_progress);
        tvLineWidth = findViewById(R.id.tv_line_width);
        tvLineInterval = findViewById(R.id.tv_line_interval);
        tv_line_animator = findViewById(R.id.tv_line_animator);
        tv_line_animator_progress = findViewById(R.id.tv_line_animator_progress);
        vw_color_base_background = findViewById(R.id.vw_color_base_background);
        tv_color_base_background = findViewById(R.id.tv_color_base_background);

        findViewById(R.id.bt_progress_set).setOnClickListener(v -> {
            int progress = Integer.parseInt(etProgress.getText().toString());
            if (progress > 100) {
                progress = 100;
            } else if (progress < 0) {
                progress = 0;
            }
            etProgress.setText("" + progress);
            animatorProgressView.setProgress(progress);
        });

        findViewById(R.id.bt_progress_0).setOnClickListener(v -> {
            etProgress.setText("" + 0);

            animatorProgressView.setProgress(0);
        });
        findViewById(R.id.bt_progress_100).setOnClickListener(v -> {
            etProgress.setText("" + 100);
            animatorProgressView.setProgress(100);
        });
        findViewById(R.id.bt_progress_random).setOnClickListener(v -> {
            int progress = new Random().nextInt(100);
            etProgress.setText("" + progress);

            animatorProgressView.setProgress(progress);
        });


        ((AppCompatSeekBar)findViewById(R.id.sb_line_width)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animatorProgressView.setPaintWidth(progress);
                tvLineWidth.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ((AppCompatSeekBar)findViewById(R.id.sb_line_interval)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animatorProgressView.setOffsetLine(progress);
                tvLineInterval.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ((AppCompatSeekBar)findViewById(R.id.sb_line_animator)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animatorProgressView.setAnimatorTime(progress);
                tv_line_animator.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((AppCompatSeekBar)findViewById(R.id.sb_line_animator_progress)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animatorProgressView.setAnimatorProgressTime(progress);
                tv_line_animator_progress.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        backgroundColor = 0xFFCECECE;

        vw_color_base_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerPopup.Builder(MainActivity.this)
                        .initialColor(backgroundColor) // Set initial color
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(vw_color_base_background , new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                backgroundColor = color;
                                tv_color_base_background.setText(String.format("#%06X", color));
                                animatorProgressView.setViewBackGroundColor(color);
                                vw_color_base_background.setBackgroundColor(color);
                            }
                        });
            }
        });


    }


}