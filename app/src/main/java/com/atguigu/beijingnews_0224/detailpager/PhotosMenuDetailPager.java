package com.atguigu.beijingnews_0224.detailpager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.atguigu.baselibrary.CacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.adapter.PhotosMenuDetailPagerAdapater;
import com.atguigu.beijingnews_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnews_0224.domain.NewsCenterBean;
import com.atguigu.beijingnews_0224.domain.PhotosMenuDetailPagerBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 作者：田学伟 on 2017/6/3 14:18
 * QQ：93226539
 * 作用：组图详情页面的内容
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    private final NewsCenterBean.DataBean dataBean;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private String url;
    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas;

    private PhotosMenuDetailPagerAdapater adapater;

    public PhotosMenuDetailPager(Context mContext, NewsCenterBean.DataBean dataBean) {
        super(mContext);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        //实例视图
        View view = View.inflate(mContext, R.layout.pager_photos_menu_detail, null);
        ButterKnife.inject(this, view);
        //设置下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(url);
            }
        });
        //设置滑动多少的距离才有效果
//        refreshLayout.setDistanceToTriggerSync(100);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //联网请求
        url = Constants.BASE_URL + dataBean.getUrl();
        Log.e("TAG", "图组的网络地址=====" + url);
        String saveJson = CacheUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet(url);
    }

    private void getDataFromNet(final String url) {
        OkHttpUtils.get()
                .url(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.e("TAG", "图组请求失败==" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.e("TAG", "图组请求成功==" + response);
                CacheUtils.putString(mContext, url, response);
                processData(response);
            }
        });
    }

    /**
     * 解析数据
     *
     * @param json
     */
    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
        datas = bean.getData().getNews();
        if (datas != null && datas.size() > 0) {
            //有数据
            progressbar.setVisibility(View.GONE);
            adapater = new PhotosMenuDetailPagerAdapater(mContext, datas, recyclerview);
            //设置适配器
            recyclerview.setAdapter(adapater);
            //设置布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        } else {
            //没有数据
            progressbar.setVisibility(View.VISIBLE);
        }
        //隐藏刷新进度的效果
        refreshLayout.setRefreshing(false);
    }

    /**
     * 显示list效果,
     */
    private boolean isShowList = true;

    /**
     * 图组切换图片
     *
     * @param iv
     */
    public void swichListAndGrid(ImageButton iv) {
        if (isShowList) {
            //显示Grid
            recyclerview.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
            isShowList = false;
            //按钮状态--List
            iv.setImageResource(R.drawable.icon_pic_list_type);
        } else {
            //显示List
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            isShowList = true;
            //按钮状态--Grid
            iv.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }
}
