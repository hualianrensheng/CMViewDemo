package com.test.cmVerticalDragListView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.test.cmviewdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class CMVerticalDragListViewActivity extends AppCompatActivity {

    private ListView mLv;
    private List<String> mMItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmvertical_drag_list_view);

        initView();

        initData();

        setData();
    }



    private void initView() {
        mLv = (ListView)findViewById(R.id.menu_item_lv);
    }


    private void initData() {
        mMItems = new ArrayList<String>();
        for (int i = 0; i < 100; i++){
            mMItems.add("老朱 "+ i + " 号");
        }
    }


    private void setData() {

        mLv.setAdapter(new BaseAdapter() {

            private LayoutInflater inflater;



            @Override
            public int getCount() {
                return mMItems.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(CMVerticalDragListViewActivity.this).inflate(R.layout.activity_cmvertical_drag_item, null);
                TextView tv = (TextView)view.findViewById(R.id.cm_vertical_item_tv);
                tv.setText(mMItems.get(position));
                return view;
            }
        });
    }
}
