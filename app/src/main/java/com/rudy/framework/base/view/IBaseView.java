package com.rudy.framework.base.view;

/**
 * Created by RudyJun on 2016/12/8.
 */

public interface IBaseView {

    void showLoading(String tip);
    void showLoading(String tip , boolean cancelable , boolean touchCancelable);
    void hideLoading();

    void showToast(String message);
}
