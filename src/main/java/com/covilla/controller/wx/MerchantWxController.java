package com.covilla.controller.wx;

import com.covilla.common.Config;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.service.user.UserService;
import com.covilla.util.ValidatorUtil;
import com.covilla.util.wechat.entity.user.UserWeiXin;
import com.covilla.util.wechat.service.WXUserService;
import com.covilla.vo.ResultMsg;
import jodd.typeconverter.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by qmaolong on 2017/1/4.
 */
@Controller
@RequestMapping("/wx/user")
public class MerchantWxController {
    private Logger logger = LoggerFactory.getLogger(MerchantWxController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;

    /**
     * 商家中心
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("merchant")
    public String merchant(HttpServletRequest request, Model model){
        String openId = Convert.toString(request.getAttribute("openId"));
        User user = userService.findByOpenId(openId, false);
        if (ValidatorUtil.isNull(user)){
            return "redirect:bindMerchant";
        }
        model.addAttribute("user", user);

        List<Shop> shopList = shopService.findShopsByUser(user);
        model.addAttribute("shopList", shopList);
        return "wx/user/merchant";
    }

    @RequestMapping("bindMerchant")
    public String bindMerchant(HttpServletRequest request, Model model){
        return "wx/user/bind-merchant";
    }

    /**
     * 绑定后台账号
     * @param name
     * @param password
     * @param request
     * @return
     */
    @RequestMapping("bindMerchantSubmit")
    @ResponseBody
    public ResultMsg bindMerchantSubmit(String name, String password, HttpServletRequest request) throws Exception{
        String openId = (String)request.getAttribute("openId");
        User userByOpenId = userService.findByOpenId(openId, false);
        if (ValidatorUtil.isNotNull(userByOpenId)){
            return ResultMsg.buildFailMsg("-1", "该微信号已绑定其他账号~");
        }
        User user = userService.checkPassword(password, name);
        if (ValidatorUtil.isNull(user)){
            return ResultMsg.buildFailMsg("-1", "用户名或密码错误");
        }else if(ValidatorUtil.isNotNull(user.getOpenId())){
            return ResultMsg.buildFailMsg("-1", "该后台账户已绑定其他微信");
        }else {
            try {
                UserWeiXin userWeiXin = WXUserService.getUserInfo(openId, Config.WEIXIN_APPID, Config.WEIXIN_APPSECRET);
                userService.bindUser(userWeiXin, user);
            }catch (ServiceException se){
                return ResultMsg.buildFailMsg("-1", se.getMessage());
            }catch (Exception e){
                logger.error(e.toString());
                return ResultMsg.buildFailMsg("-1", "系统错误~");
            }
        }
        return ResultMsg.buildSuccessMsg();
    }

    /**
     * 解绑后台账号
     * @param request
     * @return
     */
    @RequestMapping("unbindMerchant")
    @ResponseBody
    public ResultMsg unbindMerchant(HttpServletRequest request){
        String openId = (String)request.getAttribute("openId");
        User user = userService.findByOpenId(openId, false);
        if (ValidatorUtil.isNull(user)){
            return ResultMsg.buildFailMsg("-1", "该微信未绑定后台账号");
        }
        try {
            userService.unBindWeixin(user.get_id());
            return ResultMsg.buildSuccessMsg();
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
    }

    /**
     * 关于我们
     * @return
     */
    @RequestMapping("about")
    public String about(){
        return "wx/about";
    }

    @RequestMapping("user")
    public String user(HttpServletRequest request, Model model){
        String openId = Convert.toString(request.getAttribute("openId"));
        User user = userService.findByOpenId(openId, true);
        model.addAttribute("user", user);
        return "wx/user/user";
    }

}
