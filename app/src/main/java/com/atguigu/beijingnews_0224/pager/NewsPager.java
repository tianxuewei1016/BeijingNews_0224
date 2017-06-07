package com.atguigu.beijingnews_0224.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.baselibrary.CacheUtils;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        //获取数据
        String savaJson = CacheUtils.getString(mContext, Constants.NEWSCENTER_PAGER_URL);
        //判断不能为空--取反--当不是null才能进来
        if (!TextUtils.isEmpty(savaJson)) {
            processData(savaJson);
        }
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
                        //缓存数据
                        CacheUtils.putString(mContext, Constants.NEWSCENTER_PAGER_URL, response);
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
//        NewsCenterBean newsCenterBean = new Gson().fromJson(json, NewsCenterBean.class);
        //使用系统的API解析json数据
        NewsCenterBean newsCenterBean = parseJson(json);
        Log.e("TAG", "解析成功==" + newsCenterBean.getData().get(0).getChildren().get(0).getTitle());
        datas = newsCenterBean.getData();
        //传入到左侧菜单
        MainActivity mainActivity = (MainActivity) mContext;

        //实例化详情页面
        basePagers = new ArrayList<>();
        basePagers.add(new NewsMenuDetailPager(mContext, datas.get(0).getChildren()));
        basePagers.add(new TopicMenuDetailPager(mContext,datas.get(0).getChildren()));
        basePagers.add(new PhotosMenuDetailPager(mContext, datas.get(2)));
        basePagers.add(new InteractMenuDetailPager(mContext));
        basePagers.add(new VoteMenuDetailPager(mContext));

        //得到左侧菜单Fragment
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //设置数据
        leftMenuFragment.setData(datas);


    }

    /**
     * 手动解析数据
     *
     * @param json
     * @return
     */
    private NewsCenterBean parseJson(String json) {
        NewsCenterBean newsCenterBean = new NewsCenterBean();

        try {
            JSONObject jsonObject = new JSONObject(json);
            //解析retcode
            int retcode = jsonObject.optInt("retcode");
            //设置数据
            newsCenterBean.setRetcode(retcode);

            JSONArray jsonArray = jsonObject.optJSONArray("data");
            //集合
            List<NewsCenterBean.DataBean> data = new ArrayList<>();
            newsCenterBean.setData(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                //数据
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (jsonObject1 != null) {
                    NewsCenterBean.DataBean dataBean = new NewsCenterBean.DataBean();

                    dataBean.setId(jsonObject1.optInt("id"));
                    dataBean.setType(jsonObject1.optInt("type"));
                    String title = jsonObject1.optString("title");
                    dataBean.setTitle(title);
                    String url = jsonObject1.optString("url");
                    dataBean.setUrl(url);

                    JSONArray jsonArray1 = jsonObject1.optJSONArray("children");

                    if (jsonArray1 != null) {
                        List<NewsCenterBean.DataBean.ChildrenBean> children = new ArrayList<>();
                        //设置children数据的
                        dataBean.setChildren(children);
                        for (int i1 = 0; i1 < jsonArray1.length(); i1++) {

                            JSONObject jsonObject2 = jsonArray1.getJSONObject(i1);

                            NewsCenterBean.DataBean.ChildrenBean childrenBean = new NewsCenterBean.DataBean.ChildrenBean();
                            //解析数据
                            childrenBean.setId(jsonObject2.optInt("id"));
                            childrenBean.setType(jsonObject2.optInt("type"));
                            childrenBean.setTitle(jsonObject2.optString("title"));
                            childrenBean.setUrl(jsonObject2.optString("url"));

                            //添加到集合中
                            children.add(childrenBean);
                        }
                    }
                    //添加到集合中
                    data.add(dataBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsCenterBean;
    }


    /**
     * 根据位置切换到不同的详情页面
     *
     * @param prePosition
     */

    public void swichPager(int prePosition) {

        //设置标题
        tv_title.setText(datas.get(prePosition).getTitle());

        MenuDetailBasePager basePager = basePagers.get(prePosition);//NewsMenuDetailPager,TopicMenuDetailPager...
        View rootView = basePager.rootView;
        fl_content.removeAllViews();//把之前显示的给移除

        fl_content.addView(rootView);

        //调用InitData
        basePager.initData();

        if (prePosition == 2) {
            //显示
            ib_switch_list_grid.setVisibility(View.VISIBLE);
            ib_switch_list_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotosMenuDetailPager basePager1 = (PhotosMenuDetailPager) basePagers.get(2);
                    basePager1.swichListAndGrid(ib_switch_list_grid);
                }
            });
        } else {
            //隐藏
            ib_switch_list_grid.setVisibility(View.GONE);
        }

    }
}
