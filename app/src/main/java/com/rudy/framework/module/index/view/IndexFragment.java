package com.rudy.framework.module.index.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rudy.framework.R;
import com.rudy.framework.base.presenter.BasePresenter;
import com.rudy.framework.base.view.BaseFragment;
import com.rudy.framework.base.view.PreviewPictureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RudyJun on 2016/12/22.
 */

public class IndexFragment extends BaseFragment {

    @BindView(R.id.tvIndex)
    TextView tvIndex;

    @BindView(R.id.btScan)
    Button btScan;

    @BindView(R.id.btQuery)
    Button btQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, null);

        ButterKnife.bind(this, view);

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> pathList = new ArrayList<>();
                pathList.add("http://rudy-test.oss-cn-shanghai.aliyuncs.com/android/42.jpg");
                pathList.add("http://rudy-test.oss-cn-shanghai.aliyuncs.com/android/QQ%E6%88%AA%E5%9B%BE20161217144437.png");
                pathList.add("http://pic23.nipic.com/20120830/668573_154825475126_2.jpg");
                pathList.add("http://pic27.nipic.com/20130309/10401505_134039675000_2.jpg");
                pathList.add("http://pic9.nipic.com/20100902/5615113_084913074019_2.jpg");
                pathList.add("http://pic22.nipic.com/20120727/668573_145407326100_2.jpg");
                pathList.add("http://k.zol-img.com.cn/dcbbs/24268/a24267112_01000.jpg");
                pathList.add("http://pic.58pic.com/58pic/13/20/24/67k58PIC6st_1024.jpg");
                PreviewPictureActivity.startActivity(activity, pathList);

            }
        });

        btQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ExampleActivity.class);
                startActivity(intent);
            }
        });
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
