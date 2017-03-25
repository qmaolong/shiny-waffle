package com.covilla.controller.wx;

import com.covilla.service.card.CardService;
import com.covilla.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by qmaolong on 2017/1/14.
 */
@Controller
@RequestMapping("/wx")
public class IndexWxController {
    @Autowired
    private UserService userService;
    @Autowired
    private CardService cardService;

    @RequestMapping("recommend")
    public String recommend(HttpServletRequest request, Model model){
        return "/wx/index/recommend";
    }

    @RequestMapping("msg")
    public String msg(HttpServletRequest request, Model model){
        model.addAttribute("message", request.getAttribute("message"));
        model.addAttribute("msgType", request.getAttribute("msgType"));
        return "wx/msg";
    }
}
