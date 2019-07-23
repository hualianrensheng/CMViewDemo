package com.test.cmMaterialDesign;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * @info 沉浸式状态栏
 * Created by muchen on 2018/3/23.
 */

public class CMMaterialDesignStatusBarUtil {


    public static void setStatusBarColor(Activity activity,int color){

        //判断，手机如果是5.0以上，直接设置状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //直接设置状态栏颜色
//            activity.getWindow().setStatusBarColor(color);

            View decorView = activity.getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
            activity.getWindow().setStatusBarColor(color);

        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){// 系统 4.4 - 5.0 之间的
            /*思路
                1.在4.4 - 5.0 之间 将他弄成全屏
                2.acitvity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                3.保证 电量 时间 网络状态 等都在
             */
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //在状态栏的部分添加一个布局 setContentView 的源码,自己添加一个布局（高度是状态栏的高度）
            View view = new View(activity);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
            view.setLayoutParams(params);
            view.setBackgroundColor(color);

            ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
            decorView.addView(view);

            //获取activity中setContentView布局的根布局
            ViewGroup contentView = (ViewGroup)activity.findViewById(android.R.id.content);
            contentView.setPadding(0,getStatusBarHeight(activity),0,0);
        }
    }

    /**
     * @info 获取状态栏的高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity){
        //获取状态栏的高度
        Resources resources = activity.getResources();
        int statusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(statusBarHeight);
    }

    /**
     * 设置Activity 全屏
     * @param activity
     */
    public static void setStatusBarTranslucent(Activity activity){
        //5.0以上
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //获取DecorView
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//4.4 - 5.0 之间
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //在状态栏的部分添加一个布局 setContentView 的源码,自己添加一个布局（高度是状态栏的高度）
            View view = new View(activity);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
            view.setLayoutParams(params);
            view.setBackgroundColor(Color.TRANSPARENT);

            ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
            decorView.addView(view);

            //获取activity中setContentView布局的根布局
            ViewGroup contentView = (ViewGroup)activity.findViewById(android.R.id.content);
            contentView.setPadding(0,getStatusBarHeight(activity),0,0);
        }
    }
}
