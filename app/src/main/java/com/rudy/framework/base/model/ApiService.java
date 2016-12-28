package com.rudy.framework.base.model;

import com.rudy.framework.module.index.model.entity.PhoneQuery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by RudyJun on 2016/12/28.
 */

public interface ApiService {

    @GET("mobilenumber")
    Call<PhoneQuery> phoneInvoices(@Header("apikey") String apikey, @Query("phone") String phone);

}
