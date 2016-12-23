package com.rudy.framework.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.rudy.framework.FrameWorkApplication;
import com.rudy.framework.base.Constants;
import com.rudy.framework.util.NetUtil;

/**
 * Created by RudyJun on 2016/12/23.
 */

public class NetworkReceiver extends BroadcastReceiver {
    //当网络断开时，此广播接受到两次，为避免重复提示，将这次网络状态与上次的对比，不同的话
    //再进行提示，即相同网络只提示一次
    private static int state = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            FrameWorkApplication.getApplication().setNetworkType(NetUtil.getNetWorkType(context));
            int networkType = FrameWorkApplication.getApplication().getNetworkType();
            if (FrameWorkApplication.getApplication().isDebug() && networkType != state) {
                String netWorkState = "";
                switch (networkType) {
                    case Constants.NETTYPE_NONE:
                        netWorkState = "当前网络连接不可用";
                        break;

                    case Constants.NETTYPE_2G:
                        netWorkState = "网络连接变更为2G";
                        break;

                    case Constants.NETTYPE_3G:
                        netWorkState = "网络连接变更为3G";
                        break;

                    case Constants.NETTYPE_4G:
                        netWorkState = "网络连接变更为4G";
                        break;

                    case Constants.NETTYPE_WIFI:
                        netWorkState = "网络连接变更为WIFI";
                        break;

                    default:
                        break;
                }
                Toast.makeText(context, netWorkState, Toast.LENGTH_SHORT).show();

            }
            state = networkType;
        }
    }
}
