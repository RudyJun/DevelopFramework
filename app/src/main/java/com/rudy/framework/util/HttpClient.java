package com.rudy.framework.util;

import android.os.Build;
import android.util.Log;

import com.rudy.framework.FrameWorkApplication;
import com.rudy.framework.base.Constants;
import com.rudy.framework.base.exception.NetworkDisconnectException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.text.Html.escapeHtml;
/**
 * Created by RudyJun on 2016/11/23.
 */
public class HttpClient {

    private static String TAG = "HttpClient";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient okHttpClient;


    static {
        File sdcache = FileUtil.getCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        okHttpClient = builder.build();
    }


    /**
     * 获取OkHttpClient实例
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static String getJson(String url) throws NetworkDisconnectException, IOException {
        if (FrameWorkApplication.getApplication().getNetworkType() == Constants.NETTYPE_NONE) {
            throw new NetworkDisconnectException("Network unavailable");
        }
        Request request = commonRequestBuilder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String contentType = response.header("Content-Type");
        if (contentType != null && contentType.contains("application\\json")) {
            return response.body().string();
        } else {
            return response.body().string();
        }
    }

    /**
     * 增加header
     * @param url
     * @param headerMap
     */
    public static String getJson(String url , Map<String , String> headerMap) throws NetworkDisconnectException, IOException {
        if (FrameWorkApplication.getApplication().getNetworkType() == Constants.NETTYPE_NONE) {
            throw new NetworkDisconnectException("Network unavailable");
        }
        Set<Map.Entry<String, String>> entries = headerMap.entrySet();
        Request.Builder builder = commonRequestBuilder();
        for (Map.Entry<String, String> entry : entries) {
            builder.addHeader(entry.getKey() , entry.getValue());
            Log.e(TAG , "key = "+entry.getKey() + "  value = "+entry.getValue());
        }

        Request request = builder
                .url(url)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String contentType = response.header("Content-Type");
        if (contentType != null && contentType.contains("application\\json")) {
            return response.body().string();
        } else {
            return response.body().string();
        }
    }

    public static String post(String url, String json) throws NetworkDisconnectException, IOException {
        if (FrameWorkApplication.getApplication().getNetworkType() == Constants.NETTYPE_NONE) {
            throw new NetworkDisconnectException("Network unavailable");
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = commonRequestBuilder()
                .url(url)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String contentType = response.header("Content-Type");
        if (contentType != null && contentType.contains("application\\json")) {
            return response.body().string();
        } else {
            return escapeHtml(response.body().string());
        }
    }

    public static String put(String url, RequestBody formBody) throws NetworkDisconnectException, IOException {
        if (FrameWorkApplication.getApplication().getNetworkType() == Constants.NETTYPE_NONE) {
            throw new NetworkDisconnectException("Network unavailable");
        }
        Request request = commonRequestBuilder()
                .url(url)
                .put(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }

    public static String delete(String url) throws NetworkDisconnectException, IOException {
        if (FrameWorkApplication.getApplication().getNetworkType() == Constants.NETTYPE_NONE) {
            throw new NetworkDisconnectException("Network unavailable");
        }
        Request request = commonRequestBuilder()
                .url(url)
                .delete()
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }

    public static String post(String url, RequestBody formBody) throws NetworkDisconnectException, IOException {
        if (FrameWorkApplication.getApplication().getNetworkType() == Constants.NETTYPE_NONE) {
            throw new NetworkDisconnectException("Network unavailable");
        }
        Request request = commonRequestBuilder()
                .url(url)
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }

    /**
     * 获取下载文件的大小
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static long getFileLength(String url) throws NetworkDisconnectException, IOException {
        Request request = commonRequestBuilder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        call.cancel();
        return response.body().contentLength();
    }

    public static void download(String url, String filePath) throws NetworkDisconnectException, IOException {
        if (FrameWorkApplication.getApplication().getNetworkType() == Constants.NETTYPE_NONE) {
            throw new NetworkDisconnectException("Network unavailable");
        }
        Request request = commonRequestBuilder().url(url)
//                .addHeader("X-CSRFToken", csrftoken)
                .addHeader("Content-Type", "application/json").build();
        Response response = okHttpClient.newCall(request).execute();

        InputStream in = response.body().byteStream();
        FileOutputStream out = new FileOutputStream(filePath);
        byte b[] = new byte[1024];
        int j = 0;
        while ((j = in.read(b)) != -1) {
            out.write(b, 0, j);
        }
    }

    private static Request.Builder commonRequestBuilder() {
        return new Request.Builder()
                .addHeader("x-platform", "Android")
                .addHeader("x-platform-version", Build.VERSION.RELEASE)
                .addHeader("x-version", SettingUtil.getVersion())
                .addHeader("User-Agent", "Android/" + Build.VERSION.RELEASE + " FrameWork/" + SettingUtil.getVersion());
    }

}
