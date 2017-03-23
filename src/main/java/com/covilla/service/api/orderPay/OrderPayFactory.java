package com.covilla.service.api.orderPay;

import com.covilla.common.CardTypeEnum;
import com.covilla.common.PaymentEnum;
import com.covilla.model.mongo.card.Card;
import com.covilla.service.card.CardService;
import com.covilla.util.SpringContextUtil;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;

/**
 * Created by qmaolong on 2016/11/21.
 */
public class OrderPayFactory {

    public IOrderPay getPayInstant(String key, String payType){
        //自定义支付方式
        if(ValidatorUtil.isNotNull(payType)){
            if(PaymentEnum.aliPay.getName().equals(payType)){
                return new AliPay();
            }else if(PaymentEnum.wxPay.getName().equals(payType)){
                return new WeixinScanPay();
            }else if(PaymentEnum.card.getName().equals(payType)){
                CardService cardService = (CardService) SpringContextUtil.getBean("cardService");
                Card card = cardService.findBy_id(new ObjectId(key));
                if(CardTypeEnum.coupon.getCode().equals(card.getMedium())){//券支付
                    return new CouponPay();
                }else {//储值卡支付
                    return new CardPay();
                }
            }else if(PaymentEnum.coupon.getName().equals(payType)){
                return new CouponPay();
            }else {
                return new CashPay();
            }
        }else{//自动判断支付方式
            if(ValidatorUtil.isNull(key)){//现金、银行卡付款
                return new CashPay();
            }else if(key.indexOf("13") == 0 && key.length() == 18){//微信支付
                return new WeixinScanPay();
            }else if(key.indexOf("28") == 0){//支付宝支付
                return new AliPay();
            }else {
                CardService cardService = (CardService) SpringContextUtil.getBean("cardService");
                Card card = cardService.findBy_id(new ObjectId(key));
                if(CardTypeEnum.coupon.getCode().equals(card.getMedium())){//券支付
                    return new CouponPay();
                }else {//储值卡支付
                    return new CardPay();
                }
            }
        }
    }
}
