package com.rudy.framework.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

public abstract class RecyclerViewCommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public RecyclerViewCommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(RecycleViewHolder holder, T t, int position) {
                RecyclerViewCommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(RecycleViewHolder holder, T t, int position);

    public void setData(List<T> dataList) {
        if (null != this.mDatas) {
            this.mDatas.clear();
            this.mDatas.addAll(dataList);
        }

    }

    public List<T> getData() {
        return this.mDatas;
    }


    public void insertItemAtPosition(int location, T item) {
        mDatas.add(location, item);
        notifyItemInserted(location);
    }

}