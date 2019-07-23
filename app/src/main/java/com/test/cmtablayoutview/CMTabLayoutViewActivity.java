package com.test.cmtablayoutview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.cmviewdemo.R;

import java.util.ArrayList;
import java.util.List;

public class CMTabLayoutViewActivity extends AppCompatActivity {

    private CMTabLayoutView cmTableLayoutView;
    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmtab_layout_view);

        initView();

        initData();
    }



    /**
     * @info 添加控件
     */
    private void initView() {
        cmTableLayoutView = (CMTabLayoutView)findViewById(R.id.tab_layout);
    }

    private void initData() {

        mItems = new ArrayList<>();

        mItems.add("1111");
        mItems.add("22222222");
        mItems.add("333333333333333");
        mItems.add("4444");
        mItems.add("55555555");
        mItems.add("666666666666");
        mItems.add("77777");
        mItems.add("8888888888");
        mItems.add("9999999999999999");
        mItems.add("10101010aa");

        cmTableLayoutView.setAdapter(new CMTabLayoutBaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public View getView(int position, ViewGroup parent) {

                TextView tv = (TextView) LayoutInflater.from(CMTabLayoutViewActivity.this).inflate(R.layout.cmlablayout_item,parent,false);
                tv.setText(mItems.get(position));
                return tv;
            }
        });

    }
}
