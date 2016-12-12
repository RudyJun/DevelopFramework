package com.rudy.framework.module;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rudy.framework.R;
import com.rudy.framework.base.presenter.BasePresenter;
import com.rudy.framework.base.view.BaseActivity;
import com.rudy.framework.base.view.PreviewPictureActivity;
import com.rudy.framework.module.index.ExampleActivity;

import butterknife.BindView;

/**
 * Created by RudyJun on 2016/11/23.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.btScan)
    Button btScan;

    @BindView(R.id.btQuery)
    Button btQuery;

    private long mLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PreviewPictureActivity.class);
                intent.putExtra("isFromGrowAlbum", false);
                startActivity(intent);
            }
        });

        btQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExampleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastTime < 2000) {
            super.onBackPressed();
        } else {
            mLastTime = currentTime;
            showToast("再按一次退出应用");
        }
    }
}
