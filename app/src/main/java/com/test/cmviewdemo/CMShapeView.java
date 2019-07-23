package com.test.cmviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画 圆形，正方形，三角形，并提供方法，不断切换
 * Created by muchen on 2018/1/23.
 */

public class CMShapeView extends View {

    private Paint cmPaint;
    private Path cmPath;

    private CMShape cmCurrentShape = CMShape.Circle;

    public enum CMShape{
        Circle,Square,Tirangle
    }

    public CMShapeView(Context context) {
        this(context,null);
    }

    public CMShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //创建画笔
        cmPaint = new Paint();
        cmPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //保证是正方形
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int hightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hight = MeasureSpec.getSize(heightMeasureSpec);

        if(MeasureSpec.AT_MOST == widthMode){
            width = 40;
        }

        if(MeasureSpec.AT_MOST == hightMode){
            hight = 40;
        }

        width = (width > hight ? hight : width);
        hight = (width > hight ? hight : width);

        setMeasuredDimension(width,hight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        switch (cmCurrentShape){
            case Circle://圆形 画正方形
                int center = getHeight()/2;
                cmPaint.setColor(Color.YELLOW);
                canvas.drawCircle(center,center,center,cmPaint);
                break;
            case Square://正方形 画三角形
                cmPaint.setColor(Color.BLUE);
                Rect rect = new Rect(0,0,getWidth(),getHeight());
                canvas.drawRect(rect,cmPaint);
                break;
            case Tirangle://三角形 画圆形
                cmPaint.setColor(Color.RED);
                if(cmPath == null){
                    cmPath = new Path();
                    cmPath.moveTo(getWidth()/2,0);
                    cmPath.lineTo(0,getHeight());
                    cmPath.lineTo(getWidth(),getHeight());
                    cmPath.close();
                }
                canvas.drawPath(cmPath,cmPaint);
                break;
        }
    }

    public synchronized void exChange(){
        switch (cmCurrentShape){
            case Tirangle:
                cmCurrentShape = CMShape.Square;
                break;
            case Square:
                cmCurrentShape = CMShape.Circle;
                break;
            case Circle:
                cmCurrentShape = CMShape.Tirangle;
                break;
        }
        invalidate();
    }
}
