package com.atguigu.beijingnews_0224.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.fragment.ContentFragment;
import com.atguigu.beijingnews_0224.fragment.LeftMenuFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFT_TAG = "left_tag";
    public static final String MAIN_TAG = "main_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSlidingMenu();
        initFragment();
    }

    private void initSlidingMenu() {
        //设置主页面
        setContentView(R.layout.activity_main);
        //设置左侧菜单
        setBehindContentView(R.layout.left_menu);


        SlidingMenu slidingMenu = getSlidingMenu();
        //3.设置右侧菜单
//        slidingMenu.setSecondaryMenu(R.layout.left_menu);

        //设置滑动模式:左侧+主页;左侧+主页+右侧;主页+右侧
        slidingMenu.setMode(SlidingMenu.LEFT);

        //设置滑动模式:不可以滑动;滑动边缘;全屏滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //设置主页面占的宽度
        slidingMenu.setBehindOffset(600);
    }

    private void initFragment() {
        //1.得到fragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事物
        FragmentTransaction ft = fm.beginTransaction();
        //3.替换两个Fragment
        ft.replace(R.id.fl_left, new LeftMenuFragment(), LEFT_TAG);
        ft.replace(R.id.fl_main, new ContentFragment(), MAIN_TAG);
        //4.提交事物
        ft.commit();
    }
}
