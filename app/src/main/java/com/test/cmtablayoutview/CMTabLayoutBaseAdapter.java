package com.test.cmtablayoutview;

import android.view.View;
import android.view.ViewGroup;

/**
 * @info 流式布局的adapter
 * Created by muchen on 2018/2/26.
 */

public abstract class CMTabLayoutBaseAdapter {

    //1.有多少个条目
    public abstract int getCount();

    //2.通过位置获取当前的view
    public abstract View getView(int position, ViewGroup parent);
}
