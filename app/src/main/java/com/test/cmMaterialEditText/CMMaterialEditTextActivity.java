package com.test.cmMaterialEditText;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.test.cmviewdemo.R;

public class CMMaterialEditTextActivity extends AppCompatActivity {

    private cmMaterialEditTextView mCmMaterialEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmmaterial_edit_text);

        mCmMaterialEditTextView = (cmMaterialEditTextView)findViewById(R.id.cm_material_edittext);
        mCmMaterialEditTextView.setUserFloatingLabel(true);
    }
}
