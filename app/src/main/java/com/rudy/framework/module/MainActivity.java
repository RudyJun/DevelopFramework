package com.rudy.framework.module;

import android.os.Bundle;

import com.rudy.framework.R;
import com.rudy.framework.base.view.BaseActivity;

/**
 * Created by RudyJun on 2016/11/23.
 */

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void initViews() {
        showLoading("");
    }
}
