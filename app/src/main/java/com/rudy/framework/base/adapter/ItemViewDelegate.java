package com.rudy.framework.base.adapter;

public interface ItemViewDelegate<T>
{

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(RecycleViewHolder holder, T t, int position);


}