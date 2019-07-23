package com.test.cmSlidingMenuView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import com.test.cmviewdemo.R;

/**
 * Created by muchen on 2018/2/28.
 */

public class CMSlidingMenuView extends HorizontalScrollView {

    //菜单的宽度
    private int mMenuWidth;
    //菜单view，主view
    private View mMenuView,mContentView;

    //定义手势快速滑动类
    private GestureDetector mGestureDetector;
    //定义 菜单是否打开
    private boolean mMenuIsOpen = false;


    public CMSlidingMenuView(Context context) {
        this(context,null);
    }

    public CMSlidingMenuView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMSlidingMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CMSlidingMenuView);
        float rightMargin = array.getDimension(R.styleable.CMSlidingMenuView_cmSlidingMenuRightMargin, dip2px(context, 50));
        //获取菜单页的宽度 = 屏幕的宽度 - 右边的一小部分（自己定义的宽度）
        mMenuWidth = (int)(getScreenWidth(context) - rightMargin);
        array.recycle();

        mGestureDetector = new GestureDetector(context,new GestureListener());
    }

    /**
     * @info 初始化手势处理类 手势处理类的监听回调
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        //只处理快速滑动
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //判断，快速上下滑动的时候不做处理
            if(Math.abs(velocityY) > Math.abs(velocityX)){
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            if(mMenuIsOpen){
                if(velocityX < 0){//
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

    private void toggleMenu(){
        if(mMenuIsOpen){
            closeMenu();
        }else{
            openMenu();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mMenuIsOpen){
            float currentX = ev.getX();
            if(currentX > mMenuWidth){
                toggleMenu();
                return false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * @info 在解析完xml文件后，里面调用的方法 1.指定宽高
     */
    @Override
    protected void onFinishInflate() {
        //解析完xml文件（布局文件）
        super.onFinishInflate();

        //指定宽高 （内容页面的宽度是屏幕的宽度）
        //先做判断，子View是否是2个
        ViewGroup container = (ViewGroup) getChildAt(0);
        int childCount = container.getChildCount();
        if(childCount != 2){
            throw new RuntimeException("只能放置2个子View");
        }
        //获取菜单view
        mMenuView = container.getChildAt(0);
        //设置宽高
        ViewGroup.LayoutParams menuParams = mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        mMenuView.setLayoutParams(menuParams);

        //获取主页面View
        mContentView = container.getChildAt(1);
        ViewGroup.LayoutParams contentParams = mContentView.getLayoutParams();
        contentParams.width = getScreenWidth(getContext());
        mContentView.setLayoutParams(contentParams);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 2. 初始化进来后是 菜单界面是关闭的
        scrollTo(mMenuWidth,0);
    }

    /**
     * @info 3. 处理手势变换
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //添加手势快速滑动处理
        if(mGestureDetector.onTouchEvent(ev)){
            return false;
        }

        //1.获取手指移动的速率，如果速率大于一定值，就认定为快速滑动,
        //2.处理事件拦截

        if(ev.getAction() == MotionEvent.ACTION_UP){

            int currentScrollX = getScrollX();

            if(currentScrollX > mMenuWidth/2){
                //关闭菜单
                closeMenu();
            }else{
                //打开菜单
                openMenu();
            }
            return true;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * @info 打开菜单
     */
    private void openMenu() {
        smoothScrollTo(0,0);
        mMenuIsOpen = true;
    }

    /**
     * @info 关闭菜单
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth,0);
        mMenuIsOpen = false;
    }

    /**
     * @info 4.处理右边主页面的缩放，需要不断的获取当前滚动的距离
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //计算滚动的梯度值
        float sclae = 1f * l / mMenuWidth;
        //右边的缩放：最大是0.7f，最小时1f
        float rightScale = 0.8f + 0.2f*sclae;

        //设置右边的缩放，默认是以中心点缩放
        ViewCompat.setPivotX(mContentView,0);
        ViewCompat.setPivotY(mContentView,mContentView.getMeasuredHeight()/2);
        ViewCompat.setScaleX(mContentView,rightScale);
        ViewCompat.setScaleY(mContentView,rightScale);

        //设置菜单的缩放和透明
        //透明
        float leftAlpha = 0.5f
                + (1 - sclae)*0.5f;
        ViewCompat.setAlpha(mMenuView,leftAlpha);

        //缩放 0.7f
        float leftSclae = 0.7f + (1 - sclae)*0.3f;
        ViewCompat.setScaleX(mMenuView,leftSclae);
        ViewCompat.setScaleY(mMenuView,leftSclae);

        //设置 退出 按钮的位置 平移 1*0.7f
        ViewCompat.setTranslationX(mMenuView,0.25f);

    }



    /**
     * @info 获取屏幕的高度
     * @param context
     * @return
     */
    private int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * @info Dip into pixels
     * @param context
     * @param dpValue
     * @return
     */
    private int dip2px(Context context,float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
}
