package com.rudy.framework.module.me.view;

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

public class MeFragment extends BaseFragment {

    private TextView tvMe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        tvMe = (TextView) view.findViewById(R.id.tvMe);
        return view;
    }

    @Override
    public void fetchData() {
        tvMe.setText("获取数据后的我的");
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
