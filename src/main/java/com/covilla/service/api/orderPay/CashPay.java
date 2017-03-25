package com.covilla.service.api.orderPay;

import com.covilla.model.PayFlow;
import com.covilla.vo.BaseApiResultMsg;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/11/22.
 */
public class CashPay implements IOrderPay {

    public BaseApiResultMsg pay(String orderNo, BigDecimal orderAmount, BigDecimal cardAmount, String cardId, String body, String shopId, String operator, String remark){
        return new BaseApiResultMsg();
    }

    public BaseApiResultMsg rollBackPay(PayFlow payFlow, String operator, String remark){
        return new BaseApiResultMsg();
    }

    public BaseApiResultMsg checkPayState(String orderNo, String shopId) throws Exception{
        return new BaseApiResultMsg();
    }
}
