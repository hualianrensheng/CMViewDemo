package com.test.cmVerticalDragListView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * Created by CMMY on 2018/3/1.
 */

public class CMVerticalDragListView extends FrameLayout {


    private ViewDragHelper mDragHelper;

    private View mDragView;
    //菜单文件的高度
    private int mMenuHeight;
    //菜单是否打开
    private boolean mMenuIsOpen = false;

    public CMVerticalDragListView(@NonNull Context context) {
        this(context,null);
    }

    public CMVerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMVerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper = ViewDragHelper.create(this,mDragHelperCallback);
    }

    /**
     * @info 加载完布局xml文件后，调用的方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //判断是否是有两个子view，如果不是，则抛出异常
        int childCount = getChildCount();
        if(2 != childCount){
            throw new RuntimeException("VerticalDragListView 只能包含2个子布局！");
        }
        mDragView = getChildAt(1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取子布局的高度
        View menuView = getChildAt(0);
        mMenuHeight = menuView.getMeasuredHeight();
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //指定该View是否可以拖动
            return mDragView == child;
        }

        //水平方向禁止移动
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //垂直方向移动的距离就是后面菜单view的高度
            if(top <= 0){
                top = 0;
            }
            if(top >= mMenuHeight){
                top = mMenuHeight;
            }
            return top;
        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if(releasedChild == mDragView){
                if(mDragView.getTop() > mMenuHeight/2){
                    //滑动到固定高度（打开）
                    mDragHelper.settleCapturedViewAt(0,mMenuHeight);
                }else{
                    mDragHelper.settleCapturedViewAt(0,0);
                }
                invalidate();
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    // 现象就是ListView可以滑动，但是菜单滑动没有效果了
    private float mDownY;

    // ecause ACTION_DOWN was not received for this pointer before ACTION_MOVE
    // VDLV.onInterceptTouchEvent().DOWN -> LV.onTouch() ->
    // VDLV.onInterceptTouchEvent().MOVE -> VDLV.onTouchEvent().MOVE
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 菜单打开要拦截
        if (mMenuIsOpen) {
            return true;
        }

        // 向下滑动拦截，不要给ListView做处理
        // 谁拦截谁 父View拦截子View ，但是子 View 可以调这个方法
        // requestDisallowInterceptTouchEvent 请求父View不要拦截，改变的其实就是 mGroupFlags 的值
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                // 让 DragHelper 拿一个完整的事件
                mDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                if ((moveY - mDownY) > 0 && !canChildScrollUp()) {
                    // 向下滑动 && 滚动到了顶部，拦截不让ListView做处理
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }



    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断View是否滚动到了最顶部,还能不能向上滚
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mDragView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mDragView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mDragView, -1) || mDragView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mDragView, -1);
        }
    }


    /**
     * 响应滚动
     */
    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
