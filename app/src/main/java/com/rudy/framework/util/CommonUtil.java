package com.rudy.framework.util;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
/**
 * Created by RudyJun on 2016/11/23.
 */
public final class CommonUtil {

    private static final String TAG = CommonUtil.class.getSimpleName();
    // 校验手机号码正则 2015年
    private static Pattern phPattern = Pattern.compile("((1[38][0-9])|(15[012356789])|(14[57])|(17[678]))\\d{8}");

    private static final long ONE_MINUTE = 60000L;

    private static final long ONE_HOUR = 3600000L;

    private static final long ONE_DAY = 86400000L;

    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";

    private static final String ONE_MINUTE_AGO = "分钟前";

    private static final String ONE_HOUR_AGO = "小时前";

    private static final String ONE_DAY_AGO = "天前";

    private static final String ONE_MONTH_AGO = "月前";

    private static final String ONE_YEAR_AGO = "年前";

    private CommonUtil() {

    }

    public static String getVersionName(Context context) {
        String pkName = context.getPackageName();
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Get versionName or versionCode failed.", e);
            return "";
        }
        return versionName;
    }


    /**
     * 校验是否为合法手机号码
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (StringUtil.isEmpty(phoneNumber)) {
            return false;
        }
        return phPattern.matcher(phoneNumber).matches();
    }

    /**
     * 获取手机uuid
     * 6.0系统需要获取权限
     */
    public static String getUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = tm.getDeviceId();
        String tmSerial = tm.getSimSerialNumber();
        tmSerial = StringUtil.isEmpty(tmSerial) ? "" : tmSerial;
        String androidId =
                android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        Log.d("debug", "uuid=" + uniqueId);
        return uniqueId;
    }

    /**
     * MD5加密
     */
    public final static String getMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String format(Date date) {
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    /**
     * 显示提示
     */
    public static void showToast(Context context, String message) {
        if (null == context) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    public static void recycleBitmap(List<Bitmap> bitmapList) {
        if (!CollectionUtil.isEmpty(bitmapList)) {
            for (Bitmap bitmap : bitmapList) {
                if (null != bitmap && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            System.gc();
        }
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断底部是否有虚拟按键, 原理是获取手机屏幕大小， 再获取手机真实显示屏幕大小，对比得出
     */
    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size;
    }

    /**
     * 是否有显示导航栏
     */
    public static boolean hasNavBar(Resources resources) {
        try {
            int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            return id > 0 && resources.getBoolean(id);
        } catch (Throwable e) {
            Log.e(TAG, "Check if exists Navigation Bar failed. exception:" + e.toString(), e);
            return false;
        }
    }


    /**
     * 获得当前进程的名字
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        if (null == activityManager){
            return null;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 处理地址省份信息
     * 直辖市：北京、上海、重庆、天津
     * 自治区： 广西壮族自治区、内蒙古自治区、新疆维吾尔自治区、西藏自治区、宁夏自治区
     * 特别行政区: 香港特别行政区、澳门特别行政区
     * 直辖市统一加上市, 自治区统一去掉自治区
     * 特别行政区统一去掉行政区
     *
     * @param province
     * @return
     */
    public static String procAddrProvince(String province) {
        if (StringUtil.isEmpty(province)) {
            return province;
        }

        if (province.contains("北京")) {
            return "北京市";
        } else if (province.contains("上海")) {
            return "上海市";
        } else if (province.contains("重庆")) {
            return "重庆市";
        } else if (province.contains("天津")) {
            return "天津市";
        } else if (province.contains("广西")) {
            return "广西";
        } else if (province.contains("内蒙古")) {
            return "内蒙古";
        } else if (province.contains("新疆")) {
            return "新疆";
        } else if (province.contains("西藏")) {
            return "西藏";
        } else if (province.contains("宁夏")) {
            return "宁夏";
        } else if (province.contains("香港")) {
            return "香港";
        } else if (province.contains("澳门")) {
            return "澳门";
        } else if (province.contains("台湾")) {
            return "台湾";
        }
        return province;
    }


    /**
     * @param dateTime
     */
    public static String formatOffsetDays(DateTime dateTime) {
        return formatOffsetDays(new LocalDateTime().toDateTime(), dateTime);
    }

    public static String formatOffsetDays(DateTime beginTime, DateTime endTime) {
        if (null == endTime) {
            return "";
        }
        int days = Days.daysBetween(new LocalDate(beginTime.getMillis()), new LocalDate(endTime.getMillis())).getDays();
        if (days == 0){
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
            return new LocalDateTime(endTime.getMillis()).toString(timeFormatter);
        } else if (days == 1){
            return "明天";
        } else if (days == 2){
            return "后天";
        } else if (days > 2){
            int weekIndex = new LocalDateTime(endTime.getMillis()).getDayOfWeek();
            switch (weekIndex){
                case DateTimeConstants.MONDAY:
                    return "周一";
                case DateTimeConstants.TUESDAY:
                    return "周二";
                case DateTimeConstants.WEDNESDAY:
                    return "周三";
                case DateTimeConstants.THURSDAY:
                    return "周四";
                case DateTimeConstants.FRIDAY:
                    return "周五";
                case DateTimeConstants.SATURDAY:
                    return "周六";
                case DateTimeConstants.SUNDAY:
                    return "周日";
            }
        }
        return "";
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

}
