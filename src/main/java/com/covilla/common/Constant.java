package com.covilla.common;

/**
 * @todo:常量类
 *
 */
public class Constant {
	/** 数据状态值  **/
	public static final Integer DATA_STATUS_VALID = 0;
	public static final Integer DATA_STATUS_INVALID = 1;

	/**
	 * http请求方式
	 */
	public static String HTTP_GET = "GET"; //get
	public static String HTTP_POST = "POST"; //post

	/**
	 * 运行环境
	 */
	public static final String SYS_ENV_DEV = "development"; //开发环境
	public static final String SYS_ENV_PRODUCT = "production"; //生产环境

	/**
	 * cache key值
	 */
	public static final String SYS_CACHE_KEY_WXTOKEN = "sys_cache_key_wxtoken"; //微信接口调用凭证
	public static final String SYS_CACHE_KEY_JSAPI = "sys_cache_key_jsapi"; //微信js api接口票据缓存key值

	public static final String OPENID_COOKIE = "openIdCookie";

	public static final String USER_SESSION = "userSession";//User实体缓存
	public static final String SHOPID_SESSION = "shopIdSession";
	public static final String SHOP_SUPER_FLAG = "shopSuperFlag";
	public static final String WX_USER_SESSION = "wxUserSession";//微信用户信息
	public static final String API_INDEX_SESSION = "apiIndexSession";
	public static final String API_RANDOM_SESSION = "apiRandomSession";

	public static final String WX_REDIRECT_URL = Config.WX_DOMAIN_NAME + "/redir?url=";//微信鉴权回调中转地址
	public static final String WX_LOGIN_URL = Config.SYS_DOMAIN_NAME + "/merchant/wxLogin?loginCode=";//微信扫码登录回调地址
	public static final String WX_REGIST_URL = Config.SYS_DOMAIN_NAME + "/merchant/wxRegist?sessionId=";//微信注册扫码回调地址
	public static final String WX_RE_BIND_URL = Config.SYS_DOMAIN_NAME + "/merchant/rebindCallBack?sessionId=";//微信修改绑定回调地址

	public static final String WX_REGIST_USER = "wxRegistUser";
	public static final String WX_REBIND_USER = "wxRebindUser";

	public static final String ALI_PAY_AUTH_CALLBACK = Config.SYS_DOMAIN_NAME + "/ali/authCallBack";

	public static final String API_AUTH_URL = Config.SYS_DOMAIN_NAME.replace("http", "https") + "/covilla/payAuth";
}
