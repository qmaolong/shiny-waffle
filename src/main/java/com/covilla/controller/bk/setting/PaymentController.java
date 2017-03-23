package com.covilla.controller.bk.setting;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.LogOptionEnum;
import com.covilla.model.mongo.setting.Payment;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.ServiceException;
import com.covilla.service.config.ConfigService;
import com.covilla.service.pay.AliPayService;
import com.covilla.service.setting.PaymentService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.ResultMsg;
import org.bson.types.ObjectId;
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
 * Created by qmaolong on 2016/11/4.
 */
@Controller
@RequestMapping("merchant/admin/setting")
public class PaymentController {
    private Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private AliPayService aliPayService;

    @RequestMapping("payment")
    public String payment(){
        return "bk/setting/payment";
    }

    @RequestMapping("getPayments")
    @ResponseBody
    public List<Payment> getPayments(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getPayments();
        }
        return null;
    }

    @RequestMapping("paymentInput")
    public String paymentInput(String oper, Model model){
        model.addAttribute("oper", oper);
        model.addAttribute("aliAuthUrl", aliPayService.generateAuthURL());
        return "bk/setting/payment-input";
    }

    @RequestMapping("editPayment")
    @ResponseBody
    @AOPLogAnnotation(description = "支付方式",displayTitle = "支付方式",displayItemFromDataStr = "describe")
    public ResultMsg editPayment(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            paymentService.editPayment(dataStr, oper, shopId);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("sortPayment")
    @ResponseBody
    @AOPLogAnnotation(description = "支付方式排序",option = LogOptionEnum.BUSINESS)
    public ResultMsg sortPayment(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            paymentService.sortPayment(dataStr, shopId);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("choosePayment")
    public String choosePayment(){
        return "bk/setting/choose-payment";
    }

    @RequestMapping("getPaymentConfig")
    @ResponseBody
    public List<Payment> getPaymentConfig(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);

        List<Payment> allPayment = configService.getAllPayment();
        List<Payment> myPayments = shop.getPayments();

        List<Payment> newPayment = new ArrayList<Payment>();
        for (Payment payment : allPayment){
            boolean exist = false;
            if(ValidatorUtil.isNotNull(myPayments)){
                for(Payment payment1 : myPayments){
                    if(payment.getName().equals(payment1.getName())){
                        exist = true;
                        break;
                    }
                }
            }
            if(!exist){
                newPayment.add(payment);
            }
        }
       return newPayment;
    }
}
