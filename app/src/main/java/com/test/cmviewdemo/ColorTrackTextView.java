package com.test.cmviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by muchen on 2018/1/22.
 */

public class ColorTrackTextView extends AppCompatTextView {


    private Paint cmOriginTextPaint;
    private Paint cmChangeTextPaint;

    //当前进度
    private float cmCurrentProgress;

    //默认朝向
    private Directory cmCurrentDirectory = Directory.LEFT_TO_RIGHT;
    public enum Directory{
        LEFT_TO_RIGHT,RIGHT_TO_LEFT
    }

    public ColorTrackTextView(Context context) {
        this(context,null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //创建画笔
        initPaint(context,attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        int cmOriginTextColor = typedArray.getColor(R.styleable.ColorTrackTextView_cmOriginTextColor, getTextColors().getDefaultColor());
        int cmChangeTextColor = typedArray.getColor(R.styleable.ColorTrackTextView_cmChangeTextColor, getTextColors().getDefaultColor());
        typedArray.recycle();

        //根据自定义的颜色来创建画笔
        cmOriginTextPaint = getTextPaintByColor(cmOriginTextColor);
        cmChangeTextPaint = getTextPaintByColor(cmChangeTextColor);

    }

    /**
     * 根据自定义画笔，重写onDray()方法，重写绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        //根据当前进度，获取当前中间值
        int middle = (int) (cmCurrentProgress * getWidth());

        //根据朝向，绘制TextView
        if(Directory.LEFT_TO_RIGHT == cmCurrentDirectory){
            //当前朝向为  从左到右
            drawText(canvas,cmChangeTextPaint,0,middle);
            drawText(canvas,cmOriginTextPaint,middle,getWidth());
        }else{
            //当前朝向  从右到左
            drawText(canvas,cmChangeTextPaint,getWidth() - middle,getWidth());
            drawText(canvas,cmOriginTextPaint,0,getWidth()-middle);
        }

    }

    /**
     * 绘制TextView
     * @param canvas
     * @param textPaint
     * @param start
     * @param end
     */
    private void drawText(Canvas canvas, Paint textPaint, int start, int end) {

        //保存画布状态
        canvas.save();

        Rect rect = new Rect(start,0,end,getHeight());
        canvas.clipRect(rect);

        //获取文字
        String text = getText().toString();

        Rect bounds = new Rect();
        textPaint.getTextBounds(text,0,text.length(),bounds);

        //获取字体的宽度
        int x = getWidth()/2 - bounds.width()/2;
        //获取基线
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        int baseLine = getHeight()/2 + dy;

        canvas.drawText(text,x,baseLine,textPaint);

        //释放画布状态，既恢复Canvas旋转，缩放等之后的状态。
        canvas.restore();
    }

    /**
     * 根据自定义的颜色创建画笔
     * @param textColor
     * @return
     */
    private Paint getTextPaintByColor(int textColor) {

        //创建画笔
        Paint paint = new Paint();
        //设置画笔颜色
        paint.setColor(textColor);
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置防抖动
        paint.setDither(true);
        //设置字体大小
        paint.setTextSize(getTextSize());

        return paint;

    }

    /**
     * 设置方向
     * @param directory
     */
    public synchronized  void setDirectory(Directory directory){
        this.cmCurrentDirectory = directory;
    }

    /**
     * 设置进度
     * @param currentProgress
     */
    public synchronized  void setCurrentProgress(float currentProgress){
        this.cmCurrentProgress = currentProgress;
        invalidate();
    }

    /**
     * 设置画笔颜色
     * @param changeColor
     */
    public synchronized void setChangeColor(int changeColor){
        this.cmChangeTextPaint.setColor(changeColor);
    }

    public synchronized void setOriginColor(int originColor){
        this.cmOriginTextPaint.setColor(originColor);
    }
}
