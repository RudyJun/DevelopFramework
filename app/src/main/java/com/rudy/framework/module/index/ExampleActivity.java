package com.rudy.framework.module.index;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rudy.framework.R;
import com.rudy.framework.base.config.RequestUrl;
import com.rudy.framework.base.view.BaseActivity;
import com.rudy.framework.module.index.presenter.ExamplePresenter;
import com.rudy.framework.module.index.service.entity.PhoneQuery;
import com.rudy.framework.module.index.view.IExamplerView;
import com.rudy.framework.util.StringUtil;

import butterknife.BindView;

/**
 * Created by RudyJun on 2016/12/8.
 */

public class ExampleActivity extends BaseActivity<ExamplePresenter> implements IExamplerView{

    @BindView(R.id.etNumber)
    EditText etNumber;

    @BindView(R.id.btQuery)
    Button btQuery;

    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;

    @BindView(R.id.tvBelong)
    TextView tvBelong;

    @BindView(R.id.tvProvince)
    TextView tvProvince;

    @BindView(R.id.tvCity)
    TextView tvCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
    }

    @Override
    protected void initViews() {
        btQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isEmpty(etNumber.getText().toString())){
                    showToast("手机号码不能为空");
                    return;
                }else if(etNumber.getText().toString().length()!=11){
                    showToast("请输入正确的手机号码");
                }
                showLoading("");
                presenter.getPhoneQuery(String.format(RequestUrl.PHONE_QUERY , etNumber.getText().toString()));
            }
        });
    }

    @Override
    protected ExamplePresenter createPresenter() {
        return new ExamplePresenter(this);
    }

    @Override
    public void getPhoneQueryResult(PhoneQuery queryResult) {

        hideLoading();

        if(queryResult.getRetData() != null) {
            tvPhoneNumber.setText(queryResult.getRetData().getPhone());
            tvBelong.setText(queryResult.getRetData().getSupplier());
            tvProvince.setText(queryResult.getRetData().getProvince());
            tvCity.setText(queryResult.getRetData().getCity());
        }else{
            showToast("查询失败");
        }

    }
}
