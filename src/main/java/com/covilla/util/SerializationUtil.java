package com.covilla.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * 对象与json转化类
 * Created by qmaolong on 2017/1/16.
 */
public class SerializationUtil {

    /**
     * 序列化对象
     * @param o
     * @return
     */
    public static String serializeObject(Object o){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(o);
            return json;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 反序列化对象
     * @param string
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T>T deSerializeObject(String string, Class<T> clazz){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);//忽略目标对象没有的属性
            T object = mapper.readValue(string, clazz);
            return object;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 反序列化List对象
     * @param string
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T>List<T> deSerializeList(String string, Class<T> clazz){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);//忽略目标对象没有的属性
            List<T> result = mapper.readValue(string, getCollectionType(mapper, List.class, clazz));
            return result;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取泛型的Collection Type
     * @param collectionClass 泛型的Collection
     * @param elementClasses 元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    private static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
