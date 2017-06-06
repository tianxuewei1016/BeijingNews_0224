package com.atguigu.beijingnews_0224.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.activity.MainActivity;

/**
 * 作者：田学伟 on 2017/6/2 16:21
 * QQ：93226539
 * 作用：视图的基类
 */

public class BasePager {
    public Context mContext;
    /**
     * 代表一整个页面
     */
    public View rootView;
    public TextView tv_title;
    public ImageButton ib_menu;
    public ImageButton ib_switch_list_grid;
    /**
     * 子类的视图添加到该布局上
     */
    public FrameLayout fl_content;


    public BasePager(final Context mContext) {
        this.mContext = mContext;
        //初始化布局
        rootView = View.inflate(mContext, R.layout.base_pager, null);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) rootView.findViewById(R.id.ib_menu);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_content);
        ib_switch_list_grid = (ImageButton) rootView.findViewById(R.id.ib_switch_list_grid);

        //设置点击事件
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开---关
                ((MainActivity) mContext).getSlidingMenu().toggle();
            }
        });

    }

    /**
     * 子类绑定数据的时候重写
     */
    public void initData() {

    }
}
