package com.test.cmDashView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.test.cmUtils.DpToPxUtil;

public class CMDashBoardView extends View {

    //角度
    private static final int ANGLE = 120;
    //半径
    private static final float RADIOUS = DpToPxUtil.dp2px(150);
    //刻度盘弧形宽度
    private static final float dashBoardWidth = DpToPxUtil.dp2px(2);
    //指针长度
    private static final float pointLength = DpToPxUtil.dp2px(100);

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path mPath = new Path();
    private Path mPathArc = new Path();

    private PathDashPathEffect mPathDashPathEffect;
    private PathMeasure mPathMeasure;

    {
        //设置画笔
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dashBoardWidth);

        //添加虚线刻度
        mPath.addRect(0,0,DpToPxUtil.dp2px(2),DpToPxUtil.dp2px(10),Path.Direction.CW);

        //测量弧形长度
        mPathArc.addArc(getWidth()/2 - RADIOUS,getHeight()/2 - RADIOUS,getWidth()/2 + RADIOUS,getHeight()/2 + RADIOUS,90 + ANGLE/2,360 - ANGLE);
        mPathMeasure = new PathMeasure(mPathArc,false);
        mPathDashPathEffect = new PathDashPathEffect(mPath,(mPathMeasure.getLength() - DpToPxUtil.dp2px(2))/20,0,PathDashPathEffect.Style.ROTATE);


    }

    public CMDashBoardView(Context context) {
        super(context);
    }

    public CMDashBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CMDashBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //1，画弧形
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dashBoardWidth);
        canvas.drawArc(getWidth()/2 - RADIOUS,getHeight()/2 - RADIOUS,getWidth()/2 + RADIOUS,getHeight()/2 + RADIOUS,90 + ANGLE/2,360 - ANGLE,false,mPaint);

        //2.画刻度
        mPaint.setPathEffect(mPathDashPathEffect);
        canvas.drawArc(getWidth()/2 - RADIOUS,getHeight()/2 - RADIOUS,getWidth()/2 + RADIOUS,getHeight()/2 + RADIOUS,90 + ANGLE/2,360 - ANGLE,false,mPaint);

        //3.画指针
        canvas.drawLine(
                getWidth()/2,
                getHeight()/2,
                getWidth()/2 + (float)Math.cos(Math.toRadians(getCurrentMark(14))) * pointLength,
                getHeight()/2 + (float)Math.sin(Math.toRadians(getCurrentMark(6))) * pointLength,
                mPaint);

    }

    //获取对应刻度的角度
    private int getCurrentMark(int mark){
        return (int)(90 + (float)ANGLE/2 + (360 - (float)ANGLE)/20 * mark);
    }
}
