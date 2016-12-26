package com.rudy.framework.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;


/**
 * Created by RudyJun on 2016/12/26.
 */

public class JsonUtil {

    /**
     * 把JSON文本parse为JavaBean
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T json2obj(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }


    /**
     * 把JSON文本parse成JavaBean集合
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> json2list(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }


    /**
     * 将JavaBean序列化为JSON文本
     *
     * @param object
     * @return
     */
    public static String obj2json(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 将JavaBean、Map、Collection序列化为带格式的JSON文本
     *
     * @param object
     * @param prettyFormat
     * @return
     */
    public static String obj2jsonWithFormat(Object object, boolean prettyFormat) {
        return JSON.toJSONString(object, prettyFormat);
    }

    /**
     * 将JavaBean、Map、Collection转换为JSONObject或者JSONArray。
     *
     * @param object
     * @return
     */
    public static Object obj2JSON(Object object) {
        return JSON.toJSON(object);
    }

    /**
     * 把JSON文本parse为JSONObject或者JSONArray
     *
     * @param json
     * @return
     */
    public static Object json2JSON(String json) {
        return JSON.parse(json);
    }

    /**
     * 把JSON文本parse成JSONObject
     *
     * @param json
     * @return
     */
    public static JSONObject json2JSONObject(String json) {
        return JSON.parseObject(json);
    }

    /**
     * 把JSON文本parse成JSONArray
     *
     * @param json
     * @return
     */
    public static JSONArray json2JSONArray(String json) {
        return JSON.parseArray(json);
    }
}
