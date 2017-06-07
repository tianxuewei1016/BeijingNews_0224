package com.atguigu.beijingnews_0224.detailpager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.activity.MainActivity;
import com.atguigu.beijingnews_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnews_0224.domain.NewsCenterBean;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.atguigu.beijingnews_0224.R.id.ib_next;

/**
 * 作者：田学伟 on 2017/6/3 14:18
 * QQ：93226539
 * 作用：专题详情页面的内容
 */

public class TopicMenuDetailPager extends MenuDetailBasePager {

    private final List<NewsCenterBean.DataBean.ChildrenBean> datas;
    /**
     * TabDetailPager对应的数据
     */

    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.tabLayout)
    TabLayout indicator;
    @InjectView(ib_next)
    ImageButton ibNext;
    /**
     * 页面集合,创建12次
     */
    private List<TabDetailPager> tabDetailPagers;


    public TopicMenuDetailPager(Context mContext, List<NewsCenterBean.DataBean.ChildrenBean> children) {
        super(mContext);
        this.datas = children;
    }

    @Override
    public View initView() {
        //实例视图
        View view = View.inflate(mContext, R.layout.pager_topic_menu_detail, null);
        ButterKnife.inject(this, view);

        //设置点击事件
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换到下一个页面
                viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            }
        });

        //监听页面的改变
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    //slidingMenu可以滑动
                    MainActivity mainActivity = (MainActivity) mContext;
                    mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                } else {
                    //slidingMenu不可以滑动
                    MainActivity mainActivity = (MainActivity) mContext;
                    mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //根据数据创建子页面
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            //一会在传递数据
            tabDetailPagers.add(new TabDetailPager(mContext, datas.get(i)));

        }
        //设置适配器
        viewpager.setAdapter(new NewsMenuDetailPagerAdapter());

//        TabPageIndicator和viewpager关联起来
//        indicator.setViewPager(viewpager);

        //TabLayout和ViewPager关联起来
        indicator.setupWithViewPager(viewpager);
        indicator.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    class NewsMenuDetailPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return tabDetailPagers == null ? 0 : tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            tabDetailPager.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return datas.get(position).getTitle();
        }
    }
}
