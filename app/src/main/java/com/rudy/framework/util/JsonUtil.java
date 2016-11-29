package com.rudy.framework.util;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rudy.framework.base.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Created by RudyJun on 2016/11/23.
 */
public class JsonUtil {
    /**
     * 解决JsonObject optString bug
     * 当key对应的值为null时({name:null})，JsonObject.optString返回的为"null"字符串
     */
    public static String optString(JSONObject jsonObject, String key) {
        return (null == jsonObject || jsonObject.isNull(key))
                ? Constants.EMPTY_STRING : jsonObject.optString(key, Constants.EMPTY_STRING);
    }

    /**
     * 同上
     */
    public static String optString(JSONArray jsonArray, int index) {
        return (null == jsonArray || jsonArray.isNull(index))
                ? Constants.EMPTY_STRING : jsonArray.optString(index, Constants.EMPTY_STRING);
    }

    public static JSONObject getJsonFromStrExceptionNull(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            return jsonObject;
        } catch (JSONException e) {
            return null;
        }
    }

    private static ObjectMapper objectMapper = null;

    static {
        // 将objectMapper 设置为全局静态缓存，提高调用效率
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }


    /**
     * 供外部调用OjbectMapper对象<br>
     */
    public static ObjectMapper getObjectMapper() {

        return objectMapper;
    }

    /**
     * 将对象转为angular json串<br>
     */
    public static String obj2angular(Object obj, String callback) {

        String result = obj2json(obj);

        if (callback != null && callback.length() > 0 && result != null) {
            return callback + "(" + result + ")";
        }

        return result;
    }

    /**
     * 将对象转为json串<br>
     */
    public static String obj2json(Object obj) {

        if (obj == null) {
            return null;
        }

        String jsonResult = null;

        try {
            jsonResult = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonResult;
    }

    /**
     * 从json串中获取某key对应的值<br>
     */
    public static String getJsonValue(String jsonSrc, String jsonKey) {
        if (TextUtils.isEmpty(jsonSrc) || TextUtils.isEmpty(jsonKey)) {
            return null;
        }
        JsonNode node = JsonUtil.json2obj(jsonSrc, JsonNode.class);

        if (node == null) {
            return null;
        }

        // 获取jsonKey数据
        JsonNode dataNode = node.get(jsonKey);

        if (null == dataNode) {
            return null;
        }

        return dataNode.toString();
    }

    /**
     * 从json串中获取某key对应的值<br>
     */
    public static boolean getJsonAsBool(String jsonSrc, String jsonKey) {
        JsonNode node = JsonUtil.json2obj(jsonSrc, JsonNode.class);

        // 获取jsonKey数据
        JsonNode dataNode = node.get(jsonKey);

        return dataNode.asBoolean();
    }

    /**
     * 将json串转为对象<br>
     */
    public static <T> T json2obj(String jsonStr, Class<T> clazz) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        T t = null;

        try {
            t = objectMapper.readValue(jsonStr, clazz);
        } catch (IOException e) {

        }
        return t;
    }

    /**
     * 将一般对象转为jsonNode<br>
     */
    public static JsonNode obj2node(Object obj) {

        if (null == obj) {
            return null;
        }

        JsonNode node = null;

        try {
            node = objectMapper.readTree(obj2json(obj));
        } catch (IOException e) {
        }

        return node;
    }

    public static <T> T obj2T(Object obj, Class<T> clazz) {

        if (null == obj) {
            return null;
        }

        T t = null;

        try {
            t = objectMapper.readValue(obj2json(obj), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

    /**
     * 将jsonNode转为一般对象<br>
     */
    public static <T> T node2obj(JsonNode node, Class<T> clazz) {
        if (null == node) {
            return null;
        }

        return json2obj(obj2json(node), clazz);
    }

    /**
     * 将json串转为map对象<br>
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {

        // 入參校驗
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        Map<String, Map<String, Object>> map = null;
        try {
            map = objectMapper.readValue(jsonStr,
                    new TypeReference<Map<String, T>>() {
                    });
        } catch (IOException e) {

        }

        // 非空校验
        if (CollectionUtil.isEmpty(map)) {
            return null;
        }

        Map<String, T> result = new HashMap<String, T>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * jsonnode 转list对象<br>
     */
    public static <T> List<T> node2list(JsonNode jsonNode, Class<T> clazz) {
        if (null == jsonNode || !jsonNode.isArray()) {
            return null;
        }

        List<T> tList = new ArrayList<T>();

        for (Iterator<JsonNode> nodeIt = jsonNode.iterator(); nodeIt.hasNext(); ) {
            JsonNode node = nodeIt.next();
            T t = node2obj(node, clazz);

            tList.add(t);
        }

        return tList;
    }

    /**
     * 字符串转map，并且map的value为list<br>
     */
    public static <T> Map<String, List<T>> json2mapWithList(String jsonStr, Class<T> clazz) {

        // 入参校验
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        Map<String, Map<String, List<Object>>> map = null;
        try {
            map = objectMapper.readValue(jsonStr,
                    new TypeReference<Map<String, List<T>>>() {
                    });
        } catch (IOException e) {

        }

        // 非空校验
        if (CollectionUtil.isEmpty(map)) {
            return null;
        }

        Map<String, List<T>> result = new HashMap<String, List<T>>();

        for (Map.Entry<String, Map<String, List<Object>>> entry : map.entrySet()) {

            List<T> tList = new ArrayList<T>();

            @SuppressWarnings("unchecked")
            List<Object> obList = (List<Object>) entry.getValue();

            for (Object o : obList) {
                T t = objectMapper.convertValue(o, clazz);
                tList.add(t);
            }

            result.put(entry.getKey(), tList);

        }
        return result;
    }

    /**
     * 将json串转为list对象<br>
     */
    public static <T> List<T> json2list(String jsonStr, Class<T> clazz) {

        // 入參校驗
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        List<Map<String, Object>> list = null;
        try {
            list = objectMapper.readValue(jsonStr,
                    new TypeReference<List<T>>() {
                    });
        } catch (IOException e) {

        }

        // 非空校验
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }

        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }

    /**
     * map convert to javaBean
     */
    private static <T> T map2pojo(@SuppressWarnings("rawtypes")
                                          Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * 基本数据类型转换成list
     */
    public static <T> List<T> simpleJson2list(String jsonStr, Class<T> clazz) {
        try {
            List<T> list = objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            return list;
        } catch (JsonParseException e) {
            return null;
        } catch (JsonMappingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * json字符转为JsonNode对象<br>
     */
    public static JsonNode json2node(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            return null;
        }
    }
}
