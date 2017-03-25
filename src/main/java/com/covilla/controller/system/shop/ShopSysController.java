package com.covilla.controller.system.shop;

import com.covilla.common.Constant;
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

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by qmaolong on 2016/10/7.
 */
@Controller
@RequestMapping("sys/system/shop")
public class ShopSysController {
    Logger logger = LoggerFactory.getLogger(ShopSysController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;

    /**
     * 门店列表
     */
    @RequestMapping("shops")
    public String shops(){
        return "system/shop/shops";
    }

    @RequestMapping("getShops")
    @ResponseBody
    public List<Shop> getShops(){
        return shopService.findAllShops();
    }

    @RequestMapping("shopInput")
    public String shopInput(String oper, Integer id, Integer userId, Model model){
        if(!"add".equals(oper)){
            Shop shop = shopService.findById(id);
            User owner = userService.findBy_id(shop.getOwner());
            model.addAttribute("owner", owner);
        }
        model.addAttribute("oper", oper);
        model.addAttribute("userId", userId);
        return "system/shop/shop-input";
    }

    /**
     * 编辑门店信息提交
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editShop")
    @ResponseBody
    public ResultMsg editShop(String dataStr, String oper, HttpSession session){
        User user = (User)session.getAttribute(Constant.USER_SESSION);
        try {
            shopService.editShopBySys(dataStr, oper, user.get_id().toString());
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
        return ResultMsg.buildSuccessMsg();
    }
}
