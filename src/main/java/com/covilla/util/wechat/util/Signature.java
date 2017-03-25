package com.covilla.util.wechat.util;

import com.covilla.common.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by qmaolong on 2016/11/22.
 */
public class Signature {
    public static String getSign(Map<String,Object> map){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + Config.WEIXIN_PAY_SIGN_KEY;
        result = MD5.MD5Encode(result).toUpperCase();
        return result;
    }
}
