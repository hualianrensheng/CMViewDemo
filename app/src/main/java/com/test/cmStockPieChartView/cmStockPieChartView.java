package com.test.cmStockPieChartView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.test.cmUtils.DpToPxUtil;

public class cmStockPieChartView extends View {

    //画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //扇形半径
    private static final float RADIOUS = DpToPxUtil.dp2px(100);
    //扇形所在的矩形区域
    private RectF mRectF = new RectF();
    //起始角度值
    private int currentAngle = 270;
    //每块扇形所对应的角度
    private int[] angles = {90,90,120,60};
    //每块扇形所对应的颜色
    private int[] colors = {Color.parseColor("#ea0200"),Color.parseColor("#f96362"),Color.parseColor("#85c263"),Color.parseColor("#5e9e3e")};


    //扇形的偏移
    private static final float PULLED_OUT_INDEX = DpToPxUtil.dp2px(3);
    private static final float IMAGINARY_LINE_WIDTH = DpToPxUtil.dp2px(2);
    //偏移的扇形所在的矩形
    private RectF mRectF_Remove = new RectF();
    //绘制虚线的DashPathEffect
    private DashPathEffect mDashPathEffect;
    //绘制虚线的Path
    private Path mPath = new Path();
    {
        mDashPathEffect = new DashPathEffect(new float[]{IMAGINARY_LINE_WIDTH, IMAGINARY_LINE_WIDTH}, 10f);
    }

    //绘制圆弧的Rect
    private RectF mRectF_Arc = new RectF();
    //圆弧的偏移
    private static final float ARC_OUT_INDEX = DpToPxUtil.dp2px(12);
    //圆弧的path
    private Path mPath_Arc = new Path();

    public cmStockPieChartView(Context context) {
        super(context);
    }

    public cmStockPieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public cmStockPieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(getWidth()/2 - RADIOUS,getHeight()/2 - RADIOUS,getWidth()/2 + RADIOUS,getHeight()/2 + RADIOUS);
        mRectF_Remove.set(getWidth()/2 - RADIOUS - PULLED_OUT_INDEX,getHeight()/2 - RADIOUS - PULLED_OUT_INDEX,getWidth()/2 + RADIOUS + PULLED_OUT_INDEX,getHeight()/2 + RADIOUS + PULLED_OUT_INDEX);
        mRectF_Arc.set(getWidth()/2 - RADIOUS - ARC_OUT_INDEX,getHeight()/2 - RADIOUS - ARC_OUT_INDEX,getWidth()/2 + RADIOUS + ARC_OUT_INDEX,getHeight()/2 + RADIOUS + ARC_OUT_INDEX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制扇形
        mPaint.reset();
        for (int i = 0;i < angles.length; i++){
            mPaint.setColor(colors[i]);
            canvas.save();
            //平移绿色，红色扇形
            if(i == 0 || i == 3){
                canvas.translate(0, -PULLED_OUT_INDEX);
                //绘制绿色与红色扇形
                canvas.drawArc(mRectF_Remove,currentAngle,angles[i],true,mPaint);
            }else{
                //绘制扇形
                canvas.drawArc(mRectF,currentAngle,angles[i],true,mPaint);
            }
            canvas.restore();
            currentAngle += angles[i];
        }


        //绘制虚线
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DpToPxUtil.dp2px(1));
        mPaint.setPathEffect(mDashPathEffect);
        mPaint.setColor(Color.WHITE);
        //红色扇形上的虚线
        mPath.moveTo(getWidth()/2 ,getHeight()/2 - PULLED_OUT_INDEX);
        mPath.lineTo((float) getWidth()/2 + (float) Math.sin(Math.toRadians(angles[0]/2))*(RADIOUS + PULLED_OUT_INDEX),(float) (getHeight()/2 - PULLED_OUT_INDEX) - (float) Math.cos(Math.toRadians(angles[0]/2))*(RADIOUS + PULLED_OUT_INDEX));
        //绿色扇形上的虚线
        mPath.moveTo(getWidth()/2 ,getHeight()/2 - PULLED_OUT_INDEX);
        mPath.lineTo((float) getWidth()/2 - (float) Math.sin(Math.toRadians(angles[3]/2))*(RADIOUS + PULLED_OUT_INDEX),(float) (getHeight()/2 - PULLED_OUT_INDEX) - (float) Math.cos(Math.toRadians(angles[3]/2))*(RADIOUS + PULLED_OUT_INDEX));
        //绘制
        canvas.drawPath(mPath,mPaint);
        mPaint.setPathEffect(null);

        //绘制弧形
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DpToPxUtil.dp2px(1));
        mPaint.setColor(colors[0]);
        canvas.drawArc(mRectF_Arc,currentAngle + 1,angles[0]/2 - 2,false,mPaint);

        mPaint.setColor(colors[3]);
        canvas.drawArc(mRectF_Arc,currentAngle - angles[3]/2,angles[3]/2 - 1,false,mPaint);

        //绘制path
        mPath_Arc.moveTo((float) getWidth()/2 - (float) Math.sin(Math.toRadians(angles[3]/2/2))*(RADIOUS + ARC_OUT_INDEX),(float) (getHeight()/2) - (float) Math.cos(Math.toRadians(angles[3]/2/2))*(RADIOUS + ARC_OUT_INDEX));
        mPath_Arc.lineTo((float) getWidth()/2 - (float) Math.sin(Math.toRadians(angles[3]/3))*(RADIOUS + ARC_OUT_INDEX*4),(float) (getHeight()/2) - (float) Math.cos(Math.toRadians(angles[3]/3))*(RADIOUS + ARC_OUT_INDEX*4));
        mPath_Arc.lineTo((float) getWidth()/2 - (float) Math.sin(Math.toRadians(angles[3]/3))*(RADIOUS + ARC_OUT_INDEX*4) - DpToPxUtil.dp2px(50),(float) (getHeight()/2) - (float) Math.cos(Math.toRadians(angles[3]/3))*(RADIOUS + ARC_OUT_INDEX*4));
        canvas.drawPath(mPath_Arc,mPaint);

        mPath_Arc.rewind();
        mPaint.setColor(colors[0]);
        mPath_Arc.moveTo((float) getWidth()/2 + (float) Math.sin(Math.toRadians(angles[0]/2/2))*(RADIOUS + ARC_OUT_INDEX),(float) (getHeight()/2) - (float) Math.cos(Math.toRadians(angles[0]/2/2))*(RADIOUS + ARC_OUT_INDEX));
        mPath_Arc.lineTo((float) getWidth()/2 + (float) Math.sin(Math.toRadians(angles[0]/2/2))*(RADIOUS + ARC_OUT_INDEX) + DpToPxUtil.dp2px(80),(float) (getHeight()/2) - (float) Math.cos(Math.toRadians(angles[0]/2/2))*(RADIOUS + ARC_OUT_INDEX));
        mPath_Arc.lineTo((float) getWidth()/2 + (float) Math.sin(Math.toRadians(angles[0]/2/2))*(RADIOUS + ARC_OUT_INDEX) + DpToPxUtil.dp2px(100),(float) (getHeight()/2) - (float) Math.cos(Math.toRadians(angles[0]/2/2))*(RADIOUS + ARC_OUT_INDEX) + DpToPxUtil.dp2px(20));
        canvas.drawPath(mPath_Arc,mPaint);


    }
}
