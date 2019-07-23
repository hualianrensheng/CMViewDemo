package com.test.cmviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义RdtingBar
 * Created by muchen on 2018/1/24.
 */

public class CMRatingBarView extends View {


    private Bitmap cmRatingNormalBitmap, cmRatingFocusBitmap;
    //个数
    private int cmRatingGradeNumber = 5;

    private int cmRatingCurrentNumber = 0;


    public CMRatingBarView(Context context) {
        this(context, null);
    }

    public CMRatingBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CMRatingBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CMRatingBarView);

        cmRatingGradeNumber = (int) typedArray.getInt(R.styleable.CMRatingBarView_cmRatingBarGradeNumber, cmRatingGradeNumber);

        int cmRatingNormalId = typedArray.getResourceId(R.styleable.CMRatingBarView_cmRatingBarStartNormal, 0);
        int cmRatingFocusId = typedArray.getResourceId(R.styleable.CMRatingBarView_cmRatingBarStartFocus, 0);
        if (0 == cmRatingNormalId) {
            throw new RuntimeException("请设置默认RatingBar图片");
        }
        if (0 == cmRatingFocusId) {
            throw new RuntimeException("请设置选中RatingBar图片");
        }
        cmRatingNormalBitmap = BitmapFactory.decodeResource(getResources(), cmRatingNormalId);
        cmRatingFocusBitmap = BitmapFactory.decodeResource(getResources(), cmRatingFocusId);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heigth = MeasureSpec.getSize(heightMeasureSpec);

        width = getPaddingLeft() + (cmRatingNormalBitmap.getWidth() + getPaddingRight()) * cmRatingGradeNumber;
        heigth = cmRatingNormalBitmap.getHeight() + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(width, heigth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //直接绘制bitmap

        for (int i = 0; i < cmRatingGradeNumber; i++) {
            int x = (cmRatingNormalBitmap.getWidth() + getPaddingRight()) * i;

            if(i < cmRatingCurrentNumber){
                canvas.drawBitmap(cmRatingFocusBitmap, getPaddingLeft() + x, getPaddingTop(), null);
            }else{
                canvas.drawBitmap(cmRatingNormalBitmap, getPaddingLeft() + x, getPaddingTop(), null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:

                //1.获取手指当前的位置，除以整个的宽度，然后再去画RatingBar
                float cmCurrentWidth = event.getX();
                int cmCurrentNumber = (int) (cmCurrentWidth / cmRatingNormalBitmap.getWidth() + 1);

                Log.e("CMRatingBar 个数",cmCurrentNumber+"个，event.getX（）= "+event.getX()+" ! cmRatingNormalBitmap.getWidth() + 1 = "+cmRatingNormalBitmap.getWidth() + 1);

                if(cmCurrentNumber < 0){
                    cmCurrentNumber = 0;
                }

                if(cmCurrentNumber > cmRatingGradeNumber){
                    cmCurrentNumber= cmRatingGradeNumber;
                }

                if(cmCurrentNumber == cmRatingCurrentNumber){
                    return true;
                }



                cmRatingCurrentNumber = cmCurrentNumber;
                invalidate();

                break;
        }

        return true;
    }
}
