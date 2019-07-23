package com.test.cmtouch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by muchen on 2018/2/27.
 */

public class CMTouchViewGroup extends LinearLayout {

    public CMTouchViewGroup(Context context) {
        this(context,null);
    }

    public CMTouchViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMTouchViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @info 事件分发
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("TAG","ViewGroup dispatchTouchEvent()"+ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    /**
     * @info 事件拦截
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("TAG","ViewGroup onInterceptTouchEvent()"+ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * @info 事件触摸
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG","ViewGroup onTouchEvent()"+event.getAction());
        return super.onTouchEvent(event);
    }
}
