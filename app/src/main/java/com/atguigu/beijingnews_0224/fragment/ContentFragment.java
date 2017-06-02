package com.atguigu.beijingnews_0224.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews_0224.base.BaseFragment;

/**
 * 作者：田学伟 on 2017/6/2 15:29
 * QQ：93226539
 * 作用：
 */

public class ContentFragment extends BaseFragment{
    private TextView textView;
    @Override
    protected View initView() {
        textView = new TextView(mContext);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("主菜单");
    }
}

