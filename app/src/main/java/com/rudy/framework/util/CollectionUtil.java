package com.rudy.framework.util;

import java.util.Collection;
import java.util.Map;
/**
 * Created by RudyJun on 2016/11/23.
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }
}
