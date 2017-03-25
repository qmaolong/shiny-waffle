package com.covilla.common;

import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Properties;

public class Config {
	private static Logger logger = LoggerFactory.getLogger(Config.class);

	public static String SYS_DEV_MODE = "";//开发环境，由监听器InitConfigListner赋值

	public static String SYS_DOMAIN_NAME = "";//域名
	public static String WX_DOMAIN_NAME = "";
	public static String WEIXIN_APPID = "";//微信appid
	public static String WEIXIN_APPSECRET = "";
	public static String WEIXIN_TOKEN = "";
	public static String WEIXIN_MERCHANT_ID = "";
	public static String WEIXIN_PAY_SIGN_KEY = "";
	public static String WEIXIN_CERT_LOCATION = "";

	public static String ALI_APPID = "";
	public static String ALI_PUBLIC_KEY = "";
	public static String ALI_PRIVATE_KEY = "";

	public static String STATIC_LOCATION = "";

	public static ObjectId SUPER_ADMIN_ID;//超级管理员id
	public static ObjectId MODOULE_SHOP_ID;//门店模板id
	public static ObjectId DATA_SHOP_ID;//数据门店id

	static{
		load();
	}

	private static void load(){
		Properties properties = new Properties();
		try {
			String configFilePath = "";
			SYS_DEV_MODE = System.getProperty("spring.profiles.active");
			if (ValidatorUtil.isNull(SYS_DEV_MODE)){
				SYS_DEV_MODE = Constant.SYS_ENV_DEV;
			}
			if(Constant.SYS_ENV_PRODUCT.equals(SYS_DEV_MODE)){
				configFilePath = "config.pro.properties";//生产环境配置文件
			}else {
				configFilePath = "config.dev.properties";//开发环境配置文件
			}
			String configFile = URLDecoder.decode(Config.class.getResource("/").getPath(), "UTF-8").replaceAll("file:/", "") + configFilePath;
			InputStreamReader inputStream =  new InputStreamReader(new FileInputStream(configFile), "utf-8");
			properties.load(inputStream);
		} catch(Exception e){
			logger.error("读取配置文件出错"+e.getMessage(),e);
		}

		init(properties);
	}

	/**
	 * @description: 初始化系统变量
	 *
	 * @author xuys
	 *
	 * @time 2015年3月11日
	 *
	 * @param
	 */
	private static void init(Properties properties) {
		SYS_DOMAIN_NAME = properties.getProperty("sys.domain.name");
		WX_DOMAIN_NAME = properties.getProperty("wx.domain.name");
		WEIXIN_APPID = properties.getProperty("sys.wxapp.appid");
		WEIXIN_APPSECRET = properties.getProperty("sys.wxapp.appsecret");
		WEIXIN_TOKEN = properties.getProperty("sys.wxapp.apptoken");
		WEIXIN_MERCHANT_ID = properties.getProperty("sys.wxapp.mchid");
		WEIXIN_PAY_SIGN_KEY = properties.getProperty("sys.wxapp.key");
		try {
			WEIXIN_CERT_LOCATION = URLDecoder.decode(Config.class.getResource("/").getPath(), "UTF-8").replaceAll("file:/", "") + "/cert/apiclient_cert.p12";
		}catch (Exception e){
			logger.error("读取微信支付证书路径失败");
		}

		ALI_APPID = properties.getProperty("sys.ali.appid");
		ALI_PUBLIC_KEY = properties.getProperty("sys.ali.publicKey");
		ALI_PRIVATE_KEY = properties.getProperty("sys.ali.privateKey");

		STATIC_LOCATION = properties.getProperty("sys.static.location");

		SUPER_ADMIN_ID = new ObjectId(properties.getProperty("sys.super.admin.id"));
		MODOULE_SHOP_ID = new ObjectId(properties.getProperty("sys.module.shop.id"));
		DATA_SHOP_ID = new ObjectId(properties.getProperty("sys.data.shop.id"));
	}

}
