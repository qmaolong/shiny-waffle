package com.covilla.service.api.orderPay;

import com.covilla.model.PayFlow;
import com.covilla.vo.BaseApiResultMsg;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/11/21.
 */
public interface IOrderPay {
    /**
     * 支付接口
     * @param orderNo
     * @param orderAmount
     * @param cardAmount
     * @param cardId
     * @param shopId
     * @param operator
     * @param remark
     * @return
     */
    public BaseApiResultMsg pay(String orderNo, BigDecimal orderAmount, BigDecimal cardAmount, String cardId, String body, String shopId, String operator, String remark) throws Exception;

    public BaseApiResultMsg rollBackPay(PayFlow payFlow, String operator, String remark) throws Exception;

    public BaseApiResultMsg checkPayState(String orderNo, String shopId) throws Exception;
}
