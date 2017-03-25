package com.covilla.controller.bk.setting;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.model.mongo.setting.SmsGateway;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.util.SerializationUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.ResultMsg;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 短信网关设置
 * Created by qmaolong on 2017/3/3.
 */
@Controller
@RequestMapping("/merchant/admin/setting")
public class SmsConfigController {
    @Autowired
    private ShopService shopService;

    @RequestMapping("/smsConfig")
    public String smsConfig(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNotNull(shop)){
            model.addAttribute("smsConfig", shop.getSmsGateway());
        }
        model.addAttribute("appTypes", SmsGateway.SmsTypeEnum.values());
        return "bk/setting/sms-config";
    }

    @RequestMapping("editSmsConfig")
    @ResponseBody
    @AOPLogAnnotation(description = "修改短信网关参数")
    public ResultMsg editSmsConfig(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            Shop shop = shopService.findBy_id(shopId);
            SmsGateway smsGateway = SerializationUtil.deSerializeObject(dataStr, SmsGateway.class);
            if (ValidatorUtil.isNotNull(smsGateway.getUseSystem())){//使用系统配置
                smsGateway = new SmsGateway();
                smsGateway.setUseSystem(true);
            }
            shop.setSmsGateway(smsGateway);
            shopService.updateDocument(shop);
            return ResultMsg.buildSuccessMsg();
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
    }
}
