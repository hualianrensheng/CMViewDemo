package com.test.cmtouch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.test.cmviewdemo.R;


public class CMTouchViewActivity extends AppCompatActivity {

    private CMTouchView cmTouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmtouch_view);

        initView();
    }

    private void initView() {

        cmTouchView = (CMTouchView)findViewById(R.id.cmtouchview);

        cmTouchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("TAG","View      onTouch()"+event.getAction());
                return false;
            }
        });

//        cmTouchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("TAG","onClick()");
//            }
//        });
    }
}
