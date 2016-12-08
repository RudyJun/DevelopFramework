package com.rudy.framework;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.rudy.framework.base.Constants;
import com.rudy.framework.util.HttpClient;
import com.rudy.framework.util.NetUtil;
import com.rudy.framework.util.StringUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;

/**
 * Created by RudyJun on 2016/11/23.
 */

public class FrameWorkApplication extends Application {

    /**
     * 单例
     */
    private static FrameWorkApplication application;
    // app缓存目录
    private String appCacheDir;
    // 最大缓存数
    private long maxCacheSize = 200L * Constants.MB;

    private static int networkType = Constants.NETTYPE_NONE;

    private RefWatcher refWatcher;

    // 是否是debug模式 // TODO 上线需改为 false
    private boolean isDebug = true;

    /**
     * 返回应用实例
     * @return
     */
    public static FrameWorkApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // 初始化目录
        initCacheDir();
        //初始化Fresco图片加载库
        initFresco();
        // 获取当前网络状态
        networkType = NetUtil.getNetWorkType(this);
        if (FrameWorkApplication.getApplication().isDebug()) {
            refWatcher = LeakCanary.install(this);
        }
    }


    private void initCacheDir() {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                appCacheDir = getExternalCacheDir().getPath();
                if (StringUtil.isEmpty(appCacheDir)) {
                    appCacheDir = getCacheDir().getPath();
                }
            } else {
                appCacheDir = getCacheDir().getPath();
            }
        } catch (Exception e) {
            appCacheDir = getCacheDir().getPath();
        }
    }

    /**
     * 初始化Fresco图片加载库
     */
    private void initFresco() {
        // 图片缓存
        DiskCacheConfig cacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(new File(getAppCacheDir()))
                .setBaseDirectoryName("fresco_cache")
                .setMaxCacheSize(maxCacheSize).build();

        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, HttpClient.getOkHttpClient())
                .setMainDiskCacheConfig(cacheConfig)
                .setDownsampleEnabled(true) // 设置支持PNG图片resize, 否则加载png图片超卡
                .build();

        Fresco.initialize(this, config);
    }

    public static RefWatcher getRefWatcher(Context context) {
        FrameWorkApplication application = (FrameWorkApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public boolean isDebug() {
        return isDebug;
    }


    public String getAppCacheDir() {
        if (StringUtil.isEmpty(appCacheDir)) {
            initCacheDir();
            if (StringUtil.isEmpty(appCacheDir)) {
                return getCacheDir().getPath();
            }
        }
        return appCacheDir;
    }

    public int getNetworkType() {
        return FrameWorkApplication.networkType;
    }
}
