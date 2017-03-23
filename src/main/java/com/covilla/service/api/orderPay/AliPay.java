package com.covilla.service.api.orderPay;

import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.covilla.common.PayStateEnum;
import com.covilla.common.PaymentEnum;
import com.covilla.model.PayFlow;
import com.covilla.model.mongo.setting.Payment;
import com.covilla.service.ServiceException;
import com.covilla.service.pay.AliPayService;
import com.covilla.service.pay.PayFlowService;
import com.covilla.service.setting.PaymentService;
import com.covilla.util.DateUtil;
import com.covilla.util.SpringContextUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.BaseApiResultMsg;
import com.covilla.vo.api.PayStateApiMsg;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by qmaolong on 2016/11/22.
 */
public class AliPay implements IOrderPay {
    Logger logger = LoggerFactory.getLogger(AliPay.class);

    public BaseApiResultMsg pay(String orderNo, BigDecimal orderAmount, BigDecimal cardAmount, String cardId, String body, String shopId, String operator, String remark) throws Exception{
        PaymentService paymentService = (PaymentService) SpringContextUtil.getBean("paymentService");
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");
        Payment payment = paymentService.findPayment(new ObjectId(shopId), PaymentEnum.aliPay.getName());

        if(!payment.getActivated()){
            throw new ServiceException("支付宝支付未启用");
        }else if (ValidatorUtil.isNull(payment) || ValidatorUtil.isNull(payment.getAlAuthToken())){
            throw new ServiceException("门店支付宝授权参数不全");
        }else if(ValidatorUtil.isNotNull(payment.getAlTokenExpireDate()) && payment.getAlTokenExpireDate().before(DateUtil.addDay(new Date(), 180)) && ValidatorUtil.isNotNull(payment.getAlRefreshTokenExpireDate()) && payment.getAlRefreshTokenExpireDate().after(new Date())){//半年刷新一次
            payment = paymentService.refreshAliPayment(shopId);//刷新授权token
        }else if(ValidatorUtil.isNotNull(payment.getAlRefreshTokenExpireDate()) && payment.getAlRefreshTokenExpireDate().before(new Date())){
            throw new ServiceException("支付宝授权信息已过期，请登录后台重新授权~");
        }
        AliPayService aliPayService = (AliPayService) SpringContextUtil.getBean("aliPayService");

        //查找是否已有支付记录
        PayFlow payFlow = payFlowService.findByOrderNo(orderNo);
        if(ValidatorUtil.isNull(payFlow)){
            payFlow = new PayFlow(orderNo, payFlowService.generateSeriesNumber("P"), cardAmount, orderNo, shopId, PaymentEnum.aliPay.getName(), null, null, payment.getAlAuthToken(), cardId, 0, "NEW");
            payFlowService.save(payFlow);
        }

        if(ValidatorUtil.isNull(body)){
            body = orderNo;
        }
        try {
            AlipayTradePayResponse response = aliPayService.scanPay(payment.getAlAuthToken(), orderNo, cardAmount, cardId, body, shopId);
            if(ValidatorUtil.isNull(response)){
                throw new ServiceException("支付宝支付调用失败");
            }

            PayStateApiMsg resultMsg = null;
            if(response.getCode().equals("10000")){
                resultMsg = PayStateApiMsg.buildSuccessMsg(PaymentEnum.aliPay.getName(), response.getOutTradeNo(), response.getTradeNo(), response.getTotalAmount(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
            }else if(response.getCode().equals("10003")){
                resultMsg = PayStateApiMsg.buildStateMsg(PaymentEnum.aliPay.getName(), PayStateEnum.userPaying.getName(), PayStateEnum.userPaying.getDesc(), PayStateEnum.userPaying.getOptionAdvise(), response.getOutTradeNo(), response.getTradeNo(), response.getTotalAmount(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
            }else {
                PayStateEnum payStateEnum = PayStateEnum.findByAliCode(response.getSubCode());
                if(ValidatorUtil.isNotNull(payStateEnum)){
                    resultMsg = PayStateApiMsg.buildStateMsg(PaymentEnum.aliPay.getName(), payStateEnum.getName(), payStateEnum.getDesc(), payStateEnum.getOptionAdvise(), response.getOutTradeNo(), response.getTradeNo(), cardAmount.toString(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
                }else {
                    resultMsg = PayStateApiMsg.buildFailMsg(PaymentEnum.aliPay.getName(), response.getSubMsg(), response.getOutTradeNo(), response.getTradeNo(), cardAmount.toString().toString(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
                }
            }
            //修改支付状态
            payFlow.setSeriesNo(resultMsg.getSeriesNumber());
            payFlow.setState(resultMsg.getPayState());
            payFlowService.updateByOrderNo(payFlow);
            return resultMsg;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ServiceException("支付宝接口调用错误~");
        }
    }

    public BaseApiResultMsg rollBackPay(PayFlow payFlow, String operator, String remark){
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");
        AliPayService aliPayService = (AliPayService) SpringContextUtil.getBean("aliPayService");
        try {
            AlipayTradeCancelResponse response = aliPayService.cancelPay(payFlow);
            if(ValidatorUtil.isNull(response)){
                throw new ServiceException("支付宝支付调用失败");
            }

            PayStateApiMsg result = null;
            if(response.getCode().equals("10000")){
                result = PayStateApiMsg.buildStateMsg(PaymentEnum.aliPay.getName(), PayStateEnum.revoked.getName(), "", "", response.getOutTradeNo(), response.getTradeNo(), payFlow.getAmount().toString(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
            }else {
                PayStateEnum payStateEnum = PayStateEnum.findByAliCode(response.getSubCode());
                if(ValidatorUtil.isNotNull(payStateEnum)){
                    result = PayStateApiMsg.buildStateMsg(PaymentEnum.aliPay.getName(), payStateEnum.getName(), payStateEnum.getDesc(), payStateEnum.getOptionAdvise(), response.getOutTradeNo(), response.getTradeNo(), payFlow.getAmount().toString(), "");
                }else {
                    result = PayStateApiMsg.buildFailMsg(PaymentEnum.aliPay.getName(), response.getSubMsg(), response.getOutTradeNo(), response.getTradeNo(), payFlow.getAmount().toString(), "");
                }
            }
            //只有撤销成功才更新状态
            if(PayStateEnum.revoked.getName().equals(result.getPayState())){
                payFlow.setState(PayStateEnum.revoked.getName());
                payFlowService.updateByOrderNo(payFlow);
            }
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ServiceException("撤销接口调用错误~");
        }
    }

    public PayStateApiMsg checkPayState(String orderNo, String shopId) throws Exception{
        AliPayService aliPayService = (AliPayService) SpringContextUtil.getBean("aliPayService");
        PaymentService paymentService = (PaymentService) SpringContextUtil.getBean("paymentService");
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");

        PayFlow payFlow = payFlowService.findByOrderNo(orderNo);

        Payment payment = paymentService.findPayment(new ObjectId(shopId), PaymentEnum.aliPay.getName());
        if(ValidatorUtil.isNull(payment)||ValidatorUtil.isNull(payment.getAlAuthToken())){
            throw new ServiceException("支付宝授权参数未配置");
        }
        AlipayTradeQueryResponse response = aliPayService.checkPayState(payment.getAlAuthToken(), orderNo);

        PayStateApiMsg result = null;
        if("WAIT_BUYER_PAY".equals(response.getTradeStatus())){
            result = PayStateApiMsg.buildStateMsg(PaymentEnum.aliPay.getName(), PayStateEnum.userPaying.getName(), PayStateEnum.userPaying.getDesc(), PayStateEnum.userPaying.getOptionAdvise(), response.getOutTradeNo(), response.getTradeNo(), response.getTotalAmount(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
        }else if("TRADE_SUCCESS".equals(response.getTradeStatus())){
            result = PayStateApiMsg.buildSuccessMsg(PaymentEnum.aliPay.getName(), response.getOutTradeNo(), response.getTradeNo(), response.getTotalAmount(), "");
        }else if("TRADE_CLOSED".equals(response.getTradeStatus())){
            result = PayStateApiMsg.buildStateMsg(PaymentEnum.aliPay.getName(), PayStateEnum.orderClosed.getName(), PayStateEnum.orderClosed.getDesc(), PayStateEnum.orderClosed.getOptionAdvise(), response.getOutTradeNo(), response.getTradeNo(), response.getTotalAmount(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
        }
        else {
            PayStateEnum payStateEnum = PayStateEnum.findByAliCode(response.getSubCode());
            if(ValidatorUtil.isNotNull(payStateEnum)){
                result = PayStateApiMsg.buildStateMsg(PaymentEnum.aliPay.getName(), payStateEnum.getName(), payStateEnum.getDesc(), payStateEnum.getOptionAdvise(), response.getOutTradeNo(), response.getTradeNo(), response.getTotalAmount(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
            }else {
                result = PayStateApiMsg.buildFailMsg(PaymentEnum.aliPay.getName(), response.getSubMsg(), response.getOutTradeNo(), response.getTradeNo(), response.getTotalAmount(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
            }
        }

        //更新支付状态
        payFlow.setSeriesNo(result.getSeriesNumber());
        payFlow.setState(result.getPayState());
        payFlowService.updateByOrderNo(payFlow);

        return result;



    }
}
