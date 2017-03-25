package com.covilla.controller.system.account;

import com.covilla.common.Constant;
import com.covilla.common.RoleEnum;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qmaolong on 2016/10/8.
 */
@Controller
@RequestMapping("sys/system/account")
public class AccountSysController {
    private Logger logger = LoggerFactory.getLogger(AccountSysController.class);
    @Autowired
    private UserService userService;

    /**
     * 系统管理员
     * @return
     */
    @RequestMapping("managers")
    public String managers(HttpSession session, Model model){
        User user = (User)session.getAttribute(Constant.USER_SESSION);
        model.addAttribute("user", user);
        return "system/account/managers";
    }

    /**
     * 获取列表
     * @return
     */
    @RequestMapping("getManagers")
    @ResponseBody
    public List<User> getManagers(HttpSession session){
        User user = (User)session.getAttribute(Constant.USER_SESSION);
        if(RoleEnum.admin.getCode().equals(user.getRole())){
            List<User> result = new ArrayList<User>();
            result.add(user);
            return result;
        }
        return userService.getSysManagersByCreator();
    }

    /**
     * 编辑页
     * @return
     */
    @RequestMapping("managerInput")
    public String managerInput(String oper, Model model){
        model.addAttribute("oper", oper);
        return "system/account/manager-input";
    }

    /**
     * 编辑提交
     * @param dataStr
     * @param oper
     * @return
     */
    @RequestMapping("editManager")
    @ResponseBody
    public ResultMsg editManager(String dataStr, String oper){
        try {
            userService.editUser(dataStr, oper, RoleEnum.admin);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }
}
