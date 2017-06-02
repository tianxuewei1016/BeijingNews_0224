package com.atguigu.beijingnews_0224.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：田学伟 on 2017/6/2 15:29
 * QQ：93226539
 * 作用：
 */

public class ContentFragment extends BaseFragment {

    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //默认选中主页
        rgMain.check(R.id.rb_home);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}

