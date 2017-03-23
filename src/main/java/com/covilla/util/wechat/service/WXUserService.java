package com.covilla.util.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.covilla.util.wechat.entity.user.UserWeiXin;
import com.covilla.util.wechat.util.DateFormart;
import com.covilla.util.wechat.util.StringUtil;
import com.covilla.util.wechat.util.WeixinUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WXUserService {
	public static Logger log = Logger.getLogger(WXUserService.class);

	public static String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	public static String GET_USER_OPENID_LIST = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";

	// ======================��ȡ�û���Ϣ=================================
	public static UserWeiXin getUserInfo(String openid, String appId,
			String appSecret) {
		String token = WeixinUtil.getToken(appId, appSecret);

		UserWeiXin user = null;

		if (token != null) {
			String url = GET_USER_INFO.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
			JSONObject jsonObject = WeixinUtil.httpsRequest(url, "POST", null);

			if (jsonObject != null) {
				if ((StringUtil.isNotEmpty(jsonObject.get("errcode")))
						&& (jsonObject.get("errcode") != "0")) {
					log.error("获取用户信息失败 errcode:"
							+ jsonObject.getIntValue("errcode") + "，errmsg:"
							+ jsonObject.getString("errmsg"));
				} else {
					user = new UserWeiXin();
					user.setSubscribe(Integer.valueOf(jsonObject
							.getIntValue("subscribe")));

					if (jsonObject.getIntValue("subscribe") == 1) {
						user.setOpenid(jsonObject.getString("openid"));
						user.setNickname(jsonObject.getString("nickname"));
						user.setSex(Integer.valueOf(jsonObject.getIntValue("sex")));
						user.setCity(jsonObject.getString("city"));
						user.setCountry(jsonObject.getString("country"));
						user.setProvince(jsonObject.getString("province"));
						user.setLanguage(jsonObject.getString("language"));
						user.setHeadimgurl(jsonObject.getString("headimgurl"));
						long subscibeTime = jsonObject
								.getLong("subscribe_time");
						Date st = DateFormart
								.paserYYYY_MM_DD_HHMMSSToDate(subscibeTime);
						user.setSubscribe_time(st);
					}
				}
			}

		}

		return user;
	}

	// =======================================��ȡ�û�openId�б�==================================
	public static List<String> getUserOpenIdList(String appId, String appSecret) {
		String token = WeixinUtil.getToken(appId, appSecret);
		List<String> list = null;
		if (token != null) {
			String url = GET_USER_OPENID_LIST.replace("ACCESS_TOKEN", token)
					.replace("NEXT_OPENID", "");

			JSONObject jsonObject = WeixinUtil.httpsRequest(url, "POST", null);

			if (jsonObject != null) {
				if ((StringUtil.isNotEmpty(jsonObject.get("errcode")))
						&& (jsonObject.get("errcode") != "0")) {
					log.error("获取关注用户列表失败 errcode:"
							+ jsonObject.getIntValue("errcode") + "，errmsg:"
							+ jsonObject.getString("errmsg"));
				} else {
					list = new ArrayList<String>();
					JSONObject data = jsonObject.getJSONObject("data");
					String openidStr = data.getString("openid");
					openidStr = openidStr.substring(1, openidStr.length() - 1);
					openidStr = openidStr.replace("\"", "");
					String[] openidArr = openidStr.split(",");
					for (int i = 0; i < openidArr.length; i++) {
						list.add(openidArr[i]);
					}
				}
			}
		}

		return list;
	}

	// ====================================��ȡ�û��б?����������������)============================
	public static List<UserWeiXin> getUserList(String appId, String appSecret) {
		List<UserWeiXin> list = new ArrayList<UserWeiXin>();

		List<String> listStr = getUserOpenIdList(appId, appSecret);

		if ((listStr == null) || (listStr.size() == 0)) {
			return null;
		}
		for (int i = 0; i < listStr.size(); i++) {
			UserWeiXin user = getUserInfo((String) listStr.get(i), appId,
					appSecret);
			if (user != null) {
				list.add(user);
			}
		}
		return list;
	}
}
