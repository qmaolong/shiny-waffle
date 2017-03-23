package com.covilla.controller.system.user;

import com.covilla.common.RoleEnum;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.service.user.UserService;
import com.covilla.vo.ResultMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by qmaolong on 2016/10/7.
 */
@Controller
@RequestMapping("sys/system/user")
public class UserSysController {
    Logger logger = LoggerFactory.getLogger(UserSysController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;

    /**
     * 商家列表
     * @return
     */
    @RequestMapping("merchants")
    public String merchants(){
        return "system/user/merchants";
    }

    //获取商家列表
    @RequestMapping("getMerchants")
    @ResponseBody
    public List<User> getMerchants(){
        return userService.getMerchants();
    }

    //编辑商家页面
    @RequestMapping("merchantInput")
    public String merchantInput(String oper, Model model){
        model.addAttribute("oper", oper);
        return "system/user/merchant-input";
    }

    //编辑提交
    @RequestMapping("merchantEdit")
    @ResponseBody
    public ResultMsg merchantEdit(String dataStr, String oper){
        try {
            userService.editUser(dataStr, oper, RoleEnum.owner);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
        return ResultMsg.buildSuccessMsg();
    }

    //商家门店
    @RequestMapping("merchantShops")
    public String merchantShops(Integer id, Model model){
        model.addAttribute("userId", id);
        return "system/user/merchant-shops";
    }

    /**
     * 获取列表
     * @param id
     * @return
     */
    @RequestMapping("getShopsByUser")
    @ResponseBody
    public List<Shop> getShopsByUser(Integer id){
        User merchant = userService.findById(id);
        return shopService.findShopsByUser(merchant);
    }
}
