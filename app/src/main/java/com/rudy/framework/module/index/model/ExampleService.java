package com.rudy.framework.module.index.model;

import com.rudy.framework.base.exception.NetworkDisconnectException;
import com.rudy.framework.base.model.ApiService;
import com.rudy.framework.module.index.model.entity.PhoneQuery;
import com.rudy.framework.util.RetrofitClient;

import java.io.IOException;

/**
 * Created by RudyJun on 2016/12/8.
 */

public class ExampleService {

    public PhoneQuery getPhoneQueryResult(String url, String phone) throws IOException, NetworkDisconnectException {
        String apikey = "563d6ca2b02ce6756724c8f0d89a4b8d";
        ApiService apiService = RetrofitClient.getService(url, ApiService.class);
        return RetrofitClient.getResult(apiService.phoneInvoices(apikey, phone));
    }

}
