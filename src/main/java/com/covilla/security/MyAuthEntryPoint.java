package com.covilla.security;

import com.alibaba.fastjson.JSONObject;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.ResultMsg;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.*;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户未登录错误处理
 * Created by qmaolong on 2017/3/20.
 */
public class MyAuthEntryPoint implements AuthenticationEntryPoint, InitializingBean {
    private PortMapper portMapper = new PortMapperImpl();
    private PortResolver portResolver = new PortResolverImpl();
    private String loginFormUrl;
    private boolean useForward = false;

    public MyAuthEntryPoint(String loginFormUrl) {
        Assert.notNull(loginFormUrl, "loginFormUrl cannot be null");
        this.loginFormUrl = loginFormUrl;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(StringUtils.hasText(this.loginFormUrl) && UrlUtils.isValidRedirectUrl(this.loginFormUrl), "loginFormUrl must be specified and must be a valid redirect URL");
        if(this.useForward && UrlUtils.isAbsoluteUrl(this.loginFormUrl)) {
            throw new IllegalArgumentException("useForward must be false if using an absolute loginFormURL");
        } else {
            Assert.notNull(this.portMapper, "portMapper must be specified");
            Assert.notNull(this.portResolver, "portResolver must be specified");
        }
    }

    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authException) throws IOException, ServletException {
        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
        //如果是ajax请求
        if (isAjax) {
            ResultMsg errorMsg = ResultMsg.buildFailMsg("-1", "登录失效~");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().print(JSONObject.toJSONString(errorMsg));
        } else {
            SavedRequest savedRequest = (SavedRequest)req.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if (ValidatorUtil.isNotNull(savedRequest)){
                req.setAttribute("redirectUrl", savedRequest.getRedirectUrl());
            }
            req.getRequestDispatcher(loginFormUrl).forward(req, resp);
        }
    }
}
