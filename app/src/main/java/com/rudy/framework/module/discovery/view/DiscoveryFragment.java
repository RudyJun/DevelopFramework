package com.rudy.framework.module.discovery.view;

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

public class DiscoveryFragment extends BaseFragment {

    private TextView tvDiscovery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, null);
        tvDiscovery = (TextView) view.findViewById(R.id.tvDiscovery);
        return view;
    }

    @Override
    public void fetchData() {
        tvDiscovery.setText("获取数据后的发现");
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
