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
    private View vw_color_progress_background;
    private View vw_color_line;
    private MaterialTextView tv_color_base_background;
    private MaterialTextView tv_color_progress_background;
    private MaterialTextView tv_color_base_line;

    //默认展示相关颜色
    private int backgroundColor = 0xFFCECECE;
    private int viewLineColor = 0xFFFFFFFF;
    private int viewProgressColor = 0xFF000000;

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
        vw_color_progress_background = findViewById(R.id.vw_color_progress_background);
        tv_color_progress_background = findViewById(R.id.tv_color_progress_background);
        vw_color_line = findViewById(R.id.vw_color_line);
        tv_color_base_line = findViewById(R.id.tv_color_base_line);

        //设置进度(自定义)
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

        //设置进度(0)
        findViewById(R.id.bt_progress_0).setOnClickListener(v -> {
            etProgress.setText("" + 0);

            animatorProgressView.setProgress(0);
        });

        //设置进度(100)
        findViewById(R.id.bt_progress_100).setOnClickListener(v -> {
            etProgress.setText("" + 100);
            animatorProgressView.setProgress(100);
        });

        //设置进度(随机)
        findViewById(R.id.bt_progress_random).setOnClickListener(v -> {
            int progress = new Random().nextInt(100);
            etProgress.setText("" + progress);

            animatorProgressView.setProgress(progress);
        });

        //设置动画中线的宽度
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

        //设置动画中线之间的间隔
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

        //设置动画速度
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

        //设置progress进度的速度
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

        //设置背景颜色
        vw_color_base_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerPopup.Builder(MainActivity.this)
                        .initialColor(backgroundColor)
                        .enableAlpha(true)
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

        //设置显示进度的背景颜色
        vw_color_progress_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerPopup.Builder(MainActivity.this)
                        .initialColor(viewProgressColor)
                        .enableAlpha(true)
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(vw_color_progress_background , new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                viewProgressColor = color;
                                tv_color_progress_background.setText(String.format("#%06X", color));
                                animatorProgressView.setViewProgressColor(color);
                                vw_color_progress_background.setBackgroundColor(color);
                            }
                        });
            }
        });


        //设置线的颜色
        vw_color_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerPopup.Builder(MainActivity.this)
                        .initialColor(viewLineColor)
                        .enableAlpha(true)
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(vw_color_line , new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                viewLineColor = color;
                                tv_color_base_line.setText(String.format("#%06X", color));
                                animatorProgressView.setViewLineColor(color);
                                vw_color_line.setBackgroundColor(color);
                            }
                        });
            }
        });

        //随机生成一种颜色的组合
        findViewById(R.id.bt_color_random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundColor = randomColor();
                viewProgressColor = randomColor();
                viewLineColor = randomColor();
                animatorProgressView.setViewBackGroundColor(backgroundColor);
                animatorProgressView.setViewProgressColor(viewProgressColor);
                animatorProgressView.setViewLineColor(viewLineColor);

                tv_color_base_background.setText(String.format("#%06X", backgroundColor));
                vw_color_base_background.setBackgroundColor(backgroundColor);

                tv_color_progress_background.setText(String.format("#%06X", viewProgressColor));
                vw_color_progress_background.setBackgroundColor(viewProgressColor);

                tv_color_base_line.setText(String.format("#%06X", viewLineColor));
                vw_color_line.setBackgroundColor(viewLineColor);

            }
        });

    }

    /**
     * @description 生成随机颜色
     */
    private int randomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }

}