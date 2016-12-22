package com.rudy.framework.base.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.rudy.framework.FrameWorkApplication;
import com.rudy.framework.base.presenter.BasePresenter;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by RudyJun on 2016/12/22.
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IBaseView {
    protected String TAG = getClass().getSimpleName();

    protected T presenter;
    protected BaseActivity activity;
    // 是否延迟加载
    protected boolean isLazyLoad = false;
    protected boolean isDataInitiated;
    protected boolean isViewInitiated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        presenter = createPresenter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareFetchData();
    }

    private void prepareFetchData() {
        if ((getUserVisibleHint() || !isLazyLoad) && isViewInitiated && !isDataInitiated) {
            fetchData();
            isDataInitiated = true;
        }

    }

    public void setLazyLoad(boolean lazyLoad) {
        isLazyLoad = lazyLoad;
    }

    public abstract void fetchData();


    protected abstract T createPresenter();

    @Override
    public void onDestroy() {
        if (null != presenter) {
            presenter.onDestory();
        }
        super.onDestroy();
        if (FrameWorkApplication.getApplication().isDebug()) {
            RefWatcher refWatcher = FrameWorkApplication.getRefWatcher(activity);
            refWatcher.watch(this);
        }
    }

    @Override
    public void showLoading(String tip) {
        if (activity != null) {
            activity.showLoading("");
        }

    }

    @Override
    public void showLoading(String tip, boolean cancelable, boolean touchCancelable) {
        if (activity != null) {
            activity.showLoading(tip, cancelable, touchCancelable);
        }
    }

    @Override
    public void hideLoading() {
        if (activity != null) {
            activity.hideLoading();
        }
    }

    @Override
    public void showToast(String message) {
        if (activity != null) {
            activity.showToast(message);
        }
    }
}
