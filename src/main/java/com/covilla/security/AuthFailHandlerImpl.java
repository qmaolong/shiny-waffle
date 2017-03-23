package com.covilla.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by qmaolong on 2017/3/17.
 */
public class AuthFailHandlerImpl implements
        AuthenticationFailureHandler {
    private String redirectUrl;
    public AuthFailHandlerImpl(String redirectUrl){
        this.redirectUrl = redirectUrl;
    }

    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException var3) throws IOException, ServletException {
        req.setAttribute("name", req.getParameter("name"));
        req.setAttribute("password", req.getParameter("password"));
        req.setAttribute("msg", "账号或密码错误");
        req.setAttribute("redirectUrl", req.getParameter("redirectUrl"));
        req.setAttribute("shopId", req.getParameter("shopId"));
        req.getRequestDispatcher(req.getContextPath() + redirectUrl).forward(req, resp);
    }

}