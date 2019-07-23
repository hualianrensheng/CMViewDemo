package com.test.cmviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by muchen on 2018/1/23.
 */

public class CMProgressView extends View {

    //外弧颜色
    private int cmProgressOuterColr = Color.BLUE;
    //内弧颜色
    private int cmProgressInnerColor = Color.RED;
    //圆弧宽度
    private int cmProgressBorderWidth = 10;
    //字体颜色
    private int cmProgressTextColor = Color.RED;
    //字体大小
    private int cmProgressTextSize = 20;

    private Paint cmProgressOuterPaint,cmProgressInnerPaint,cmProgressTextPaint;

    //最大进度
    private int cmProgressMax = 0;
    //当前进度
    private int cmProgressCurrent = 0;


    public CMProgressView(Context context) {
        this(context,null);
    }

    public CMProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CMProgressView);
        cmProgressOuterColr = typedArray.getColor(R.styleable.CMProgressView_cmProgressOuterColor, cmProgressOuterColr);
        cmProgressInnerColor = typedArray.getColor(R.styleable.CMProgressView_cmProgressInnerColor, cmProgressInnerColor);
        cmProgressBorderWidth = typedArray.getDimensionPixelOffset(R.styleable.CMProgressView_cmProgressBorderWidth,cmProgressBorderWidth);
        cmProgressTextColor = typedArray.getColor(R.styleable.CMProgressView_cmProgressTextColor,cmProgressTextColor);
        cmProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.CMProgressView_cmProgressTextSize,cmProgressTextSize);
        typedArray.recycle();

        //初始化画笔
        cmProgressOuterPaint = new Paint();
        cmProgressOuterPaint.setAntiAlias(true);
        cmProgressOuterPaint.setColor(cmProgressOuterColr);
        cmProgressOuterPaint.setStrokeWidth(cmProgressBorderWidth);
        cmProgressOuterPaint.setStyle(Paint.Style.STROKE);
        cmProgressOuterPaint.setStrokeCap(Paint.Cap.ROUND);

        cmProgressInnerPaint = new Paint();
        cmProgressInnerPaint.setAntiAlias(true);
        cmProgressInnerPaint.setColor(cmProgressInnerColor);
        cmProgressInnerPaint.setStrokeWidth(cmProgressBorderWidth);
        cmProgressInnerPaint.setStyle(Paint.Style.STROKE);
        cmProgressInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        cmProgressTextPaint = new Paint();
        cmProgressTextPaint.setAntiAlias(true);
        cmProgressTextPaint.setTextSize(cmProgressTextSize);
        cmProgressTextPaint.setColor(cmProgressTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //从新测量 宽高，如果是AT_MOST 模式，则赋默认值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if(MeasureSpec.AT_MOST == widthMode){
            width = 40;
        }

        if(MeasureSpec.AT_MOST == heightMode){
            height = 40;
        }

        width = (height > width ? width : height);
        height = (height > width ? width : height);

        //设置宽高
        setMeasuredDimension(width,height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1.画内圆弧
        canvas.drawCircle(getWidth()/2,getWidth()/2,getWidth()/2-cmProgressBorderWidth/2,cmProgressOuterPaint);

        //2.画外圆弧
        RectF rectF = new RectF(0+cmProgressBorderWidth/2,0+cmProgressBorderWidth/2,getWidth()-cmProgressBorderWidth/2,getHeight()-cmProgressBorderWidth/2);
        float precent = (float)cmProgressCurrent/cmProgressMax;
        canvas.drawArc(rectF,0,precent*360,false,cmProgressInnerPaint);

        //3.画进度文字
        String text = String.valueOf((int)(precent*100))+"%";
        //找基线
        Rect textBounds = new Rect();
        cmProgressTextPaint.getTextBounds(text,0,text.length(),textBounds);

        int dx = getWidth()/2 - textBounds.width()/2;
        int dy = (textBounds.bottom - textBounds.top)/2 - textBounds.bottom;
        int baseLine = getHeight()/2 + dy;
        canvas.drawText(text,dx,baseLine,cmProgressTextPaint);
    }

    public synchronized void setProgressMas(int max){
        if(0 > max){
            throw new IllegalArgumentException("最大值需大于0");
        }
        this.cmProgressMax = max;
    }

    public synchronized void setProgressCurrent(int current){
        if( cmProgressMax < current){
            throw new IllegalArgumentException("当前进度需小于最大值");
        }
        this.cmProgressCurrent = current;
        invalidate();
    }
}
