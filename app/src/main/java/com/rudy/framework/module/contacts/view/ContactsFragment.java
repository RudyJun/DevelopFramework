package com.rudy.framework.module.contacts.view;

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

public class ContactsFragment extends BaseFragment {

    private TextView tvContacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //延迟加载数据，即当fragment可见时才获取数据
        setLazyLoad(true);
        View view = inflater.inflate(R.layout.fragment_contacts, null);
        tvContacts = (TextView) view.findViewById(R.id.tvContacts);
        return view;
    }

    @Override
    public void fetchData() {
        tvContacts.setText("获取数据后的通讯录");
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
