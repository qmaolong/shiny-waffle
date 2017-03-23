package com.nick.web;

import com.nick.model.Test;
import com.nick.model.User;
import com.nick.repository.jpa.TestRepository;
import com.nick.repository.mongo.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by qmaolong on 2017/2/27.
 */
@Controller
public class IndexController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private TestRepository testRepository;

    @RequestMapping("/index")
    public String index(Model model){
        User user = userDao.findByName("covilla");
        Test test = testRepository.findByName("long");
        model.addAttribute("name", user.getName() + "," + test.getName());
        return "index";
    }
}
