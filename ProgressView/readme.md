---
title: 每周一个自定义View(1) -动态ProgressView
date: 2021-06-03 21:54:14
tags:
---

# 每周一个自定义View(1) -动态ProgressView
> 新的一个系列, 应该是计划每周实现一个自定义View, 看看能检查到多久吧

>这次就从一个常见的ProgressBark开始吧, 最近的项目中使用了一个Progress显示文件下载进度的功能, 设计给的是一个静态的图片, 也没有说需要具体实现的情况, 后面优化的时候刚好有了性质, 就有了下面的这个AnimatorProgressBar.

<!-- more -->

## 效果展示
<img src="https://update-image.oss-cn-shanghai.aliyuncs.com/blog/prorgessview/progressview.gif" width = "300" height = "500" alt="ProgressView" align=center />

### 支持功能
* 基本的的进度设置(当前默认为0-100)
* 颜色定义, 使用的颜色都是可以设置的, 满足各样的ui需求
* 元素定义, 作为展示的Progress中的线条可以设置宽度和间距
* 动画控制, 动画效果可以设置展示速度, 总有一个组合适合你

### 设计过程
![图层](https://update-image.oss-cn-shanghai.aliyuncs.com/blog/prorgessview/WX20210603-221636.png)

将相关的view分为了四层, 从下至上分别为

* 背景图层
 用于显示整个view的背景
* 进度图层
 相当于进度条的背景颜色
* 线段动画图层
 在这里绘制出现的线段, 并控制器动画的效果
* 遮罩展示图层
 这里使用了遮罩展示的方法, 控制遮罩图层的进度和样式来表现实际的展示效果


 #### 实现过程
 首先是背景如果绘制的, 简单来说就是两个圆形和一个矩形, 具体的动画效果可以参考下面,
 ![https://update-image.oss-cn-shanghai.aliyuncs.com/blog/prorgessview/progress_1.gif](https://update-image.oss-cn-shanghai.aliyuncs.com/blog/prorgessview/progress_1.gif)


其它的图层基本上也都是比较类似的情况, 除此之外就是线条的实现

![https://update-image.oss-cn-shanghai.aliyuncs.com/blog/prorgessview/WX20210605-000109.png](https://update-image.oss-cn-shanghai.aliyuncs.com/blog/prorgessview/WX20210605-000109.png)

其中中心黑色的为展示的区域, 白色的是绘制的区域

## 代码实现

### 可编辑参数
```java
  //下面为可自定义编辑的参数
    //背景颜色
    private int viewBackGroundColor;
    //线条颜色
    private int viewLineColor;
    //进度条颜色
    private int viewProgressColor;
    //线条见间隔
    private int offsetLine;
    //执行动画需要的时间
    private int animatorTime;
    //进度变化动画需要的时间
    private int animatorProgressTime;
    //画笔/线条的宽度
    private int paintWidth;

```

### 背景图层&绘制图层背景
这两个图形的实现比较类似

```java
    /**
     * @description 绘制图层背景
     */
    private void getProgressBackground(Canvas canvas){
        //设置画笔
        Paint paint = new Paint();
        paint.setColor(viewProgressColor);

        //确定两个圆形的中心位置及半径
        int leftCirclePoint = height / 2;
        int rightCirclePoint = width - leftCirclePoint;
        int radius = height / 2;

        //绘制左侧和右侧的圆形填充
        canvas.drawCircle(leftCirclePoint, height >> 1, radius, paint);
        canvas.drawCircle(rightCirclePoint, height >> 1, radius, paint);

        //绘制中心的矩形填充
        Rect rect = new Rect(leftCirclePoint, 0, rightCirclePoint, height);
        canvas.drawRect(rect, paint);
    }

```

```java
    /**
     * @description 绘制背景图层
     */
    private void drawViewBackground(Canvas canvas) {
        canvas.save();

        Paint paint = new Paint();
        paint.setColor(viewBackGroundColor);

        int leftCirclePoint = height / 2;
        int rightCirclePoint = width - leftCirclePoint;
        int radius = height / 2;

        canvas.drawCircle(leftCirclePoint, height >> 1, radius, paint);
        canvas.drawCircle(rightCirclePoint, height >> 1, radius, paint);

        Rect rect = new Rect(leftCirclePoint, 0, rightCirclePoint, height);
        canvas.drawRect(rect, paint);
    }
```

### 绘制遮罩图层
```java
    /**
     * @description 绘制遮罩图层
     */
    private Bitmap progressBitmapDst(){
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        //如果当前进度为0, 则不展示任何内容
        if (progress == 0){
            return bitmap;
        }
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);


        //遮罩图层宽度与当前progress进度有关
        int leftCirclePoint = height / 2;
        int rightCirclePoint = leftCirclePoint + (int) ((width - height) / 1f * progress / 100f);

        int radius = height / 2;

        canvas.drawCircle(leftCirclePoint, height >> 1, radius, paint);
        canvas.drawCircle(rightCirclePoint, height >> 1, radius, paint);

        Rect rect = new Rect(leftCirclePoint, 0, rightCirclePoint, height);
        canvas.drawRect(rect, paint);


        return bitmap;
    }
```

### 绘制线条动画
```java
    /**
     * @description 绘制线条动画
     */
    private void getProgressLines(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(viewLineColor);

        //设置画笔宽度
        paint.setStrokeWidth(paintWidth);

        //设置画笔圆形边界
        paint.setStrokeCap(Paint.Cap.ROUND);
        //todo 动画效果展示优化
        canvas.translate(-offsetLine * 2, 0);
        //offsetAnimator 为动画展示时使用, 每次移动一定的长度,
        //通过连续的移动来实现视觉上的动画效果
        canvas.translate(offsetAnimator, 0);
        canvas.save();

        int indexOffset = 0;

        //依次绘制线条
        do {
            //绘制右上到左下的线条
            canvas.drawLine(20, -20, -offsetLine - 20, height + 20, paint);
            indexOffset += offsetLine;
            //偏移位置
            canvas.translate(offsetLine, 0);
        }while (indexOffset < width + offsetLine * 4);

        canvas.restore();
    }
```    


### 动画效果
```java
    /**
     * @description 进度变化动画
     * @param startProgress
     * @param endProgress
     */
    public void startProgressAnimator(int startProgress, int endProgress){
        //动画差值范围
        ValueAnimator va  = ValueAnimator.ofFloat(startProgress, endProgress);
        va.setDuration(animatorProgressTime);
        //线性取值
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }

        });


        va.start();
    }


    /**
     * @description 开始动画
     */
    public void startAnimator(){
        //动画差值范围
        ValueAnimator va  = ValueAnimator.ofFloat(0f, offsetLine);
        //循环播放
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setDuration(animatorTime);
        //线性取值
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                offsetAnimator = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        va.start();
    }
```

## 最后
相关代码可以访问[我的GitHub](https://github.com/clwater/AndroidAnimator/tree/main/ProgressView)来获取,欢迎大家start或者提供建议.