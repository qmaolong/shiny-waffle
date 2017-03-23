package com.covilla.controller.bk;

import com.covilla.common.Constant;
import com.covilla.common.RoleEnum;
import com.covilla.controller.api.WeiXinApi;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.plugin.rabbitMQ.SmsProducer;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.service.user.UserService;
import com.covilla.util.ContentUtil;
import com.covilla.util.SerializationUtil;
import com.covilla.util.SpringSecurityUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.util.qrcode.QrCodeUtil;
import com.covilla.util.wechat.entity.user.UserWeiXin;
import com.covilla.vo.ResultMsg;
import com.covilla.vo.VerifyCode;
import com.covilla.sms.VerificationCodeMessage;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 商户
 * Created by LongLongAgo on 2016/7/21.
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController{
    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SessionRepository repository;
    @Autowired
    private SmsProducer smsProducer;
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * 登录界面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = {"", "/", "login"})
    public String login(HttpSession session, Model model) throws Exception{
        String loginUrl = ContentUtil.toAuthUrl(Constant.WX_LOGIN_URL + session.getId());
        model.addAttribute("qrcodeUrl", QrCodeUtil.generateCodeToString(loginUrl, "png"));
        return "bk/login";
    }

    /**
     * 轮询获取状态
     * @param session
     * @return
     */
    @RequestMapping("getCurrentState")
    @ResponseBody
    public String getCurrentState(HttpSession session, HttpServletRequest request){
        User user = (User)session.getAttribute(Constant.USER_SESSION);
        UserWeiXin userWeiXin = (UserWeiXin)session.getAttribute(Constant.WX_USER_SESSION);
        if(ValidatorUtil.isNotNull(user)){
            manualLogin(user, request);
            return "success";
        }else if(ValidatorUtil.isNotNull(userWeiXin)){
            return "new";
        }else {
            return "fail";
        }
    }

    /**
     * 微信扫描回调
     * @param code
     * @param loginCode
     * @param model
     * @return
     */
    @RequestMapping("wxLogin")
    public String wxLogin(String code, String loginCode, Model model){
        UserWeiXin userWeiXin = WeiXinApi.getAuthUserInfo(code);
        if(ValidatorUtil.isNull(userWeiXin)){
            model.addAttribute("errorMsg", "扫描失败，请刷新页面重试");
            return "wx/login-bk";
        }
        String openId = userWeiXin.getOpenid();
        com.covilla.model.mongo.user.User user = userService.findByOpenId(openId, false);

        Session session = repository.getSession(loginCode);
        if(ValidatorUtil.isNull(session)){
            model.addAttribute("errorMsg", "扫描失败，请刷新页面重试");
            return "wx/login-bk";
        }
        if(ValidatorUtil.isNotNull(user)){//老用户
            session.setAttribute(Constant.USER_SESSION, user);
        }else {
            session.setAttribute(Constant.WX_USER_SESSION, userWeiXin);
        }
        repository.save(session);
        model.addAttribute("loginCode", loginCode);
        return "wx/login-bk";
    }

    /**
     * 绑定界面
     * @param session
     * @return
     */
    @RequestMapping("bindUserPage")
    public String bindUserPage(HttpSession session, Model model){
        UserWeiXin userWeiXin = (UserWeiXin)session.getAttribute(Constant.WX_USER_SESSION);
        model.addAttribute("userWeiXin", userWeiXin);
        return "bk/bind-user";
    }

    /**
     * 用户绑定提交
     * @param code
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("bindUserSubmit")
    public String bindUserSubmit(String code, HttpSession session, Model model){
        UserWeiXin userWeiXin = (UserWeiXin)session.getAttribute(Constant.WX_USER_SESSION);
        if(ValidatorUtil.isNull(userWeiXin)){
            return "redirect:login";
        }else if(ValidatorUtil.isNull(code)){
            return "bk/regist";
        }
        try {
            User user = userService.findByActiveCode(code);
            user = userService.bindUser(userWeiXin, user);
            session.setAttribute(Constant.USER_SESSION, user);
        }catch (ServiceException se){
            model.addAttribute("warn", se.getMessage());
            return "bk/bind-user";
        }catch (Exception e){
            logger.error(e.getMessage());
            model.addAttribute("warn", e.getMessage());
            return "bk/regist";
        }
        return "redirect:main";
    }

    /**
     * 用户注册试用
     * @return
     */
    @RequestMapping("regist")
    public String regist(){
        return "bk/regist";
    }

    /**
     * 微信注册二维码绑定页
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("registQrcode")
    public String registQrcode(HttpSession session, Model model){
        String sessionId = session.getId();
        String loginUrl = ContentUtil.toAuthUrl(Constant.WX_REGIST_URL + sessionId);
        model.addAttribute("qrcodeUrl", QrCodeUtil.generateCodeToString(loginUrl, "png"));
        return "bk/regist_qrcode";
    }

    /**
     * 注册扫描绑定微信回调
     * @param code
     * @param sessionId
     * @param model
     * @return
     */
    @RequestMapping("wxRegist")
    public String wxRegist(String code, String sessionId, Model model){
        UserWeiXin userWeiXin = WeiXinApi.getAuthUserInfo(code);
        if(ValidatorUtil.isNull(userWeiXin)){
            model.addAttribute("errorMsg", "扫描失败，请刷新页面重试");
            return "wx/login-bk";
        }
        Session session = repository.getSession(sessionId);
        session.setAttribute(Constant.WX_REGIST_USER, userWeiXin);
        repository.save(session);
        return "wx/login-bk";
    }

    /**
     * 注册获取扫描状态
     * @param session
     * @return
     */
    @RequestMapping("getRegistState")
    @ResponseBody
    public ResultMsg getRegistState(HttpSession session){
        UserWeiXin userWeiXin = (UserWeiXin)session.getAttribute(Constant.WX_REGIST_USER);
        if(ValidatorUtil.isNotNull(userWeiXin)){
            User user = userService.findByOpenId(userWeiXin.getOpenid(), false);
            if(ValidatorUtil.isNotNull(user)){
                session.removeAttribute(Constant.WX_REGIST_USER);
                return ResultMsg.buildFailMsg("-1", "该微信已绑定其他账号");
            }else {
                session.removeAttribute(Constant.WX_REGIST_USER);
                return ResultMsg.buildSuccessMsg(userWeiXin);
            }
        }
        return ResultMsg.buildSuccessMsg();
    }

    /**
     * 注册提交
     * @param dataStr
     * @return
     */
    @RequestMapping("registSubmit")
    @ResponseBody
    public ResultMsg registSubmit(String dataStr, String verifyCode, HttpSession session){
        try {
            User user = SerializationUtil.deSerializeObject(dataStr, User.class);
            VerifyCode verifyCodeSession = (VerifyCode) session.getAttribute("verifyCodeSession");
            if (ValidatorUtil.isNull(verifyCode) || ValidatorUtil.isNull(verifyCodeSession)
                    || !verifyCode.equals(verifyCodeSession.getCode()) || !verifyCodeSession.getTel().equals(user.getTel())
                    || !VerifyCode.TypeEnum.REGIST_MERCHANT.getName().equals(verifyCodeSession.getType().getName())){
                return ResultMsg.buildFailMsg("-1", "验证码错误！");
            }
            if (ValidatorUtil.isNotNull(verifyCodeSession.getExpireTime()) && new Date().after(verifyCodeSession.getExpireTime())){
                return ResultMsg.buildFailMsg("-1", "验证码已过期！");
            }
            user = userService.regist(user);
            session.removeAttribute("verifyCodeSession");
            return ResultMsg.buildSuccessMsg(user.getEncodeId());
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
    }

    /**
     * 管理界面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("main")
    public String main(HttpSession session, Model model) {
        User user = SpringSecurityUtil.getCurrentUser();
        model.addAttribute("user", user);
        //当前操作门店
        ObjectId currentShopId = (ObjectId) session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(currentShopId)){
            Shop shop = userService.defaultShop(user);
            if(ValidatorUtil.isNotNull(shop)){
                session.setAttribute(Constant.SHOPID_SESSION, shop.get_id());
                session.setAttribute(Constant.SHOP_SUPER_FLAG, shop.getSuperFlag());
                model.addAttribute("currentShop", shop);
            }else if(RoleEnum.owner.getCode().equals(user.getRole())){//新管理员允许新建门店
                model.addAttribute("createNewShop", true);
            }
        }else{
            Shop shop = shopService.findAvailableShop(currentShopId);
            model.addAttribute("currentShop", shop);
        }
        return "bk/main";
    }

    /**
     * 选择门店
     * @return
     */
    @RequestMapping("chooseShop")
    public String chooseShop(){
        return "bk/switch-shop";
    }

    /**
     * 切换门店
     * @param dataStr
     * @return
     */
    @RequestMapping("switchShop")
    @ResponseBody
    public ResultMsg switchShop(String dataStr, HttpSession session){
        User user = SpringSecurityUtil.getCurrentUser();
        Shop shop = SerializationUtil.deSerializeObject(dataStr, Shop.class);
        if(ValidatorUtil.isNull(user)){
            return ResultMsg.buildFailMsg("-1", "登录失效");
        }
        if(RoleEnum.manager.getCode().equals(user.getRole()) && (ValidatorUtil.isNull(shop) || ValidatorUtil.isNull(user.getManageShops()) || user.getManageShops().indexOf(new ObjectId(shop.getObjectIdStr())) < 0)){
            return ResultMsg.buildFailMsg("-1", "当前用户没有权限管理该门店");
        }
        session.setAttribute(Constant.SHOPID_SESSION, new ObjectId(shop.getObjectIdStr()));
        session.setAttribute(Constant.SHOP_SUPER_FLAG, shop.getSuperFlag());
        return ResultMsg.buildSuccessMsg();
    }

    /**
     * 获取可管理门店
     * @param session
     * @return
     */
    @RequestMapping("shopList")
    @ResponseBody
    public List<Shop> getShopList(HttpSession session){
        User user = SpringSecurityUtil.getCurrentUser();
        return shopService.findShopsByUser(user);
    }

    /**
     * 用户未失效时，从Main页面获取shopId重新存入session
     * @param shopId
     * @param session
     * @return
     */
    @RequestMapping("resetShopId")
    @ResponseBody
    public ResultMsg resetShopId(String shopId, HttpSession session){
        User user = SpringSecurityUtil.getCurrentUser();

        if (ValidatorUtil.isNull(user)){
            return ResultMsg.buildFailMsg("-1", "");
        }
        try {
            //判断当前登录用户是否有权限操作此门店
            Shop shop = shopService.findAvailableShop(new ObjectId(shopId));
            if(RoleEnum.owner.getCode().equals(user.getRole())&&shop.getOwner().equals(user.get_id())){
                session.setAttribute(Constant.SHOPID_SESSION, new ObjectId(shopId));
                session.setAttribute(Constant.SHOP_SUPER_FLAG, shop.getSuperFlag());
                return ResultMsg.buildSuccessMsg();
            }else if(RoleEnum.manager.getCode().equals(user.getRole())&&user.getManageShops().indexOf(shop.get_id())>=0){
                session.setAttribute(Constant.SHOPID_SESSION, new ObjectId(shopId));
                session.setAttribute(Constant.SHOP_SUPER_FLAG, shop.getSuperFlag());
                return ResultMsg.buildSuccessMsg();
            }else {
                return ResultMsg.buildFailMsg("-1", "");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "");
        }
    }

    /**
     * test
     */
    @RequestMapping("testBack")
    public String test(Model model, HttpSession session){
        User user = userService.findByName("covilla");
        if(ValidatorUtil.isNull(session) || ValidatorUtil.isNull(user)){
            model.addAttribute("errorMsg", "扫描失败，请刷新页面重试");
            return "wx/login-bk";
        }
        session.setAttribute(Constant.USER_SESSION, user);
        return "wx/login-bk";
    }

    @RequestMapping("testTable")
    public String testTable(){
        return "bk/table-test";
    }

    @RequestMapping("index.js.map")
    @ResponseBody
    public void indexMap(){
        return;
    }

    /**
     * 更换绑定微信回调
     * @return
     */
    @RequestMapping("rebindCallBack")
    public String rebindCallBack(String code, String sessionId){
        Session session = repository.getSession(sessionId);

        UserWeiXin userWeiXin = WeiXinApi.getAuthUserInfo(code);
        session.setAttribute(Constant.WX_REBIND_USER, userWeiXin);
        repository.save(session);
        return "wx/login-bk";
    }

    /**
     * 首次新增门店
     * @return
     */
    @RequestMapping("addShop")
    public String addShop(){
        return "bk/add-shop";
    }

    /**
     * 新增门店提交
     * @param dataStr
     * @param session
     * @return
     */
    @RequestMapping("addShopSubmit")
    @ResponseBody
    public ResultMsg addShopSubmit(String dataStr, HttpSession session){
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            Shop shop = SerializationUtil.deSerializeObject(dataStr, Shop.class);
            shopService.createTryOutShop(shop, user);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("sendVerifyCode")
    @ResponseBody
    public ResultMsg sendVerifyCode(String tel, HttpSession session){
        Shop dataShop = shopService.findDataShop();
        try {
            VerificationCodeMessage message = smsProducer.sendVerifyCode(tel, "注册商家", dataShop.get_id().toString());
            VerifyCode verifyCode = new VerifyCode(tel, message.getCode(), VerifyCode.TypeEnum.REGIST_MERCHANT);
            Date date = new Date();
            date.setTime(date.getTime() + (60*5*1000));
            verifyCode.setExpireTime(date);
            session.setAttribute("verifyCodeSession", verifyCode);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
        return ResultMsg.buildSuccessMsg();
    }

    /**
     * 手动登录
     * @param user
     * @param request
     */
    private void manualLogin(User user, HttpServletRequest request){
        //根据用户名username加载userDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        //设置authentication中details
        authentication.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(Constant.USER_SESSION, user);
    }

}
