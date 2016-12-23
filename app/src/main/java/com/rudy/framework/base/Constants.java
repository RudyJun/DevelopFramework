package com.rudy.framework.base;

import android.os.Environment;

/**
 * Created by RudyJun on 2016/11/23.
 */
public interface Constants {

    //图片下载目录
    String DOWNLOAD_PHOTO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/rudy/development/pictures/";

    String EMPTY_STRING = "";
    String UNDERLINE = "_";
    String APPLICATION_SETTING = "application_setting";
    String ALLOW_NO_WIFI = "allow_no_wifi";
    String IS_FIRST_INTO_VERSION = "isFirstIntoVersion";
    String SDCARD_NAME = "sdcard_name";
    String SDCARD_PATH = "sdcard_path";
    String PNG_SUFFIX = ".png";

    // 网络类型
    int NETTYPE_NONE = 0;
    int NETTYPE_LINE = 1;
    int NETTYPE_WIFI = 2;
    int NETTYPE_2G = 3;
    int NETTYPE_3G = 4;
    int NETTYPE_4G = 5;
    String NETWORK_ERROR = "加载失败, 请稍后重试";
    String NO_NETWORK = "当前无可用网络连接, 请检查网络设置";

    // 字节数
    int KB = 1024;
    int MB = 1024 * KB;
}