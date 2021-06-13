package com.clwater.animationprogress2;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
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

    private int tipWidth = 200;
    private int tipHeight = 100;
    private int progress = 50;
    private int triangleWith = 30;
    private float changeProgress = 40;

    @Override
    /**
     * @description 测量当前view宽高
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setProgress(int progress) {
        if (progress != this.progress) {
            invalidate();
            startProgressAnimator(this.progress, progress);
        }
    }

    public void startProgressAnimator(int startProgress, int endProgress){
        //动画差值范围
        ValueAnimator va  = ValueAnimator.ofFloat(startProgress, endProgress);
        va.setDuration(1000);
        //线性取值
        va.setInterpolator(new AnticipateOvershootInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
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
        canvas.translate(0, tipHeight / 3);
//        canvas.translate(tipWidth, 0);
        int tipPosition = (int) ((width - tipWidth) / 100f * progress);

        if (tipPosition < 0 ){
            tipPosition = 0;
        }else if (tipPosition > width - tipWidth){
            tipPosition = width - tipWidth;
        }

//        canvas.rotate(0, tipWidth >> 1, tipHeight + triangleWith);

//        canvas.translate(tipWidth >> 1, 0);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));

        canvas.translate(tipPosition, 0);

        int degrees = (int) (changeProgress / 100f * - 20);

        canvas.rotate(degrees, tipWidth >> 1,
                tipHeight + triangleWith);
        canvas.save();

        canvas.drawRoundRect(0,
                0,
                tipWidth,
                tipHeight,
                30, 30, paint);
        canvas.translate(tipWidth >> 1, tipHeight);

        //实例化路径
        Path path = new Path();
        path.moveTo(-triangleWith >>  1, 0);// 此点为多边形的起点
        path.lineTo(triangleWith >>  1, 0);
        path.lineTo(0, triangleWith);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);

        canvas.restore();
        canvas.save();

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(tipHeight >> 1);
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        int baseLineY = (int) ((tipHeight >> 1) - top/2 - bottom/2);//基线中间点的y轴计算公式

        if(progress < 0){
            progress = 0;
        }else if (progress > 100){
            progress = 100;
        }
        String text = "" +  progress + "%";
        canvas.drawText(text, tipWidth >> 1, baseLineY, textPaint);
    }
}
