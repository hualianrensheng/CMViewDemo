package com.test.cmMaiMaiImage;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.test.cmUtils.CMBitMapUtils;
import com.test.cmUtils.DpToPxUtil;
import com.test.cmviewdemo.R;

public class CMMaiMaiImageView extends View {


    private static final float IMAGE_WIDTH = DpToPxUtil.dp2px(1200);

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mBitmap;


    private float originOffsetX;
    private float originOffsetY;

    //偏移
    private float offsetX;
    private float offsetY;

    private float smallScalab;
    private float bigScalab;


    //判断当前图片展示的是大图还是小图
    private boolean isBig;

    //图片放大缩小的动画
    private float scaleFraction;

    //图片放大缩小的动画
    private ObjectAnimator mObjectAnimator;

    public ObjectAnimator getObjectAnimator() {
        if(mObjectAnimator == null){
            mObjectAnimator = ObjectAnimator.ofFloat(this,"scaleFraction",0,1);
        }
        return mObjectAnimator;
    }

    public float getScaleFraction() {
        return scaleFraction;
    }

    public void setScaleFraction(float scaleFraction) {
        this.scaleFraction = scaleFraction;
        invalidate();
    }

    CMGestureDetectorListener mCMGestureDetector = new CMGestureDetectorListener();
    GestureDetectorCompat mGestureDetectorCompat;


    public CMMaiMaiImageView(Context context) {
        this(context,null);
    }

    public CMMaiMaiImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMMaiMaiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //添加绘制图片
        mBitmap = CMBitMapUtils.getAvatarWithImage(getResources(),(int) IMAGE_WIDTH, R.drawable.kenan);
        mGestureDetectorCompat = new GestureDetectorCompat(context,mCMGestureDetector);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        originOffsetX = ((float)(getWidth() - mBitmap.getWidth())) / 2f;
        originOffsetY = ((float)(getHeight() - mBitmap.getHeight())) / 2f;

        if( ((float)mBitmap.getWidth()/mBitmap.getHeight()) > ((float) mBitmap.getHeight()/mBitmap.getWidth())){
            //图片宽度比高度大，横向图片
            smallScalab = (float) getWidth()/mBitmap.getWidth();
            bigScalab = (float) getHeight()/mBitmap.getHeight();
        }else{
            //图片宽度比高度小，纵向图片
            smallScalab = (float) getHeight()/mBitmap.getHeight();
            bigScalab = (float) getWidth()/mBitmap.getWidth();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(offsetX,offsetY);
        float scale = smallScalab + (bigScalab - smallScalab)*scaleFraction;
        canvas.scale(scale,scale,getWidth()/2f,getHeight()/2f);
        canvas.drawBitmap(mBitmap,originOffsetX,originOffsetY,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetectorCompat.onTouchEvent(event);
    }


    class CMGestureDetectorListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            Log.e("====","onDow=");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.e("====","onShowPress=");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("====","onSingleTapUp=");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e("====","onScroll=");
            if(isBig){
                offsetX -= distanceX;
                offsetY -= distanceY;

                offsetX = Math.min(offsetX,(mBitmap.getWidth()*bigScalab - getWidth())/2);
                offsetX = Math.max(offsetX,-(mBitmap.getWidth()*bigScalab - getWidth())/2);

                offsetY = Math.min(offsetY,(mBitmap.getHeight()*bigScalab - getHeight())/2);
                offsetY = Math.max(offsetY,-(mBitmap.getHeight()*bigScalab - getHeight())/2);

                invalidate();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.e("====","onLongPress=");

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("====","onFling=");
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e("====","onSingleTapConfirmed=");
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e("====","onDoubleTap=");
            isBig = !isBig;
            if(isBig){
                getObjectAnimator().start();
            }else{
                getObjectAnimator().reverse();
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.e("====","onDoubleTapEvent=" + isBig);
            return false;
        }

    }
}
