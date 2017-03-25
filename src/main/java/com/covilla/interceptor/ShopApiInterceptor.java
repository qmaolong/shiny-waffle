package com.covilla.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.covilla.common.Constant;
import com.covilla.model.mongo.user.User;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.BaseApiResultMsg;
import org.bson.types.ObjectId;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by qmaolong on 2017/1/13.
 */
public class ShopApiInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Constant.USER_SESSION);
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        //判断登录是否过期
        if(ValidatorUtil.isNull(user) || ValidatorUtil.isNull(shopId)){
            BaseApiResultMsg errorMsg = BaseApiResultMsg.buildErrorMsg("-1", "登录失效，请重新登录~");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(JSONObject.toJSONString(errorMsg));
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
