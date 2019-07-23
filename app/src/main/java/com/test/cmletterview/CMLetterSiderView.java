package com.test.cmletterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.test.cmviewdemo.R;

/**
 * Created by muchen on 2018/1/24.
 */

public class CMLetterSiderView extends View {

    //定义26个字母
    private static String[] cmLetterSiderViewLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

    private Paint cmLetterNormalPaint,cmLetterChoosePaint;

    private int cmLetterChooseTextSize;
    private int cmLetterChooseTextColor;

    private int cmLetterNormalTextSize;
    private int cmLetterNormalTextColor;

    private String cmLetterCurrent;


    public CMLetterSiderView(Context context) {
        this(context,null);
    }

    public CMLetterSiderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMLetterSiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //创建画笔
        initPaint(context,attrs);
    }

    /**
     * 创建画笔
     * 1.获取自定义属性
     * 2.创建画笔
     */
    private void initPaint(Context context, @Nullable AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CMLetterSiderView);

        cmLetterNormalTextColor = typedArray.getColor(R.styleable.CMLetterSiderView_cmLetterSiderNormalTextColor,cmLetterNormalTextColor);
        cmLetterNormalTextSize = (int) typedArray.getDimension(R.styleable.CMLetterSiderView_cmLetterSiderNormalTextSize,cmLetterNormalTextSize);

        cmLetterChooseTextColor = typedArray.getColor(R.styleable.CMLetterSiderView_cmLetterSiderChooseTextColor, cmLetterChooseTextColor);
        cmLetterChooseTextSize = (int) typedArray.getDimension(R.styleable.CMLetterSiderView_cmLetterSiderChooseTextSize,cmLetterChooseTextSize);

        typedArray.recycle();

        cmLetterNormalPaint = new Paint();
        cmLetterNormalPaint.setAntiAlias(true);
        cmLetterNormalPaint.setColor(cmLetterNormalTextColor);
        cmLetterNormalPaint.setTextSize(cmLetterNormalTextSize);

        cmLetterChoosePaint = new Paint();
        cmLetterChoosePaint.setAntiAlias(true);
        cmLetterChoosePaint.setColor(cmLetterChooseTextColor);
        cmLetterChoosePaint.setTextSize(cmLetterChooseTextSize);

    }

    /**
     * 重新测量宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int textWidth = (int) cmLetterNormalPaint.measureText("A");// A字母的宽度

        width = getPaddingLeft()+getPaddingLeft()+textWidth;
        height = height + 1*cmLetterSiderViewLetters.length;

        //重新设置宽高
        setMeasuredDimension(width,height);
    }

    /**
     * 重新绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //重新绘制

        //获取单个字母条目的高度
        int itemsHeight = (getHeight() - getPaddingBottom() - getPaddingTop())/cmLetterSiderViewLetters.length;
        //for循环画出26个字母
        for (int i = 0; i < cmLetterSiderViewLetters.length; i++){

            float y = (getHeight() - getPaddingBottom() - getPaddingTop())/cmLetterSiderViewLetters.length;

            int letterCenterY = (int) (y*i + y/2 + getPaddingTop());

            Paint.FontMetrics fontMetrics = cmLetterNormalPaint.getFontMetrics();
            int dy = (int) ((fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom);
            int baseLine = letterCenterY + dy;

            if(cmLetterSiderViewLetters[i].equals(cmLetterCurrent)){

                float x = getWidth()/2 - cmLetterChoosePaint.measureText(cmLetterSiderViewLetters[i])/2;

                canvas.drawText(cmLetterSiderViewLetters[i],x,baseLine,cmLetterChoosePaint);
            }else{

                float x = getWidth()/2 - cmLetterNormalPaint.measureText(cmLetterSiderViewLetters[i])/2;

                canvas.drawText(cmLetterSiderViewLetters[i],x,baseLine,cmLetterNormalPaint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                //获取当前手指的位置（高度），用当前手指的高度除以单个字母的高度，得出手指当前所处于哪个字母上
                float currentMoveY = event.getY();
                float itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom())/cmLetterSiderViewLetters.length;
                int currentPosition = (int) (currentMoveY/itemHeight);

                if(0 > currentPosition){
                    currentPosition = 0;
                }

                if((cmLetterSiderViewLetters.length - 1) < currentPosition){
                    currentPosition = cmLetterSiderViewLetters.length - 1;
                }
                cmLetterCurrent = cmLetterSiderViewLetters[currentPosition];
                if(mCMLetterSiderTouchListener != null){
                    mCMLetterSiderTouchListener.cmLetterSiderTouchListener(cmLetterCurrent,true);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(mCMLetterSiderTouchListener != null){
                    mCMLetterSiderTouchListener.cmLetterSiderTouchListener(cmLetterCurrent,false);
                }
                break;
        }
        return true;
    }

    //提供回掉方法
    private CMLetterSiderTouchListener mCMLetterSiderTouchListener;
    public void setOnCMLetterSiderTouchListener(CMLetterSiderTouchListener mCMLetterSiderTouchListener){
        this.mCMLetterSiderTouchListener = mCMLetterSiderTouchListener;
    }

    public interface CMLetterSiderTouchListener{
        void cmLetterSiderTouchListener(CharSequence letter,boolean isTouch);
    }
}
