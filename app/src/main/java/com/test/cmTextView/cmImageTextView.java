package com.test.cmTextView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.test.cmUtils.CMBitMapUtils;
import com.test.cmUtils.DpToPxUtil;
import com.test.cmviewdemo.R;

public class cmImageTextView extends View {

    //图像宽度
    private static final float IMAGE_WIDTH = DpToPxUtil.dp2px(120);

    private static final float IMAGE_OFFSET = DpToPxUtil.dp2px(50);
    private static final float IMAGE_LEFT = DpToPxUtil.dp2px(45);

    //创建画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //图像
    private Bitmap mBitmap;

    Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();

//    private String text = "Android is fortunate to have an incredibly rich ecosystem of SDKs and libraries to help developers build great apps more efficiently. These SDKs can range from developer tools that simplify complicated feature development to end-to-end services such as analytics, attribution, engagement, etc. All of these tools can help Android developers reduce cost and ship products faster. For the past few months, various teams at Google have been working together on new initiatives to expand the resources and support we offer for the developers of these tools. Today, SDK developers can sign up and register their SDK with us to receive updates that will keep you informed about Google Play policy changes, updates to the platform, and other useful information. Our goal is to provide you with whatever you need to better serve your technical and business goals in helping your partners create better apps or games. Going forward we will be sharing further resources to help SDK developers, so stay tuned for more updates. If you develop an SDK or library for Android, make sure you sign up and register your SDK to receive updates about the latest tools and information to help serve customers better. And, if you're an app developer, share this blogpost with the developers of the SDKs that you use!";

    private String text = "中华五千年的历史长河中，出现了数不尽的千古风流人物，他们的事迹影响了一代又一代中华儿女。他们的名字如星光般灿烂，照耀着我们前进的步伐，激励着我们努力奋进。他们是华夏的骄傲，是中华好儿郎！中华五千年的历史长河中，出现了数不尽的千古风流人物，他们的事迹影响了一代又一代中华儿女。他们的名字如星光般灿烂，照耀着我们前进的步伐，激励着我们努力奋进。他们是华夏的骄傲，是中华好儿郎！中华五千年的历史长河中，出现了数不尽的千古风流人物，他们的事迹影响了一代又一代中华儿女。他们的名字如星光般灿烂，照耀着我们前进的步伐，激励着我们努力奋进。他们是华夏的骄傲，是中华好儿郎！";
    //文字长度
    private int length;
    private float verticalOffset;
    private float textTop;
    private float textBottom;
    private int count;
    private int countLine;
    private int maxWidth;
    private float[] cutWidth = new float[1];

    {
        mBitmap = CMBitMapUtils.getAvatarWithImage(getResources(),(int) IMAGE_WIDTH, R.drawable.sun_photo);
        mPaint.setTextSize(DpToPxUtil.dp2px(15));
        mPaint.getFontMetrics(mFontMetrics);
        length = text.length();
    }

    public cmImageTextView(Context context) {
        super(context);
    }

    public cmImageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public cmImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制图像
        canvas.drawBitmap(mBitmap,getWidth() - IMAGE_WIDTH - IMAGE_LEFT,IMAGE_OFFSET,mPaint);
        //绘制TextView
        verticalOffset = -mFontMetrics.top;
        for (int start = 0; start < length;){
            textTop = mFontMetrics.top + verticalOffset;
            textBottom = mFontMetrics.bottom + verticalOffset;

            if(textTop > IMAGE_OFFSET && textTop < (IMAGE_WIDTH + IMAGE_OFFSET) || textBottom > IMAGE_OFFSET && textBottom < (IMAGE_OFFSET + IMAGE_WIDTH)){
                //图文在同一行
                maxWidth = (int)(getWidth() - IMAGE_WIDTH);
                count = mPaint.breakText(text,start,length,true,maxWidth,cutWidth);
                //如果图片不是在图片最右边
                if(IMAGE_LEFT > 0){
                    countLine = mPaint.breakText(text,start,length,true,maxWidth - IMAGE_LEFT,cutWidth);
                    canvas.drawText(text,start,start + countLine,0,verticalOffset,mPaint);
                    canvas.drawText(text,start + countLine , start + count,maxWidth - IMAGE_LEFT + IMAGE_WIDTH,verticalOffset,mPaint);
                }else{
                    canvas.drawText(text,start,start + count,0,verticalOffset,mPaint);
                }

            }else{
                //不在同一行
                maxWidth = getWidth();
                count = mPaint.breakText(text,start,length,true,maxWidth,cutWidth);
                canvas.drawText(text,start,start + count,0,verticalOffset,mPaint);
            }
            start += count;
            verticalOffset += mPaint.getFontSpacing();
        }
    }
}
