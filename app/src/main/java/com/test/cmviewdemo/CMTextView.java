package com.test.cmviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.support.v7.widget.AppCompatTextView;


/**
 * Created by CMMY on 2018/1/19.
 */

public class CMTextView extends AppCompatTextView {


    private int mCmTextColor;
    private String mCmText;
    private int mCmTextSiZe = 15;

    private Paint cmPaint;

    public CMTextView(Context context) {
        this(context,null);
    }

    public CMTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //1.获取自定义的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CMTextView);
        mCmText = typedArray.getString(R.styleable.CMTextView_cmText);
        mCmTextColor = typedArray.getColor(R.styleable.CMTextView_cmTextColor,mCmTextColor);
        mCmTextSiZe = typedArray.getDimensionPixelOffset(R.styleable.CMTextView_cmTextSize,spToPx(mCmTextSiZe));
        //回收
        typedArray.recycle();

        cmPaint = new Paint();
        cmPaint.setAntiAlias(true);
        cmPaint.setColor(mCmTextColor);
        cmPaint.setTextSize(mCmTextSiZe);
    }
    /**
     * sp 与 px 转换
     * @param sp
     * @return
     */
    private int spToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    /**
     * View 的 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //1. 获取 自定义 View 的宽度，高度 的模式
        int heigthMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if(MeasureSpec.AT_MOST == heigthMode){
            Rect bounds = new Rect();
            cmPaint.getTextBounds(mCmText,0,mCmText.length(),bounds);
            height = bounds.height() + getPaddingBottom() + getPaddingTop();
            width = bounds.width() + getPaddingLeft() + getPaddingRight();
        }

        if(MeasureSpec.AT_MOST == widthMode){
            Rect bounds = new Rect();
            cmPaint.getTextBounds(mCmText,0,mCmText.length(),bounds);
            height = bounds.height() + getPaddingBottom() + getPaddingTop();
            width = bounds.width() + getPaddingLeft() + getPaddingRight();
        }

        setMeasuredDimension(width,height);

    }

    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算基线
        Paint.FontMetricsInt fontMetricsInt = cmPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        int baseLine = getHeight()/2 + dy;
        int x = getPaddingLeft();
        // x: 开始的位置  y：基线
        canvas.drawText(mCmText,x,baseLine,cmPaint);
    }

}
