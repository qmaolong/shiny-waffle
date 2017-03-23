package com.covilla.controller.wx;

import com.covilla.cache.UserCache;
import com.covilla.model.mongo.user.User;
import com.covilla.service.card.CardTypeService;
import com.covilla.service.user.UserService;
import jodd.typeconverter.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 1 on 2016/7/21.
 */
@Controller
@RequestMapping("/wx/customer")
public class CustomerController {
    @Autowired
    private CardTypeService cardTypeService;
    @Autowired
    private UserCache userCache;
    @Autowired
    private UserService userService;

    protected CardTypeService getBaseService(){
        return cardTypeService;
    }

    @RequestMapping("/customerCenter")
    public String cardList(HttpServletRequest request, Model model){
        String openId = Convert.toString(request.getAttribute("openId"));
        User user = userService.findByOpenId(openId, true);
        model.addAttribute("user", user);
        return "wx/customer/customer-center";
    }


}
