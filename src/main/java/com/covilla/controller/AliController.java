package com.covilla.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 支付宝回调
 * Created by qmaolong on 2016/11/24.
 */
@Controller
@RequestMapping("/ali")
public class AliController {

    /**
     * 支付宝授权回调页
     * @param app_id
     * @param app_auth_code
     * @param model
     * @return
     */
    @RequestMapping("authCallBack")
    public String authCallBack(String app_id, String app_auth_code, Model model){
        model.addAttribute("appId", app_id);
        model.addAttribute("authCode", app_auth_code);
        return "bk/ali-auth";
    }
}
