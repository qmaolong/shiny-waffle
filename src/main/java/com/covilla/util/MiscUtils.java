package com.covilla.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author christ
 * @date 2015/1/28
 */
public class MiscUtils {

    /**
     * 转map
     *
     * @param data
     * @return
     */
    public static Map toMap(Object... data) {
        if (data.length % 2 == 1) {
            IllegalArgumentException e = new IllegalArgumentException("You must pass an even sized array to the toMap method (size = " + data.length + ")");
            throw e;
        }
        Map map = new HashMap();
        for (int i = 0; i < data.length; ) {
            map.put(data[i++], data[i++]);
        }
        return map;
    }

}
