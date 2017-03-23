package com.covilla.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.covilla.common.APIConstants;
import com.covilla.common.Config;
import com.covilla.util.ValidatorUtil;
import com.covilla.util.httpclient.HttpClientUtil;
import com.covilla.util.wechat.entity.AccessToken;
import com.covilla.util.wechat.entity.user.UserWeiXin;
import com.covilla.util.wechat.service.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
public class WeiXinApi {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinApi.class);

    private static final Long TICKET_TIMEOUT = 7000L; //ticket超时时长(秒)，为防止ticket失效，超时时间设置为7000秒

    /**
     * @description: 获得网页授权的openId
     *
     * @author xuys
     *
     * @time 2015年3月11日
     *
     * @param code 授权码
     */
    public static String getAuth2OpenId(String code){
        String requestUrl = APIConstants.WEI_XIN_AUTH2_ACCESS_URL + "&appid=" + Config.WEIXIN_APPID + "&secret=" + Config.WEIXIN_APPSECRET + "&code=" + code;
        logger.info(requestUrl);
        String result = HttpClientUtil.get(requestUrl);
        logger.info("auth.code:" + code);
        logger.info("auth.result:" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);

        AccessToken accessToken = null;
        if (ValidatorUtil.isNotNull(jsonObject)) {
            String openId = jsonObject.getString("openid");
            logger.info("auth.openId:" + openId);

            return openId;
        }
        return null;
    }

    /**
     * 获取网页授权用户信息
     * @param code
     * @return
     */
    public static UserWeiXin getAuthUserInfo(String code){
        String requestUrl = APIConstants.WEI_XIN_AUTH2_ACCESS_URL + "&appid=" + Config.WEIXIN_APPID + "&secret=" + Config.WEIXIN_APPSECRET + "&code=" + code;
        String result = HttpClientUtil.get(requestUrl);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if (ValidatorUtil.isNotNull(jsonObject)) {
            String openId = jsonObject.getString("openid");
            String accessToken = jsonObject.getString("access_token");
            return OAuthService.getUserInfoOauth(accessToken, openId);
        }
        return null;
    }
}
