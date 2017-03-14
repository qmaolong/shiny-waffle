package com.nick.web;

import com.nick.repository.TestRepository;
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
    private TestRepository testRepository;

    @RequestMapping("/index")
    public String index(Model model){
        System.out.print(testRepository.findOne(1));
        model.addAttribute("name", "Nick");
        return "index";
    }
}
