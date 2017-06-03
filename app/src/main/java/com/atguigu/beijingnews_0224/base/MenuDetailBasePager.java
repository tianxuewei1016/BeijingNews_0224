package com.atguigu.beijingnews_0224.base;

import android.content.Context;
import android.view.View;

/**
 * 作者：田学伟 on 2017/6/3 14:15
 * QQ：93226539
 * 作用：
 */

public abstract class MenuDetailBasePager {
    /**
     * 代表子类整个视图
     */
    public View rootView;
    public final Context mContext;

    public MenuDetailBasePager(Context mContext) {
        this.mContext = mContext;
        rootView = initView();
    }

    /**
     * 抽象方法,由子类实现,达到自己的视图
     *
     * @return
     */
    public abstract View initView();

    /**
     * 子类绑定数据的时候重写
     */
    public void initData() {

    }
}
