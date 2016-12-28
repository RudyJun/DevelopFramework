package com.rudy.framework.util;

import com.rudy.framework.FrameWorkApplication;
import com.rudy.framework.base.Constants;
import com.rudy.framework.base.exception.NetworkDisconnectException;
import com.rudy.framework.base.model.ApiService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by RudyJun on 2016/12/28.
 */

public class RetrofitClient {
    private static OkHttpClient okHttpClient;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS);
        okHttpClient = builder.build();
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * @return 使用base_url, 返回ApiService实例
     */
    public static ApiService getApiService() {
        return getService(Constants.BASE_URL, ApiService.class);
    }

    /**
     * @param url 自定义url
     * @return 返回ApiService实例
     */
    public static ApiService getApiService(String url) {
        return getService(url, ApiService.class);
    }

    /**
     * @param clazz
     * @param <T>
     * @return 返回Service实例
     */
    public static <T> T getApiService(Class<T> clazz) {
        return getService(Constants.BASE_URL, clazz);
    }

    /**
     * @param url   自定义url
     * @param clazz
     * @param <T>
     * @return 返回Service实例
     */
    public static <T> T getService(String url, Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }


    public static <T> T getResult(Call<T> call) throws IOException, NetworkDisconnectException {
        if (FrameWorkApplication.getApplication().getNetworkType() == Constants.NETTYPE_NONE) {
            throw new NetworkDisconnectException("Network unavailable");
        }
        Response<T> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException("Unexpected code " + response.code());
        }
    }
}
