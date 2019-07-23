package com.test.cmletterview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.test.cmviewdemo.R;

public class CMLetterSiderViewActivity extends AppCompatActivity {

    private TextView mSiderTextTv;
    private CMLetterSiderView mSiderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmletter_sider_view);

        initView();

        mSiderView.setOnCMLetterSiderTouchListener(new CMLetterSiderView.CMLetterSiderTouchListener() {
            @Override
            public void cmLetterSiderTouchListener(CharSequence letter,boolean isTouch) {
                if(isTouch){
                    mSiderTextTv.setText(letter);
                    mSiderTextTv.setVisibility(View.VISIBLE);
                }else{
                    mSiderTextTv.setVisibility(View.GONE);
                }

            }
        });
    }

    private void initView() {
        mSiderTextTv = (TextView)findViewById(R.id.sider_text_tv);
        mSiderView = (CMLetterSiderView)findViewById(R.id.sider_letter_cmlettersider);
    }
}
