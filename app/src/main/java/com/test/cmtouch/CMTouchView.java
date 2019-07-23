package com.test.cmtouch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by muchen on 2018/2/27.
 */

public class CMTouchView extends View {


    public CMTouchView(Context context) {
        this(context,null);
    }

    public CMTouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CMTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG","View      onTouchEvent()"+event.getAction());
        return super.onTouchEvent(event);
//        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("TAG","View      dispatchTouchEvent()"+event.getAction());
        return super.dispatchTouchEvent(event);
    }
}
