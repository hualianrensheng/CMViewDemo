package com.test.cmScalableImage;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import com.test.cmUtils.CMBitMapUtils;
import com.test.cmUtils.DpToPxUtil;

public class CMScalableImageView extends View{

    private static final float IMAGE_WIDTH = DpToPxUtil.dp2px(300);
    private static final float OVER_SCALE_FACTOR = 1.5f;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap mBitmap;

    private OverScroller mOverScroller;

    //滑动偏移
    private float offsetX;
    private float offsetY;

    //旋转偏移量
    private float originOffsetX;
    private float originOffsetY;

    private float smallScalab;
    private float bigScalab;

    private float scale;

    private boolean isBig;

//    private float scaleFraction;
    private float currentFraction;
    private ObjectAnimator scaleAnimator;

    public ObjectAnimator getScaleAnimator() {
        if(scaleAnimator == null){
            scaleAnimator = ObjectAnimator.ofFloat(this,"currentFraction",0,1);
        }
        scaleAnimator.setFloatValues(smallScalab,bigScalab);
        return scaleAnimator;
    }

    public float getCurrentFraction() {
        return currentFraction;
    }

    public void setCurrentFraction(float currentFraction) {
        this.currentFraction = currentFraction;
        invalidate();
    }

    GestureDetectorCompat mGestureDetectorCompat;
    ScaleGestureDetector mScaleGestureDetector;

    CMGestureDetectorListener mCMGestureDetectorListener = new CMGestureDetectorListener();
    CMFlingRunner mCMFlingRunner = new CMFlingRunner();
    CMScaleLisenter mCMScaleLisenter = new CMScaleLisenter();

    public CMScalableImageView(Context context) {
        this(context,null);
    }

    public CMScalableImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMScalableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBitmap = CMBitMapUtils.getAvatar(getResources(),(int) IMAGE_WIDTH);
        mGestureDetectorCompat = new GestureDetectorCompat(context,mCMGestureDetectorListener);
        mOverScroller = new OverScroller(context);
        mScaleGestureDetector = new ScaleGestureDetector(context,mCMScaleLisenter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        originOffsetX = ((float) (getWidth() - mBitmap.getWidth())) / 2;
        originOffsetY = ((float) (getHeight() - mBitmap.getHeight())) / 2;

        if( (float)(mBitmap.getWidth()/mBitmap.getHeight()) > (float)(getWidth()/getHeight())){
            smallScalab = (float) getWidth() / mBitmap.getWidth();
            bigScalab = (float) getHeight() / mBitmap.getHeight() * OVER_SCALE_FACTOR;
        }else{
            bigScalab = (float) getWidth() / mBitmap.getWidth();
            smallScalab = (float) getHeight() / mBitmap.getHeight() * OVER_SCALE_FACTOR;
        }

        currentFraction = smallScalab;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float scaleFraction = (currentFraction - smallScalab) / (bigScalab - smallScalab);
        canvas.translate(offsetX * scaleFraction,offsetY * scaleFraction);
//        scale = smallScalab + (bigScalab - smallScalab) * scaleFraction;
        canvas.scale(currentFraction,currentFraction,getWidth()/2.0f,getHeight()/2.0f);
        canvas.drawBitmap(mBitmap, originOffsetX, originOffsetY,mPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        boolean result = mScaleGestureDetector.onTouchEvent(event);
        if(!mScaleGestureDetector.isInProgress()){
            result = mGestureDetectorCompat.onTouchEvent(event);
        }
        return result;
    }


    private void fixOffsets() {
        offsetX = Math.min(offsetX,(mBitmap.getWidth()*bigScalab - getWidth())/2);
        offsetX = Math.max(offsetX, -(mBitmap.getWidth()*bigScalab - getWidth())/2);
        offsetY = Math.min(offsetY,(mBitmap.getHeight()*bigScalab - getHeight())/2);
        offsetY = Math.max(offsetY, -(mBitmap.getHeight()*bigScalab - getHeight())/2);
    }

    class CMGestureDetectorListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(isBig){
                offsetX -= distanceX;
                offsetY -= distanceY;

                fixOffsets();

                invalidate();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(isBig){
                mOverScroller.fling((int) offsetX,(int)offsetY,(int)velocityX,(int)velocityY,
                        -(int)(mBitmap.getWidth()*bigScalab - getWidth())/2,
                        (int)(mBitmap.getWidth()*bigScalab - getWidth())/2,
                        -(int)(mBitmap.getHeight()*bigScalab - getHeight())/2,
                        (int)(mBitmap.getHeight()*bigScalab - getHeight())/2);

                postOnAnimation(mCMFlingRunner);
            }

            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            isBig = !isBig;
            if(isBig){

                //反向偏移
                offsetX = (e.getX() - getWidth()/2f) - (e.getX() - getWidth()/2f) * bigScalab / smallScalab;
                offsetY = (e.getY() - getHeight()/2f) - (e.getY() - getHeight()/2f) * bigScalab /smallScalab;

                getScaleAnimator().start();

                fixOffsets();

            }else{
                getScaleAnimator().reverse();
            }

            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }

    class CMScaleLisenter implements ScaleGestureDetector.OnScaleGestureListener{
        float initiaScale;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            currentFraction = initiaScale * detector.getScaleFactor();
            invalidate();
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initiaScale = currentFraction;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }


    class CMFlingRunner implements Runnable{
        @Override
        public void run() {
            if(mOverScroller.computeScrollOffset()){
                offsetX = mOverScroller.getCurrX();
                offsetY = mOverScroller.getCurrY();
                invalidate();
                postOnAnimation(this);
            }
        }
    }
}
