package com.atguigu.beijingnews_0224.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews_0224.base.BasePager;
import com.atguigu.beijingnews_0224.domain.NewsCenterBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 作者：田学伟 on 2017/6/2 16:50
 * QQ：93226539
 * 作用：
 */

public class NewsPager extends BasePager {
    public NewsPager(Context mContext) {
        super(mContext);
    }

    @Override
    public void initData() {
        super.initData();
        //把数据绑定到视图上

        //设置标题
        tv_title.setText("新闻");
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

    }
}
