package com.emptypockets.spacemania.utils;

import java.util.Map;

/**
 * Created by jenfield on 12/05/2015.
 */
public class MapUtils {

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
