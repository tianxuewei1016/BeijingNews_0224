package com.atguigu.beijingnews_0224.fragment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.activity.MainActivity;
import com.atguigu.beijingnews_0224.base.BaseFragment;
import com.atguigu.beijingnews_0224.base.BasePager;
import com.atguigu.beijingnews_0224.pager.HomePager;
import com.atguigu.beijingnews_0224.pager.NewsPager;
import com.atguigu.beijingnews_0224.pager.SettingPager;
import com.atguigu.beijingnews_0224.view.NoViewPager;
import com.slidingmenu.lib.SlidingMenu;

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
    NoViewPager vp;
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
                        //调用initData()方法
//                        pagers.get(0).initData();
                        break;
                    case R.id.rb_news:
                        vp.setCurrentItem(1, false);
//                        pagers.get(1).initData();
                        break;
                    case R.id.rb_setting:
                        vp.setCurrentItem(2, false);
//                        pagers.get(2).initData();
                        break;
                }
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagers.get(position).initData();
                if (position == 1) {
                    //可以侧滑
                    isEnableSlidingMenu(mContext, SlidingMenu.TOUCHMODE_FULLSCREEN);
                } else {
                    //其他不可以侧滑
                    isEnableSlidingMenu((MainActivity) mContext, SlidingMenu.TOUCHMODE_NONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagers.get(0).initData();
        //默认选中主页
        rgMain.check(R.id.rb_home);

        //一进来不可以侧滑
        isEnableSlidingMenu((MainActivity) mContext, SlidingMenu.TOUCHMODE_NONE);
    }



    /**
     * 是否让SlidingMenu滑动
     *
     * @param mContext
     * @param touchmodeFullscreen
     */
    private static void isEnableSlidingMenu(Context mContext, int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) mContext;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
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
            //basePager.initData();//HomePager,NewsPager,SettingPager
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

