package com.covilla.interceptor;

import com.covilla.common.Config;
import com.covilla.common.Constant;
import com.covilla.common.RoleEnum;
import com.covilla.controller.api.WeiXinApi;
import com.covilla.model.mongo.user.User;
import com.covilla.service.user.UserService;
import com.covilla.util.CookieUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.util.wechat.entity.user.UserWeiXin;
import com.covilla.util.wechat.service.WXUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenIdInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
		String openId = CookieUtil.findCookie(Constant.OPENID_COOKIE, request);
		if(ValidatorUtil.isNull(openId) && ValidatorUtil.isNotNull(request.getPart("code"))){
			String code = request.getParameter("code");
			openId = WeiXinApi.getAuth2OpenId(code);
			CookieUtil.addCookie(Constant.OPENID_COOKIE, openId, response);
		}
//		openId = "oUqqRwSsfIL38mkueXYfrrqEFCNc";//测试代码

		if (ValidatorUtil.isNull(openId)){
			request.setAttribute("message", "获取微信用户信息失败");
			request.setAttribute("msgType", "error");
			request.getRequestDispatcher("/wx/msg").forward(request,response);
			return false;
		}
		request.setAttribute("openId", openId);
		//判断是否已存在微信用户
		User user = userService.findByOpenId(openId, true);
		if (ValidatorUtil.isNull(user)){
			UserWeiXin userWeiXin = WXUserService.getUserInfo(openId, Config.WEIXIN_APPID, Config.WEIXIN_APPSECRET);
			if (ValidatorUtil.isNotNull(userWeiXin)){
				user = new User();
				user.setNickName(userWeiXin.getNickname());
				user.setOpenId(openId);
				user.setHeadImgUrl(userWeiXin.getHeadimgurl());
				user.setCity(userWeiXin.getCity());
				user.setProvince(userWeiXin.getProvince());
				user.setSex(userWeiXin.getSex());
				user.setSubscribe(userWeiXin.getSubscribe());
				user.setSubscribe_time(userWeiXin.getSubscribe_time());
				user.setId(userService.generateDocumentId("user"));
				user.setRole(RoleEnum.customer.getCode());
				userService.insert(user);
			}
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

}
