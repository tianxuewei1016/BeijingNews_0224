package com.atguigu.beijingnews_0224.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.base.BaseFragment;
import com.atguigu.beijingnews_0224.base.BasePager;
import com.atguigu.beijingnews_0224.pager.HomePager;
import com.atguigu.beijingnews_0224.pager.NewsPager;
import com.atguigu.beijingnews_0224.pager.SettingPager;

import java.util.ArrayList;

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

    private ArrayList<BasePager> pagers;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设ViewPager的数据-适配器
        //准备数据
        pagers = new ArrayList<>();
        pagers.add(new HomePager(mContext));//主页面
        pagers.add(new NewsPager(mContext));//新闻中心
        pagers.add(new SettingPager(mContext));//设置中心

        vp.setAdapter(new MyAdapter());

        //设置RadioGroup的监听
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        vp.setCurrentItem(1, false);
                        break;
                    case R.id.rb_setting:
                        vp.setCurrentItem(2, false);
                        break;
                }
            }
        });

        //默认选中主页
        rgMain.check(R.id.rb_home);
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootView;
            //调用initData方法
            basePager.initData();//HomePager,NewsPager,SettingPager
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}

