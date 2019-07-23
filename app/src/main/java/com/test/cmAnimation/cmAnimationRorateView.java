package com.test.cmAnimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.test.cmUtils.CMBitMapUtils;
import com.test.cmUtils.DpToPxUtil;
import com.test.cmviewdemo.R;

public class cmAnimationRorateView extends View {
    private static final float PADDING = DpToPxUtil.dp2px(100);
    private static final float IMAGE_WIDTH = DpToPxUtil.dp2px(200);
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap mBitmap;

    private Camera mCamera = new Camera();

    {
        mBitmap = CMBitMapUtils.getAvatarWithImage(getResources(),(int) DpToPxUtil.dp2px(200),R.drawable.sun_photo);
        mCamera.setLocation(0, 0, DpToPxUtil.getZForCamera()); // -8 = -8 * 72
    }

    float topFlip = 0;
    float bottomFlip = 0;
    float flipRotation = 0;

    public float getTopFlip() {
        return topFlip;
    }

    public void setTopFlip(float topFlip) {
        this.topFlip = topFlip;
        invalidate();
    }

    public float getBottomFlip() {
        return bottomFlip;
    }

    public void setBottomFlip(float bottomFlip) {
        this.bottomFlip = bottomFlip;
        invalidate();
    }

    public float getFlipRotation() {
        return flipRotation;
    }

    public void setFlipRotation(float flipRotation) {
        this.flipRotation = flipRotation;
        invalidate();
    }

    public cmAnimationRorateView(Context context) {
        super(context);
    }

    public cmAnimationRorateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public cmAnimationRorateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate((PADDING + IMAGE_WIDTH/2),(PADDING + IMAGE_WIDTH/2));
        canvas.rotate(-flipRotation);
        mCamera.save();
        mCamera.rotateX(topFlip);
        mCamera.applyToCanvas(canvas);
        mCamera.restore();
        canvas.clipRect(-  IMAGE_WIDTH,- IMAGE_WIDTH,IMAGE_WIDTH,0);
        canvas.rotate(flipRotation);
        canvas.translate(-(PADDING + IMAGE_WIDTH/2),-(PADDING + IMAGE_WIDTH/2));
        canvas.drawBitmap(mBitmap,PADDING,PADDING,mPaint);
        canvas.restore();

        canvas.save();
        canvas.translate((PADDING + IMAGE_WIDTH/2),(PADDING + IMAGE_WIDTH/2));
        canvas.rotate(-flipRotation);
        mCamera.save();
        mCamera.rotateX(bottomFlip);
        mCamera.applyToCanvas(canvas);
        mCamera.restore();
        canvas.clipRect( - IMAGE_WIDTH,0,IMAGE_WIDTH,IMAGE_WIDTH);
        canvas.rotate(flipRotation);
        canvas.translate(-(PADDING + IMAGE_WIDTH/2),-(PADDING + IMAGE_WIDTH/2));
        canvas.drawBitmap(mBitmap,PADDING,PADDING,mPaint);
        canvas.restore();
    }
}
