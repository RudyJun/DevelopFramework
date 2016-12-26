package com.rudy.framework.module.index.model;

import android.util.Log;

import com.rudy.framework.base.exception.NetworkDisconnectException;
import com.rudy.framework.module.index.model.entity.PhoneQuery;
import com.rudy.framework.util.HttpClient;
import com.rudy.framework.util.JsonUtil;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by RudyJun on 2016/12/8.
 */

public class ExampleService {

    public PhoneQuery getPhoneQueryResult(String url) throws IOException, NetworkDisconnectException {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("apikey", "563d6ca2b02ce6756724c8f0d89a4b8d");

        String result = HttpClient.getJson(url, hashMap);
        Log.e("ExampleService", result);

        PhoneQuery phoneQuery = JsonUtil.json2obj(result, PhoneQuery.class);
        return phoneQuery;
    }

}
