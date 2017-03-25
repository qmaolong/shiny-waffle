package com.covilla.security;

import com.covilla.common.Constant;
import com.covilla.model.mongo.user.User;
import com.covilla.util.SpringSecurityUtil;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录成功处理
 * Created by qmaolong on 2017/3/17.
 */
public class AuthSuccessHandlerImpl implements AuthenticationSuccessHandler {
    private String successUrl;

    public AuthSuccessHandlerImpl(String successUrl){
        this.successUrl = successUrl;
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        User user = SpringSecurityUtil.getCurrentUser();
        request.getSession().setAttribute(Constant.USER_SESSION, user);
        //原操作shop
        String shopId = request.getParameter("shopId");
        if (ValidatorUtil.isNotNull(shopId)){
            request.getSession().setAttribute(Constant.SHOPID_SESSION, new ObjectId(shopId));
        }
        //返回原操作页面
        String redirectUrl = request.getParameter("redirectUrl");
        if (ValidatorUtil.isNotNull(redirectUrl)){
            request.getSession().removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
            response.sendRedirect(redirectUrl);
        }else {
            response.sendRedirect(request.getContextPath() + successUrl);
        }
    }
}