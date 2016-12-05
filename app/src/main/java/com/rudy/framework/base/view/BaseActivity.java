package com.rudy.framework.base.view;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.FadingCircle;
import com.rudy.framework.FrameWorkApplication;
import com.rudy.framework.R;
import com.rudy.framework.base.AppManager;
import com.rudy.framework.util.SystemBarHelper;
import com.rudy.framework.widget.LoadingDialog;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;

/**
 * Created by RudyJun on 2016/11/23.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 默认主色调为白色, 如果是6.0或者是miui6、flyme4以上, 设置状态栏文字为黑色, 否则给状态栏着色
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) || (SystemBarHelper.isFlyme4Later() || SystemBarHelper.isMIUI6Later())) {
            SystemBarHelper.setStatusBarDarkMode(this);
            SystemBarHelper.tintStatusBar(this, getResources().getColor(R.color.topColor), 0f);
        } else {
            SystemBarHelper.tintStatusBar(this, Color.WHITE);
        }
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initData();
        initViews();
        ProgressBar loadingBar = new ProgressBar(this);
        FadingCircle fadingCircle = new FadingCircle();
        loadingBar.setIndeterminateDrawable(fadingCircle);
    }

    protected abstract void initViews();

    protected void initData() {
    }

    public void showLoading(String tip) {
        showLoading(tip, true, false);
    }

    public void showLoading(String tip, boolean cancelable, boolean touchCancelable) {
        hideLoading();
        if (null == loadingDialog) {
            loadingDialog = new LoadingDialog(this);
        }
        if (null != tip && !tip.trim().equals("")) {
            loadingDialog.setContent(tip);
        }
        loadingDialog.setCancelable(cancelable);
        loadingDialog.setCanceledOnTouchOutside(touchCancelable);
        loadingDialog.show();
    }

    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(FrameWorkApplication.getApplication().isDebug()) {
            RefWatcher refWatcher = FrameWorkApplication.getRefWatcher(this);
            refWatcher.watch(this);
        }
        AppManager.getAppManager().finishActivity(this);
    }
}
