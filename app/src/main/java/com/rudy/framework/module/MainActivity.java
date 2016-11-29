package com.rudy.framework.module;

import android.os.Bundle;
import android.widget.TextView;

import com.rudy.framework.R;
import com.rudy.framework.base.view.BaseActivity;
import com.rudy.framework.widget.CircleImageView;

import butterknife.BindView;

/**
 * Created by RudyJun on 2016/11/23.
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.tvHello)
    TextView tvHello;
    @BindView(R.id.ivPhoto)
    CircleImageView ivPhoto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void initViews() {

        ivPhoto.setImageSrc("http://zykshop.qqjlb.cn/shop/zykshop20161124230935.jpg");
        tvHello.setText("改变內容");
    }
}
