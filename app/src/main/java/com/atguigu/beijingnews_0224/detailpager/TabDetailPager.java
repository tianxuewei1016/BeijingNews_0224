package com.atguigu.beijingnews_0224.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnews_0224.domain.NewsCenterBean;
import com.atguigu.beijingnews_0224.domain.TabDetailPagerBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 作者：田学伟 on 2017/6/5 09:25
 * QQ：93226539
 * 作用：
 */

public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.lv)
    ListView lv;

    private String url;

    private List<TabDetailPagerBean.DataEntity.TopnewsEntity> topnews;

    private int prePosition = 0;

    public TabDetailPager(Context mContext, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(mContext);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);
        //设置监听ViewPager页面的变化
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = topnews.get(position).getTitle();
                tvTitle.setText(title);

                //把之前的设置默认
                llPointGroup.getChildAt(prePosition).setEnabled(false);
                //当前的设置true
                llPointGroup.getChildAt(position).setEnabled(true);
                //记录当前的值
                prePosition = position;
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
        //设置数据
        url = Constants.BASE_URL + childrenBean.getUrl();
        getDataFromNet();
    }

    /**
     * 网络请求
     */
    private void getDataFromNet() {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        Log.e("TAG", "请求失败==" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Log.e("TAG", "请求成功==" + response);
                        //缓存数据
                        processData(response);
                    }
                });
    }

    private void processData(String response) {
        TabDetailPagerBean bean = new Gson().fromJson(response, TabDetailPagerBean.class);
//        Log.e("TAG", "" + bean.getData().getNews().get(0).getTitle());
        topnews = bean.getData().getTopnews();
        //设置适配器
        viewpager.setAdapter(new MyPagerAdapter());
        tvTitle.setText(topnews.get(prePosition).getTitle());

        //把之前的移除
        llPointGroup.removeAllViews();
        //添加知识点
        for (int i = 0; i < topnews.size(); i++) {
            ImageView point = new ImageView(mContext);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            point.setLayoutParams(params);

            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = 8;
            }
            //添加到线性布局中去
            llPointGroup.addView(point);
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.news_pic_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置网络图片
            String imageUrl = Constants.BASE_URL + topnews.get(position).getTopimage();

            Glide.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
