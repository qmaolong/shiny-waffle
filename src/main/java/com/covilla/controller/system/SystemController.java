package com.covilla.controller.system;

import com.covilla.common.Constant;
import com.covilla.common.RoleEnum;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.shop.ShopService;
import com.covilla.util.SpringSecurityUtil;
import com.covilla.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by qmaolong on 2016/8/29.
 */
@Controller
@RequestMapping("sys")
public class SystemController {
    @Autowired
    private ShopService shopService;

    /**
     * 系统管理员登录
     * @return
     */
    @RequestMapping(value = {"", "/", "login"})
    public String login(){
        return "system/login-sys";
    }

    /**
     * 主界面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("main")
    public String main(HttpSession session, Model model){
        User user = SpringSecurityUtil.getCurrentUser();
        if(ValidatorUtil.isNull(user)|| (!RoleEnum.admin.getCode().equals(user.getRole())&&!RoleEnum.superAdmin.getCode().equals(user.getRole()))){
            return "redirect:login";
        }

        //门店模板id
        Shop shop = shopService.findModuleShop();
        session.setAttribute(Constant.SHOPID_SESSION, shop.get_id());

        model.addAttribute("user", user);
        return "system/main-sys";
    }

    @RequestMapping("index.js.map")
    @ResponseBody
    public String indexJsMap(){
        return "";
    }
}
