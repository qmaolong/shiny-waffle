package com.covilla.controller.bk.account;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.RoleEnum;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.service.user.UserService;
import com.covilla.util.ContentUtil;
import com.covilla.util.SpringSecurityUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.util.qrcode.QrCodeUtil;
import com.covilla.util.wechat.entity.user.UserWeiXin;
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
 * 账号管理
 * Created by qmaolong on 2016/9/30.
 */
@Controller
@RequestMapping("merchant/admin/account")
public class AccountController {
    private Logger logger = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;

    /**
     * 管理员列表
     * @return
     */
    @RequestMapping("managers")
    public String managers(){
        return "bk/user/managers";
    }

    /**
     * 获取管理员列表
     * @param session
     * @return
     */
    @RequestMapping("getManagerList")
    @ResponseBody
    public List<User>  getManagerList(HttpSession session){
        User user = SpringSecurityUtil.getCurrentUser();
        if(!RoleEnum.owner.getCode().equals(user.getRole())){
            return null;
        }
        List<User> result = userService.getShopManagersByCreator(user.get_id());
        return result;
    }

    @RequestMapping("managerInput")
    public String managerInput(String oper, HttpSession session, Model model){
        User user = SpringSecurityUtil.getCurrentUser();
        List<Shop> managerShops = shopService.findShopsByUser(user);
        model.addAttribute("shops", managerShops);
        model.addAttribute("oper", oper);
        return "bk/user/manager-input";
    }

    /**
     * 编辑管理员
     * @param dataStr
     * @param oper
     * @return
     */
    @RequestMapping("editManager")
    @ResponseBody
    @AOPLogAnnotation(description = "系统管理员")
    public ResultMsg editManager(String dataStr, String oper){
        try {
            userService.editUser(dataStr, oper, RoleEnum.manager);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    /**
     * 用户信息
     * @return
     */
    @RequestMapping("accountInfo")
    public String userInfo(HttpSession session, Model model){
        User user = SpringSecurityUtil.getCurrentUser();
        model.addAttribute("user", user);
        return "bk/user/account-info";
    }

    /**
     * 重新绑定微信号
     * @param session
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("reBindWX")
    public String reBindWX(HttpSession session, Model model){
        session.removeAttribute(Constant.WX_REBIND_USER);
        String callBackUrl = ContentUtil.toAuthUrl(Constant.WX_RE_BIND_URL + session.getId());
        model.addAttribute("qrcodeUrl", QrCodeUtil.generateCodeToString(callBackUrl, "png"));
        return "bk/user/re-bind-wx";
    }

    /**
     * 获取重新绑定状态
     * @param session
     * @return
     */
    @RequestMapping("getRebindState")
    @ResponseBody
    public ResultMsg getRebindState(HttpSession session, Integer option){
        User user = (User) session.getAttribute(Constant.USER_SESSION);
        UserWeiXin userWeiXin = (UserWeiXin)session.getAttribute(Constant.WX_REBIND_USER);
        if(ValidatorUtil.isNull(userWeiXin)){
            return null;
        }else {
            session.removeAttribute(Constant.WX_REBIND_USER);
        }
        try {
            if (option==1){//解绑
                if(!user.getOpenId().equals(userWeiXin.getOpenid())){
                    return ResultMsg.buildFailMsg("-1", "不是当前绑定的微信");
                }
                userService.unBindWeixin(user.get_id());
            }else if(option==2){
                User existUser = userService.findByOpenId(userWeiXin.getOpenid(), false);
                if (ValidatorUtil.isNotNull(existUser)){
                    return ResultMsg.buildFailMsg("-1", "绑定失败，该微信已绑定其他账号");
                }
                userService.bindUser(userWeiXin, user);
            }
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        //更新缓存的用户信息
        user = userService.findBy_id(user.get_id());
        session.setAttribute(Constant.USER_SESSION, user);
        return ResultMsg.buildSuccessMsg();
    }

    /*@RequestMapping("reBindSubmit")
    @ResponseBody
    public ResultMsg reBindSubmit(HttpSession session){
        User user = (User)session.getAttribute(Constant.USER_SESSION);
        UserWeiXin oldUser = (UserWeiXin)session.getAttribute(Constant.WX_REBIND_OLD_OPENID);
        UserWeiXin newUser = (UserWeiXin)session.getAttribute(Constant.WX_REBIND_NEW_OPENID);
        try {
            userService.reBindWX(oldUser, newUser);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        session.removeAttribute(Constant.WX_REBIND_NEW_OPENID);
        session.removeAttribute(Constant.WX_REBIND_OLD_OPENID);
        return ResultMsg.buildSuccessMsg();
    }*/

    @RequestMapping("changePassword")
    public String changePassword(){
        return "bk/user/change-password";
    }

    @RequestMapping("changePasswordSubmit")
    @ResponseBody
    @AOPLogAnnotation(description = "修改密码")
    public ResultMsg changePasswordSubmit(String password, String newPassword, HttpSession session){
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            User loginUser = userService.checkPassword(password, user.getName());
            if(ValidatorUtil.isNull(loginUser)){
                return ResultMsg.buildFailMsg("-1", "旧密码错误！");
            }
            User newUser = userService.changePassword(newPassword, user);
            session.setAttribute(Constant.USER_SESSION, newUser);
            return ResultMsg.buildSuccessMsg();
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
    }
}
