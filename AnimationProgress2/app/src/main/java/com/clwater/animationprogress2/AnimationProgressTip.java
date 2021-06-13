package com.clwater.animationprogress2;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

public class AnimationProgressTip extends View {
    public AnimationProgressTip(Context context) {
        super(context);
    }

    public AnimationProgressTip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationProgressTip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //当前view 宽度和高度
    private int width = 0;
    private int height = 0;

    //提示框的宽度
    private int tipWidth = 200;
    //提示框的高度
    private int tipHeight = 100;
    //提示框底部三甲区域的边长
    private int triangleWith = 30;
    //滑动过程中变化的角度
    private int maxDegrees = -20;

    private int backgroundColor = 0xff000000;
    private int textColor = 0xffffffff;

    //当前展示的进度
    private int progress = 0;
    //当前需要变化的进度(用于角度变化)
    private float changeProgress = 0;

    @Override
    /**
     * @description 测量当前view宽高
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * @description 更新当前的进度
     */
    public void setProgress(int progress) {
        if (progress != this.progress) {
            invalidate();
            startProgressAnimator(this.progress, progress);
        }
    }

    /**
     * @description 设置进度动画
     */
    public void startProgressAnimator(int startProgress, int endProgress) {
        //动画差值范围
        ValueAnimator va = ValueAnimator.ofFloat(startProgress, endProgress);
        va.setDuration(1000);
        //差值器取值
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //更新当前进度和变化进度的取值
                float currentProgress = (float) valueAnimator.getAnimatedValue();
                progress = (int) currentProgress;
                changeProgress = endProgress - currentProgress;
                invalidate();
            }

        });


        va.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //绘制区域下移, 避免顶部动画时内容缺失
        canvas.translate(0, tipHeight / 3);
        //更新绘制坐标位置
        int tipPosition = (int) ((width - tipWidth) / 100f * progress);

        //避免差值器过度至异常位置
        if (tipPosition < tipWidth / 6) {
            tipPosition = tipWidth / 6;
        } else if (tipPosition > width - tipWidth / 6f * 7) {
            tipPosition = (int) (width - tipWidth / 6f * 7);
        }

        //背景绘制Paint
        Paint paint = new Paint();
        paint.setColor(backgroundColor);

        //移动到中心的位置
        canvas.translate(tipPosition, 0);

        //更新需要变化的角度
        int degrees = (int) (changeProgress / 100f * maxDegrees);

        //旋转整体
        canvas.rotate(degrees, tipWidth >> 1,
                tipHeight + triangleWith);
        canvas.save();

        //绘制圆角矩形区域
        canvas.drawRoundRect(0,
                0,
                tipWidth,
                tipHeight,
                30, 30, paint);
        canvas.translate(tipWidth >> 1, tipHeight);

        //绘制底部三角区域
        Path path = new Path();
        path.moveTo(-triangleWith >> 1, 0);
        path.lineTo(triangleWith >> 1, 0);
        path.lineTo(0, triangleWith);
        path.close();
        canvas.drawPath(path, paint);

        canvas.restore();
        canvas.save();

        //文件绘制Paint
        Paint textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(tipHeight >> 1);
        textPaint.setStyle(Paint.Style.FILL);

        //使得在文字x轴居中
        textPaint.setTextAlign(Paint.Align.CENTER);

        //使得在文字y轴居中
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) ((tipHeight >> 1) - top / 2 - bottom / 2);

        //避免差值器引起的取值异常
        if (progress < 0) {
            progress = 0;
        } else if (progress > 100) {
            progress = 100;
        }
        //更新需要展示的文字信息
        String text = "" + progress + "%";
        canvas.drawText(text, tipWidth >> 1, baseLineY, textPaint);
    }
}
