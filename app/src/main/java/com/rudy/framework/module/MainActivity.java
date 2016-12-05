package com.rudy.framework.module;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rudy.framework.R;
import com.rudy.framework.base.view.BaseActivity;
import com.rudy.framework.base.view.PreviewPictureActivity;

import butterknife.BindView;

/**
 * Created by RudyJun on 2016/11/23.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.btScan)
    Button btScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this ,PreviewPictureActivity.class);
                intent.putExtra("isFromGrowAlbum" , false);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void initViews() {
    }
}
