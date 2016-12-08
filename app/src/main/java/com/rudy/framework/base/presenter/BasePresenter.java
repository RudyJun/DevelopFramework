package com.rudy.framework.base.presenter;

import android.os.Handler;
import android.os.Looper;

import com.rudy.framework.base.view.IView;

/**
 * Created by RudyJun on 2016/12/8.
 */

public class BasePresenter<T extends IView> {

    protected T view;

    public void attachView(T view){
        this.view =  view;
    }

    public void onDestory() {
        view = null;
    }

    public void hideLoading() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (null != view) {
                    view.hideLoading();
                }
            }
        });
    }
}
