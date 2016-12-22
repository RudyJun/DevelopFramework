package com.rudy.framework.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by RudyJun on 2016/12/22.
 * 可设置ViewPager是否可滑动
 */

public class ScrollViewPager extends ViewPager {

    private boolean canScroll;

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (canScroll) {
            return super.onTouchEvent(arg0);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (canScroll) {
            return super.onInterceptTouchEvent(arg0);
        }
        return false;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }
}
