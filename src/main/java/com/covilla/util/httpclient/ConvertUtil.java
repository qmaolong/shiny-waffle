package com.covilla.util.httpclient;

import com.covilla.util.wechat.util.StringUtil;
import jodd.typeconverter.Convert;
import org.bson.types.ObjectId;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
public class ConvertUtil {
    public static ObjectId toObjectId(String value){
        if(StringUtil.isEmpty(value)){
            return null;
        }
        return new ObjectId(value);
    }

    public static ObjectId toObjectId(Object object){
        if(null == object){
            return  null;
        }
        return new ObjectId(Convert.toString(object));
    }
}
