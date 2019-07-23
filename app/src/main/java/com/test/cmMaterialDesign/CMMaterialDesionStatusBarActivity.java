package com.test.cmMaterialDesign;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.test.cmviewdemo.R;

public class CMMaterialDesionStatusBarActivity extends AppCompatActivity {

    private View mTitleBar;
    private ScrollView mScrollView;
    private ImageView mImageView;
    //图片的高度
    private int mImageViewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmmaterial_desion_status_bar);

        CMMaterialDesignStatusBarUtil.setStatusBarTranslucent(this);

//        CMMaterialDesignStatusBarUtil.setStatusBarColor(this,Color.TRANSPARENT);

        initView();

        setTitleBarBackground();

    }


    /**
     * 设置控件
     */
    private void initView() {
        mTitleBar = findViewById(R.id.title_bar);
        mScrollView = (ScrollView)findViewById(R.id.title_scrollview);
        mImageView = (ImageView)findViewById(R.id.title_imageview);

        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mImageViewHeight = mImageView.getMeasuredHeight() - mTitleBar.getMeasuredHeight() ;
            }
        });
    }


    /**
     * 给抬头设置渐变的颜色
     *
     */
    private void setTitleBarBackground() {

        mTitleBar.getBackground().setAlpha(0);

        /*
        * 1.我们要不断的监听他的滚动的位置
        * 2.根据监听位置的变换，来设置抬头的显示的透明度
        * */
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                /**
                 * 1.获取图片的高度
                 * 2.根据当前滚动的位置，计算alpha的值
                 * 注意：需要先判断当前获取的图片的高度是否为空
                 */
                if(mImageViewHeight == 0)return;

                float alpha = (float) scrollY/mImageViewHeight;

                Log.e("====mImageViewHeight===",mImageViewHeight+"");
                Log.e("====scrollY============",scrollY+"");
                Log.e("====alpha============",alpha+"");

                if(alpha <= 0){
                    alpha = 0;
                }

                if(alpha > 1){
                    alpha = 1;
                }

                mTitleBar.getBackground().setAlpha((int) (alpha*255));

            }
        });
    }
}
