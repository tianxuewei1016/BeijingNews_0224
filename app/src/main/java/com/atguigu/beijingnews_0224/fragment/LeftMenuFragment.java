package com.atguigu.beijingnews_0224.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.atguigu.beijingnews_0224.base.BaseFragment;
import com.atguigu.beijingnews_0224.domain.NewsCenterBean;

import java.util.List;

/**
 * 作者：田学伟 on 2017/6/2 15:27
 * QQ：93226539
 * 作用：
 */

public class LeftMenuFragment extends BaseFragment {
    private ListView listView;
    private List<NewsCenterBean.DataBean> datas;

    @Override
    protected View initView() {
        listView = new ListView(mContext);
        return listView;
    }

    @Override
    public void initData() {
        super.initData();

    }

    public void setData(List<NewsCenterBean.DataBean> datas) {
        this.datas = datas;
        for (int i = 0; i < datas.size(); i++) {
            Log.e("TAG", "" + datas.get(i).getTitle());

        }
    }
}
