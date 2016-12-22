package com.rudy.framework.module.index.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rudy.framework.R;
import com.rudy.framework.base.presenter.BasePresenter;
import com.rudy.framework.base.view.BaseFragment;

/**
 * Created by RudyJun on 2016/12/22.
 */

public class IndexFragment extends BaseFragment {
    private TextView tvIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, null);
        tvIndex = (TextView) view.findViewById(R.id.tvIndex);
        return view;
    }

    @Override
    public void fetchData() {
        tvIndex.setText("获取数据后的首页");
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
