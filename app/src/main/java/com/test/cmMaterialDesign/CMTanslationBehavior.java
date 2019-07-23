package com.test.cmMaterialDesign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by muchen on 2018/3/28.
 */

public class CMTanslationBehavior extends FloatingActionButton.Behavior {

    private boolean isOut = false;

    public CMTanslationBehavior(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    //确定滚动方向
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return  nestedScrollAxes  == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        Log.e("TAG","dyConsumed -> "+dyConsumed+" dyUnconsumed -> "+dyUnconsumed);
        if(dyConsumed > 0){
            if(!isOut){
                //向上滑动
                int translationY = ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).bottomMargin + child.getMeasuredHeight();
                child.animate().translationY(translationY).setDuration(500).start();
                isOut = true;
            }
        }else{
            if(isOut){
                //向下滑动
                child.animate().translationY(0).setDuration(500).start();
                isOut = false;
            }
        }
    }
}
