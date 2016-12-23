package com.rudy.framework.base.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RudyJun on 2016/12/23.
 */

public abstract class CommonAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public CommonAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }
}
