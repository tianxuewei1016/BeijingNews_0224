package com.atguigu.beijingnews_0224.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews_0224.activity.MainActivity;
import com.atguigu.beijingnews_0224.base.BasePager;
import com.atguigu.beijingnews_0224.base.MenuDetailBasePager;
import com.atguigu.beijingnews_0224.detailpager.InteractMenuDetailPager;
import com.atguigu.beijingnews_0224.detailpager.NewsMenuDetailPager;
import com.atguigu.beijingnews_0224.detailpager.PhotosMenuDetailPager;
import com.atguigu.beijingnews_0224.detailpager.TopicMenuDetailPager;
import com.atguigu.beijingnews_0224.detailpager.VoteMenuDetailPager;
import com.atguigu.beijingnews_0224.domain.NewsCenterBean;
import com.atguigu.beijingnews_0224.fragment.LeftMenuFragment;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者：田学伟 on 2017/6/2 16:50
 * QQ：93226539
 * 作用：
 */

public class NewsPager extends BasePager {
    /**
     * 左侧页面的数据集合
     */
    private List<NewsCenterBean.DataBean> datas;
    /**
     * 左侧菜单详情的页面的集合
     */
    private List<MenuDetailBasePager> basePagers;


    public NewsPager(Context mContext) {
        super(mContext);
    }

    @Override
    public void initData() {
        super.initData();
        //把数据绑定到视图上

        //设置标题
        tv_title.setText("新闻");
        ib_menu.setVisibility(View.VISIBLE);
        //实例视图
        TextView textView = new TextView(mContext);
        textView.setText("新闻页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        //添加到布局上
        fl_content.addView(textView);

        //联网请求
        getDataForNet();


    }

    private void getDataForNet() {
        String url = Constants.NEWSCENTER_PAGER_URL;
        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        Log.e("TAG", "联网失败" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Log.e("TAG", "联网成功" + response);
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
        NewsCenterBean newsCenterBean = new Gson().fromJson(json, NewsCenterBean.class);
        Log.e("TAG", "解析成功==" + newsCenterBean.getData().get(0).getChildren().get(0).getTitle());
        datas = newsCenterBean.getData();
        //传入到左侧菜单
        MainActivity mainActivity = (MainActivity) mContext;

        //实例化详情页面
        basePagers = new ArrayList<>();
        basePagers.add(new NewsMenuDetailPager(mContext));
        basePagers.add(new TopicMenuDetailPager(mContext));
        basePagers.add(new PhotosMenuDetailPager(mContext));
        basePagers.add(new InteractMenuDetailPager(mContext));
        basePagers.add(new VoteMenuDetailPager(mContext));

        //得到左侧菜单Fragment
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //设置数据
        leftMenuFragment.setData(datas);


    }

    /**
     * 根据位置切换到不同的详情页面
     *
     * @param prePosition
     */
    public void swichPager(int prePosition) {
        MenuDetailBasePager basePager = basePagers.get(prePosition);//NewsMenuDetailPager,TopicMenuDetailPager...
        View rootView = basePager.rootView;
        fl_content.removeAllViews();//把之前显示的给移除

        fl_content.addView(rootView);

        //调用InitData
        basePager.initData();

    }
}
