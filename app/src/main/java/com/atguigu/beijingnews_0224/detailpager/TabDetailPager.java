package com.atguigu.beijingnews_0224.detailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.baselibrary.CacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.activity.NewsDetailActivity;
import com.atguigu.beijingnews_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnews_0224.domain.NewsCenterBean;
import com.atguigu.beijingnews_0224.domain.TabDetailPagerBean;
import com.atguigu.beijingnews_0224.view.HorizontalScrollViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

import static com.atguigu.baselibrary.Constants.BASE_URL;

/**
 * 作者：田学伟 on 2017/6/5 09:25
 * QQ：93226539
 * 作用：
 */

public class TabDetailPager extends MenuDetailBasePager {
    public static final String READ_ID_ARRAY = "read_id_array";
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;

    HorizontalScrollViewPager viewpager;
    TextView tvTitle;
    LinearLayout llPointGroup;
    ListView lv;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView pullRefreshList;

    private String url;

    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;

    private int prePosition = 0;
    /**
     * 新闻列表的数据集合
     */

    private ListAdapter adapter;
    private List<TabDetailPagerBean.DataBean.NewsBean> newsBeenList;
    private String moreUrl;
    private boolean isLoadingMore = false;

    public TabDetailPager(Context mContext, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(mContext);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);

        //得到listView
        lv = pullRefreshList.getRefreshableView();
        /**
         * 增加下拉刷新的声音
         */
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mContext);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pullRefreshList.setOnPullEventListener(soundListener);
        //顶部视图
        View viewTopNews = View.inflate(mContext, R.layout.tab_detail_topnews, null);
        viewpager = (HorizontalScrollViewPager) viewTopNews.findViewById(R.id.viewpager);
        tvTitle = (TextView) viewTopNews.findViewById(R.id.tv_title);
        llPointGroup = (LinearLayout) viewTopNews.findViewById(R.id.ll_point_group);

        //把顶部的部分以添加头的方式加入ListView中
        lv.addHeaderView(viewTopNews);
        //设置监听ViewPager页面的变化
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //把之前的设置默认
                llPointGroup.getChildAt(prePosition).setEnabled(false);
                //当前的设置true
                llPointGroup.getChildAt(position).setEnabled(true);
                //记录当前的值
                prePosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                String title = topnews.get(position).getTitle();
                tvTitle.setText(title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置下拉和上拉刷新的监听
        pullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadingMore = false;
                getDataFromNet(url);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!TextUtils.isEmpty(moreUrl)) {
                    isLoadingMore = true;
                    getDataFromNet(moreUrl);
                } else {
                    Toast.makeText(mContext, "没有更多的数据了...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //设置listview的item的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int realPosition = position - 2;
                TabDetailPagerBean.DataBean.NewsBean newsBean = newsBeenList.get(realPosition);
//                Log.e("TAG", "" + newsBean.getId() + "--------" + newsBean.getTitle());
                //获取
                String idArray = CacheUtils.getString(mContext, READ_ID_ARRAY);
                //判断是否存在
                if (!idArray.contains(newsBean.getId() + "")) {
                    idArray = idArray + newsBean.getId() + ",";
                    CacheUtils.putString(mContext, READ_ID_ARRAY, idArray);
                    //刷新适配器
                    adapter.notifyDataSetChanged();
                }
                String url = Constants.BASE_URL + newsBean.getUrl();
                //跳转到Activity显示新闻详情内容
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据
        url = BASE_URL + childrenBean.getUrl();
        getDataFromNet(url);
    }

    /**
     * 网络请求
     */
    private void getDataFromNet(String url) {
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
                        //结束下拉刷新
                        pullRefreshList.onRefreshComplete();
                    }
                });
    }

    private void processData(String response) {
        TabDetailPagerBean bean = new Gson().fromJson(response, TabDetailPagerBean.class);
//        Log.e("TAG", "" + bean.getData().getNews().get(0).getTitle));

        String more = bean.getData().getMore();
        if (!TextUtils.isEmpty(more)) {
            moreUrl = Constants.BASE_URL + more;
        }

        if (!isLoadingMore) {
            //顶部的
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
            //listView的数据
            newsBeenList = bean.getData().getNews();
            adapter = new ListAdapter();
            lv.setAdapter(adapter);
        } else {
            isLoadingMore = false;
            newsBeenList.addAll(bean.getData().getNews());//把新的数据集合加入到原来集合中，而不是覆盖
            adapter.notifyDataSetChanged();//适配器刷新
        }

    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsBeenList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_tab_detail, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TabDetailPagerBean.DataBean.NewsBean newsBean = newsBeenList.get(position);
            viewHolder.tvDesc.setText(newsBean.getTitle());
            viewHolder.tvTime.setText(newsBean.getPubdate());
            String imageUrl = Constants.BASE_URL + newsBean.getListimage();
            Glide.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivIcon);

            //判断是否已经被点击了
            String idArray = CacheUtils.getString(mContext, READ_ID_ARRAY);
            if (idArray.contains(newsBean.getId() + "")) {
                //灰色
                viewHolder.tvDesc.setTextColor(Color.GRAY);
            } else {
                //黑色
                viewHolder.tvDesc.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_desc)
        TextView tvDesc;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
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
            String imageUrl = BASE_URL + topnews.get(position).getTopimage();

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
