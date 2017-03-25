package com.covilla.util.wechat.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.covilla.common.Config;
import com.covilla.util.SpringContextUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.util.wechat.entity.AccessToken;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class WeixinUtil {
	public static Logger log = Logger.getLogger(WeixinUtil.class);
	public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	// =================================Get Access_Token==============================================
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		//先从数据库获取
		MongoTemplate mongoTemplate = (MongoTemplate) SpringContextUtil.getBean("mongoTemplate");
		Query query = new Query(Criteria.where("_id").is(Config.WEIXIN_APPID));
		List<AccessToken> tokens = mongoTemplate.find(query, AccessToken.class, "wx_access_token");
		if (ValidatorUtil.isNotNull(tokens) && ValidatorUtil.isNotNull(tokens.get(0).getExpireAt()) && new Date().before(tokens.get(0).getExpireAt())){
			return tokens.get(0);
		}

		String requestUrl = String.format(ACCESS_TOKEN, appid, appsecret);
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);

		if (jsonObject != null) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
			} catch (JSONException e) {
				accessToken = null;

				log.error("获取token失败 errcode:" + jsonObject.getIntValue("errcode")
						+ "，errmsg:" + jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	// ============================================Get Token=========================================
	public static String getToken(String appId, String appSecret) {
		AccessToken at = getAccessToken(appId, appSecret);
		if (at != null) {
			return at.getToken();
		}
		return null;
	}

	// ======================================(IMPORTANT) Get http REQUEST==========================================================
	public static JSONObject httpsRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			if (outputStr != null) {
				OutputStream outputStream = httpUrlConn.getOutputStream();

				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:", e);
		}
		return jsonObject;
	}

	// =============================Format Time===============================================================================
	public static String formatTime(String createTime) {
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}
}
