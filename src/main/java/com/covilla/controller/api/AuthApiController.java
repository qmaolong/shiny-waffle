package com.covilla.controller.api;

import com.covilla.common.Constant;
import com.covilla.common.RoleEnum;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.authKey.AuthKeyService;
import com.covilla.service.shop.ShopService;
import com.covilla.service.user.UserService;
import com.covilla.util.MySecurity;
import com.covilla.util.RandomUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.util.httpclient.HttpClientUtil;
import com.covilla.vo.BaseApiResultMsg;
import com.covilla.vo.api.ResultApiMsg;
import com.covilla.vo.api.ShopVerifyApiMsg;
import jodd.typeconverter.Convert;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LongLongAgo on 2016/8/22.
 */
@Controller
@RequestMapping("/api")
public class AuthApiController {
    private Logger logger = LoggerFactory.getLogger(AuthApiController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AuthKeyService authKeyService;

    @RequestMapping("login")
    @ResponseBody
    public Map<String, Object> login(HttpSession session, String shopId){
        Map<String, Object> result = new HashMap<String, Object>();
        String randomString = RandomUtil.genRandomString(20);

        Map<String, String> params = new HashMap<String, String>();
        params.put("shop", shopId);
        params.put("random", randomString);
        String sValue = HttpClientUtil.postHttps(Constant.API_AUTH_URL, params, "UTF-8");
        if(ValidatorUtil.isNotNull(sValue) && !"400".equals(sValue)){
            Long randomInteger = Convert.toLong(RandomUtil.genRandomNumberString(8));
            result.put("index", randomInteger);
            session.setMaxInactiveInterval(24 * 60 * 60);
            session.setAttribute(Constant.API_INDEX_SESSION, randomInteger);
            session.setAttribute(Constant.API_RANDOM_SESSION, randomString);
            result.put("sValue", sValue);
            result.put("errcode", "0");
            result.put("msg", "ok");
        }else {
            result.put("errcode", "-1");
            result.put("msg", sValue);
        }
        return result;
    }

    /**
     * 客户端登录门店验证
     * @param name
     * @param password
     * @param shopNo
     * @return
     */
    @RequestMapping("getAuthKey")
    @ResponseBody
    public BaseApiResultMsg shopVerify(String name, String password, String shopNo){
        try {
            User user = userService.checkPassword(password, name);
            if (ValidatorUtil.isNull(user)){
                return BaseApiResultMsg.buildErrorMsg("-1", "用户名或密码错误");
            }
            Shop shop = shopService.findByEncodeId(shopNo);
            if (ValidatorUtil.isNull(shop)){
                return BaseApiResultMsg.buildErrorMsg("-1", "门店不存在");
            }
            if (ValidatorUtil.isNull(shop.getAuthKeys())){
                return BaseApiResultMsg.buildErrorMsg("-1", "授权码不存在");
            }

            if ((RoleEnum.owner.getCode().equals(user.getRole()) && !shop.getOwner().equals(user.get_id()))
                    || (RoleEnum.manager.getCode().equals(user.getRole()) && user.getManageShops().indexOf(shop.get_id())<0)){
                return BaseApiResultMsg.buildErrorMsg("-1", "用户与门店不匹配");
            }else {
                String key = null;
                for (String tmp : shop.getAuthKeys()){
                    if (tmp.indexOf("TEST-")==0){
                        key = tmp;
                        break;
                    }
                }
                if (ValidatorUtil.isNull(key)){
                    return BaseApiResultMsg.buildErrorMsg("-1", "授权码获取失败");
                }
                String authKey = authKeyService.encodeAuthKey(key);
                return new ShopVerifyApiMsg(shop.get_id().toString(), shop.getName(), authKey);
            }
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 广告页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("advertise")
    public String advertise(String id, Model model){
        if(ValidatorUtil.isNotNull(id)){
            model.addAttribute("shopId", id);
            Shop shop = shopService.findBy_id(new ObjectId(id));
            if (ValidatorUtil.isNotNull(shop.getAdvertise())){
                model.addAttribute("advertise", shop.getAdvertise());
                return "bk/advertise";
            }
        }

        String[] advertise = {"/asset/bk/img/lobster.jpg"};
        model.addAttribute("advertise", advertise);
        return "bk/advertise";
    }

    @RequestMapping("getAdvertise")
    @ResponseBody
    public List<String> getAdvertise(String shopId){
        if(ValidatorUtil.isNotNull(shopId)){
            Shop shop = shopService.findBy_id(shopId);
            if (ValidatorUtil.isNotNull(shop.getAdvertise())){
                return shop.getAdvertise();
            }
        }

        List<String> result = new ArrayList<String>();
        result.add("/asset/bk/img/lobster.jpg");
        return result;
    }

    /**
     * 门店端登录
     * @param name
     * @param password
     * @return
     */
    @RequestMapping("shopLogin")
    @ResponseBody
    public BaseApiResultMsg shopLogin(String name, String password, String shopNo, String target, HttpSession session){
        try {
            User user = userService.checkPassword(password, name);
            if (ValidatorUtil.isNull(user)){
                return BaseApiResultMsg.buildErrorMsg("-1", "用户名或密码错误");
            }
            Shop shop = shopService.findByEncodeId(shopNo);
            if (ValidatorUtil.isNull(shop)){
                return BaseApiResultMsg.buildErrorMsg("-1", "门店不存在");
            }
            if ((RoleEnum.owner.getCode().equals(user.getRole()) && !shop.getOwner().equals(user.get_id()))
                    || (RoleEnum.manager.getCode().equals(user.getRole()) && user.getManageShops().indexOf(shop.get_id())<0)){
                return BaseApiResultMsg.buildErrorMsg("-1", "用户与门店不匹配");
            }
            //保存登录门店id
            session.setAttribute(Constant.SHOPID_SESSION, shop.get_id());
            session.setAttribute(Constant.USER_SESSION, user);

            if("icKey".equals(target) && ValidatorUtil.isNotNull(shop.getIcKey())){
                byte[] keySpc = "l5NH8sc4$5f\\B+[2^6.Lw{NFA^E~y(B}".getBytes("UTF-8");
                byte[] iv = "^uR[A(|(D8Rhg\\'H".getBytes("UTF-8");
                byte[] sign = MySecurity.AES256Encrypt(shop.getIcKey(), "Rijndael", keySpc, iv);
                String signStr = new BASE64Encoder().encode(sign);
                return new ResultApiMsg(signStr, shop.getObjectIdStr(), shop.getName());
            }
            return new ResultApiMsg(null, shop.getObjectIdStr(), shop.getName());
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }
}
