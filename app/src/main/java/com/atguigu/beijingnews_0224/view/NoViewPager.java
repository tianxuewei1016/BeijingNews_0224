package com.atguigu.beijingnews_0224.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：田学伟 on 2017/6/3 09:13
 * QQ：93226539
 * 作用：
 */

public class NoViewPager extends ViewPager{
    public NoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 触摸事件把事件消耗
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;//事件消费掉
    }

    /**
     * 把事件传递给孩子
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
