package com.rudy.framework.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.rudy.framework.FrameWorkApplication;
import com.rudy.framework.R;
import com.rudy.framework.base.Constants;
/**
 * Created by RudyJun on 2016/11/23.
 */
public class SettingUtil {
    private static Context context = FrameWorkApplication.getApplication().getApplicationContext();

    private static String version = context.getString(R.string.can_not_find_version_name);

    private static String version_code = "100";

    static {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
        }
    }

    public static SharedPreferences getSettingSharedPreferences() {
        return FrameWorkApplication.getApplication().getSharedPreferences(Constants.APPLICATION_SETTING + Constants.UNDERLINE + getVersion(),
                Activity.MODE_PRIVATE);
    }

    /**
     * @param allow_no_wifi true: 允许非wifi
     *                      false: 不允许非WiFi
     */
    public static void setAllowNoWifi(boolean allow_no_wifi) {
        SharedPreferences settingSharedPreferences = getSettingSharedPreferences();
        SharedPreferences.Editor editor = settingSharedPreferences.edit();
        editor.putBoolean(Constants.ALLOW_NO_WIFI, allow_no_wifi);
        editor.commit();
    }

    /**
     * @return allow_no_wifi
     */
    public static boolean getAllowNoWifi() {
        return getSettingSharedPreferences().getBoolean(Constants.ALLOW_NO_WIFI, true);
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        return version;
    }


    /**
     * @param isFirstIntoVersion ：是否是第一次进入该版本
     */
    public static void setFirstFlag(boolean isFirstIntoVersion) {
        SharedPreferences settingSharedPreferences = getSettingSharedPreferences();
        SharedPreferences.Editor editor = settingSharedPreferences.edit();
        editor.putBoolean(Constants.IS_FIRST_INTO_VERSION + "_" + version_code, isFirstIntoVersion);
        editor.commit();
    }

    /**
     * @return isFirstIntoVersion
     */
    public static boolean getFirstFlag() {
        return getSettingSharedPreferences().getBoolean(Constants.IS_FIRST_INTO_VERSION + "_" + version_code, true);
    }

    /**
     * @param sdcard_path
     */
    public static void setSdcard(String sdcard_name, String sdcard_path) {
        SharedPreferences settingSharedPreferences = getSettingSharedPreferences();
        SharedPreferences.Editor editor = settingSharedPreferences.edit();
        editor.putString(Constants.SDCARD_NAME, sdcard_name);
        editor.putString(Constants.SDCARD_PATH, sdcard_path);
        editor.commit();
    }
}