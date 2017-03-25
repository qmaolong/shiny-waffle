package com.covilla.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.covilla.common.Constant;
import com.covilla.util.MySecurity;
import com.covilla.util.ValidatorUtil;
import com.covilla.util.wechat.util.MD5;
import com.covilla.vo.BaseApiResultMsg;
import jodd.typeconverter.Convert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by qmaolong on 2016/11/9.
 */
public class ApiInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        BaseApiResultMsg errorMsg = null;

        HttpSession session = request.getSession();
        Long index = Convert.toLong(session.getAttribute(Constant.API_INDEX_SESSION));
        String randomStr = Convert.toString(session.getAttribute(Constant.API_RANDOM_SESSION));

        //签名认证
        if(ValidatorUtil.isNotNull(index)){
            String apiSign = null;

            Enumeration<String> names =  request.getParameterNames();
            Map<String, Object> attrs = new HashMap<String, Object>();
            while (names.hasMoreElements()){
                String name = names.nextElement();
                if(!"sign".equals(name)){
                    attrs.put(name, request.getParameter(name));
                }else{
                    apiSign = request.getParameter(name);
                }
            }
            attrs.put("index", index);
            String localSign = getSign(attrs, randomStr);
            //认证成功
            if(localSign.equals(apiSign)){
                session.setAttribute(Constant.API_INDEX_SESSION, index+1);
            }else {
                errorMsg = BaseApiResultMsg.buildErrorMsg("1002", "签名失败");
            }
        }else{
            errorMsg = BaseApiResultMsg.buildErrorMsg("1001", "登录失效");
        }

        if (ValidatorUtil.isNotNull(errorMsg)){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(JSONObject.toJSONString(errorMsg));
            return false;
        }else {
            return super.preHandle(request, response, handler);
        }
    }

    private String getSign(Map<String,Object> map, String key) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result = result.substring(0, result.length()-1);
        System.out.println("密钥：" + key);
        System.out.println("签名参数：" + result);
        byte[] sign = MySecurity.HmacSHA1Encrypt(result, key);
        String signStr = MD5.byteArrayToHexString(sign);
        System.out.println("签名：" + signStr);
        System.out.println("--------------------------------");
        return signStr;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)throws Exception {
        // TODO Auto-generated method stub
        super.afterCompletion(request, response, handler, ex);
    }
}
