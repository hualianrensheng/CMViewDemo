package com.test.cmMaterialDesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.test.cmviewdemo.R;

public class CMMaterialDesionDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmmaterial_desion_demo);

        //沉浸式状态栏
        startMaterialDesionDemo();

        //定义Behavior
        startCMBehaviorDemo();
    }

    private void startMaterialDesionDemo() {
        Button materialDesignDemo = (Button)findViewById(R.id.material_cmmaterialdesign_bt);

        materialDesignDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CMMaterialDesionDemoActivity.this,CMMaterialDesionStatusBarActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startCMBehaviorDemo() {
        Button mBehaviorDemo = (Button)findViewById(R.id.material_behavior_bt);

        mBehaviorDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CMMaterialDesionDemoActivity.this,CMMaterialBehaviorActivity.class);
                startActivity(intent);
            }
        });
    }
}
