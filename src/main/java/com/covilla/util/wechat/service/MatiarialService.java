package com.covilla.util.wechat.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.covilla.util.SerializationUtil;
import com.covilla.util.wechat.entity.customer.Article;
import com.covilla.util.wechat.util.WeixinUtil;

import java.util.List;

/**
 * Created by qmaolong on 2017/1/10.
 */
public class MatiarialService {
    public static final String MATIARIAL_COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=ACCESS_TOKEN";
    public static final String GET_MATIARIALS = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";

    public static JSONObject getMatiarialCount(String accessToken){
        JSONObject jsonObject = WeixinUtil.httpsRequest(MATIARIAL_COUNT.replace("ACCESS_TOKEN", accessToken), "GET", null);
        return jsonObject;
    }

    public static JSONObject getMatiarialCount(String appId, String appSecret){
        String token = WeixinUtil.getToken(appId, appSecret);
        return getMatiarialCount(token);
    }

    public static List<Article> getMatiarials(String accessToken, Integer offset, Integer count){
        JSONObject params = new JSONObject();
        params.put("type", "news");
        params.put("offset", offset);
        params.put("count", count);
        JSONObject jsonObject = WeixinUtil.httpsRequest(GET_MATIARIALS.replace("ACCESS_TOKEN", accessToken), "POST", params.toJSONString());
        JSONArray news = jsonObject.getJSONArray("item");
        return SerializationUtil.deSerializeList(news.toJSONString(), Article.class);
    }

    public static List<Article> getMatiarials(String appId, String appSecret, Integer offset, Integer count){
        String token = WeixinUtil.getToken(appId, appSecret);
        return getMatiarials(token, offset, count);
    }
}
