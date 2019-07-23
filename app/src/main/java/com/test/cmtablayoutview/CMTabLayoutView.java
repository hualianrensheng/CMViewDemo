package com.test.cmtablayoutview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muchen on 2018/2/7.
 */

public class CMTabLayoutView extends ViewGroup {

    private CMTabLayoutBaseAdapter cmTabLayoutBaseAdapter;

    private List<List<View>> mChildViews = new ArrayList<>();

    public CMTabLayoutView(Context context) {
        this(context,null);
    }

    public CMTabLayoutView(Context context, AttributeSet attrs)  {
        this(context, attrs,0);
    }

    public CMTabLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @Info 指定宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //清空集合
        mChildViews.clear();

        //获取子View的个数
        int childCount = getChildCount();

        //获取宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 计算高度
        int height = getPaddingTop() + getPaddingBottom();
        // 一行的宽度
        int lindWidth = getPaddingLeft();

        ArrayList<View> childViews = new ArrayList<View>();
        mChildViews.add(childViews);

        //如果子View高度不一致
        int maxHeight = 0;

        for (int i = 0; i < childCount ; i++){
            //2.1.1 for循环 测量子View
            View childView = getChildAt(i);
            // 获取子View的宽高，调用子View的onMeasure
            measureChild(childView,widthMeasureSpec,widthMeasureSpec);

            //获取margin值，ViewGroup.LayoutParams 没有，就用系统的MarginLayoutParams
            //LinearLahout 有自己的LayoutParams
            ViewGroup.MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();

            //当所有子View的宽度相加，大于父控件的宽度的时候，执行换行
            if(lindWidth + (childView.getMeasuredWidth() + params.rightMargin + params.leftMargin) > width){
                //累加高度，加上上一行的最大高度
                height += maxHeight;
                lindWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                childViews = new ArrayList<View>();
                mChildViews.add(childViews);
            }else{
                lindWidth += childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                maxHeight = Math.max((childView.getMeasuredHeight() + params.topMargin + params.bottomMargin),maxHeight);
            }
            childViews.add(childView);

        }

        height += maxHeight;

        Log.e("TAG","width -> "+ width + " height -> "+height);

        // 根据子View计算和指定自己的宽高
        setMeasuredDimension(width,height);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    /**
     * @info 从新设置布局位置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();

        int left = 0,top = getPaddingTop(),right = 0,bottom = 0;

        int maxHeight = 0;
        for (List<View> childViews : mChildViews) {

            left = getPaddingLeft();

            for (View childView : childViews) {

                //如果view是不可见状态，则不显示
                if(childView.getVisibility() == GONE){
                    continue;
                }

                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();

                left += params.leftMargin;
                int childTop = top + params.topMargin;

                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();

                Log.e("TAG","left -> "+ left + " right -> " + right + " top -> "+ top + "bottom ->" + bottom);

                //摆放
                childView.layout(left,top,right,bottom);
                // 宽度叠加
                left += childView.getMeasuredWidth() + params.rightMargin;

                int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                maxHeight = Math.max(maxHeight,childHeight);
            }

            top += maxHeight;
        }
    }

    public void setAdapter(CMTabLayoutBaseAdapter adapter){
        //判断传入的adapter是否为空
        if(adapter == null){
            //抛出空指针异常
        }

        //每一次设置之前，都要将之前的View清空
        removeAllViews();

        cmTabLayoutBaseAdapter = null;
        cmTabLayoutBaseAdapter = adapter;

        //获取childView的数量
        int childCount = cmTabLayoutBaseAdapter.getCount();
        for (int i = 0;i < childCount; i++){
            //通过位置，获取View
            View childView = cmTabLayoutBaseAdapter.getView(i,this);
            addView(childView);
        }
    }

}
