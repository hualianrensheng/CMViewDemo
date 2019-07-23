package com.test.cmQQSlidingMenuView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.test.cmviewdemo.R;

/**
 * Created by muchen on 2018/3/1.
 */

public class CMQQSlidingMenuView extends HorizontalScrollView {

    //菜单view
    private View mMenuView;

    //菜单默认的宽度
    private int mMenuWidth;

    //7 手指快速滑动 判断菜单是否关闭
    private boolean mMenuisOpen = false;

    //7 手势处理类
    private GestureDetector mGestureDetector;

    //9.1.2.2 把阴影添加到新的内容容器 阴影view
    private ImageView mShadowView;


    public CMQQSlidingMenuView(Context context) {
        this(context,null);
    }

    public CMQQSlidingMenuView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMQQSlidingMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CMQQSlidingMenuView);
        float rightPadding = array.getDimension(R.styleable.CMSlidingMenuView_cmSlidingMenuRightMargin,dip2px(50));
        // 4.2.1 指定菜单的宽度
        mMenuWidth = (int) (getScreenWidth() - rightPadding);
        array.recycle();

        //初始化手势处理类
        mGestureDetector = new GestureDetector(getContext(),new GestureListener());
    }

    /**
     * @info 整个布局加载完毕后调用的方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //4.动态的获取菜单View，获取内容View
        //4.1 获取菜单view和内容view
        //4.1.1 获取根布局view
        ViewGroup container = (ViewGroup) getChildAt(0);
        //4.1.2获取菜单View
        mMenuView = container.getChildAt(0);

        //4.1.3获取内容view
        // 9.处理内容的阴影效果
        // 9.1 思路在内容布局的外面加一层阴影  ImageView
        // 9.1.1 把原来的内容从根布局里面移除
        View oldContentView = container.getChildAt(1);
        container.removeView(oldContentView);
        //9.1.2 新建布局 = 原来布局 + 阴影
        FrameLayout newContentView = new FrameLayout(getContext());
        //9.1.2.1 把原来的内容添加到内容容器中
        newContentView.addView(oldContentView);
        //9.1.2.2 把阴影添加到新的内容容器
        mShadowView = new ImageView(getContext());
        mShadowView.setBackgroundColor(Color.parseColor("#99000000"));
        newContentView.addView(mShadowView);
        //9.1.3 把新的容器放回原来的位置
        container.addView(newContentView);

        //4.2 指定菜单和内容view的宽度
        mMenuView.getLayoutParams().width = mMenuWidth;
        //4.2.2 指定内容的宽度为屏幕的宽度
        newContentView.getLayoutParams().width = getScreenWidth();

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(mGestureDetector.onTouchEvent(ev)){
            return false;
        }

        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                //手指抬起
                int currentScrollX = getScrollX();
                if(currentScrollX > mMenuWidth/2){
                    closeMenu();
                }else{
                    openMenu();
                }
                return false;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * @info 切换菜单状态
     */
    private void toggleMenu(){
        if(mMenuisOpen){
            closeMenu();
        }else{
            openMenu();
        }
    }

    /**
     * @info 关闭菜单
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth,0);
        mMenuisOpen = false;
    }

    /**
     * @info 打开菜单
     */
    private void openMenu() {
        smoothScrollTo(0,0);
        mMenuisOpen = true;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //4.3 每当进入菜单页面，默认，菜单页面是关闭的
        if(changed){
            //4.3 默认关闭的状态，要让他自己滚动一段距离 默认是关闭状态
            scrollTo(mMenuWidth,0);
        }
    }

    /**
     * @info 处理抽屉效果
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //滚动回调的方法，这个方法是不断执行的
        //l 代表当前滚动的距离
        //8.处理菜单的抽屉效果 让菜单移动一段距离
        mMenuView.setTranslationX(l*0.8f);

        //9.2 滑动到不同的位置改变其阴影透明度
        float scale = l*1f/mMenuWidth;
        float alphaScale = 1 - scale;
        mShadowView.setAlpha(alphaScale);
    }

    private class  GestureListener extends GestureDetector.SimpleOnGestureListener{
        //重写快速滑动
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(Math.abs(velocityY) > Math.abs(getPivotX())){
                //此时代表上下滑动，不做处理
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            if(mMenuisOpen){
                if(velocityX < 0){
                    toggleMenu();
                    return true;
                }
            }else{
                if(velocityX > 0){
                    toggleMenu();
                    return true;
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    /**
     * @info dip 赚 px
     * @param dip
     * @return
     */
    private int dip2px(int dip){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }

    /**
     * @info 获取屏幕的宽度
     * @return 返回屏幕的宽度
     */
    private int getScreenWidth(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }


}
