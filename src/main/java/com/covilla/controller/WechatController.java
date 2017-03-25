package com.covilla.controller;

import com.covilla.common.Config;
import com.covilla.common.Constant;
import com.covilla.util.MySecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
@Controller
@RequestMapping("wechat")
public class WechatController {
    private static final Logger logger = LoggerFactory.getLogger(WechatController.class);
    /**
     * @description 校验请求是否来自微信服务器
     * @author xuys
     * @time:2015-3-9 上午10:54:09
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return String
     */
    public String checkJoin(String signature, String timestamp, String nonce,
                            String echostr) {
        // 重写totring方法，得到三个参数的拼接字符串
        List<String> list = new ArrayList<String>(3) {
            private static final long serialVersionUID = 2622344383666420433L;

            public String toString() {
                return this.get(0) + this.get(1) + this.get(2);
            }
        };

        list.add(Config.WEIXIN_TOKEN);
        list.add(timestamp);
        list.add(nonce);
        Collections.sort(list);// 排序

        String postStr = new MySecurity().encode(list.toString(),
                MySecurity.SHA_1);// SHA-1加密

        return postStr;
    }

    /**
     * @description 处理微信发送过来的请求
     * @author xuys
     * @time:2015-3-9 上午10:53:02
     * @param request
     * @param response
     * @return String
     */
    @RequestMapping(value = "/connection")
    @ResponseBody
    public String handle(HttpServletRequest request,
                         HttpServletResponse response) {
        String postStr = null;
        System.out.print("receivied a message");
        String method = request.getMethod();
        try {
            if (Constant.HTTP_GET.equals(method)) { // 校验请求是否来自微信服务器
                String signature = request.getParameter("signature");
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");
                String echostr = request.getParameter("echostr");
                postStr = checkJoin(signature, timestamp, nonce, echostr);

                Writer out = response.getWriter();
                if (signature.equals(postStr)) {
                    out.write(echostr);// 请求验证成功，返回随机码
                } else {
                    out.write("");
                }
                out.flush();
                out.close();
            } else if (Constant.HTTP_POST.equals(method)) {
                handleMsg(request, response);
            }
        } catch (IOException e) {
            logger.error("处理微信请求失败！" + e.getMessage());
        }
        return postStr;
    }

    /**
     * 处理Post请求
     * @param request
     * @param response
     */
    public void handleMsg(HttpServletRequest request, HttpServletResponse response){
        logger.info("收到请求");
    }
}
