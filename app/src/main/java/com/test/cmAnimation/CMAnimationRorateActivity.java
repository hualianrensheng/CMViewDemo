package com.test.cmAnimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.test.cmUtils.DpToPxUtil;
import com.test.cmviewdemo.R;

public class CMAnimationRorateActivity extends AppCompatActivity {

    private cmAnimationRorateView mSunView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmanimation_rorate);

        mSunView = findViewById(R.id.sun);

        mSunView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ObjectAnimator bottomFlipAnimator = ObjectAnimator.ofFloat(mSunView, "bottomFlip", 45);
                bottomFlipAnimator.setDuration(1500);

                ObjectAnimator flipRotationAnimator = ObjectAnimator.ofFloat(mSunView, "flipRotation", 270);
                flipRotationAnimator.setDuration(1500);

                ObjectAnimator topFlipAnimator = ObjectAnimator.ofFloat(mSunView, "topFlip", - 45);
                topFlipAnimator.setDuration(1500);

//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playSequentially(bottomFlipAnimator, flipRotationAnimator, topFlipAnimator);
//        animatorSet.setStartDelay(1000);
//        animatorSet.start();

                PropertyValuesHolder bottomFlipHolder = PropertyValuesHolder.ofFloat("bottomFlip", 45);
                PropertyValuesHolder flipRotationHolder = PropertyValuesHolder.ofFloat("flipRotation", 270);
                PropertyValuesHolder topFlipHolder = PropertyValuesHolder.ofFloat("topFlip", - 45);
                ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mSunView, bottomFlipHolder, flipRotationHolder, topFlipHolder);
                objectAnimator.setStartDelay(1000);
                objectAnimator.setDuration(2000);
                objectAnimator.start();
            }
        });


    }
}
