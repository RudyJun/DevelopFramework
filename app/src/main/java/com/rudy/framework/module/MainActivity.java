package com.rudy.framework.module;

import android.os.Bundle;
import android.widget.TextView;

import com.rudy.framework.R;
import com.rudy.framework.base.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RudyJun on 2016/11/23.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.tvTest)
    TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
    @Override
    protected void initViews() {

    }
}
