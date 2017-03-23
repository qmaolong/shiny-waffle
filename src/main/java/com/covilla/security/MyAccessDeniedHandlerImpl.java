package com.covilla.security;

import com.alibaba.fastjson.JSONObject;
import com.covilla.vo.ResultMsg;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户无权限异常处理
 * Created by qmaolong on 2017/3/18.
 */
public class MyAccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException reason) throws ServletException, IOException {
        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
        //如果是ajax请求
        if (isAjax) {
            ResultMsg errorMsg = ResultMsg.buildFailMsg("-1", "当前用户没有操作权限~");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().print(JSONObject.toJSONString(errorMsg));
        } else {
            resp.sendError(403);
        }
    }
}