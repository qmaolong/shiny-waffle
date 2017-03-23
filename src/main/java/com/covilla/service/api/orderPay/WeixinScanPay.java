package com.covilla.service.api.orderPay;

import com.covilla.common.PayStateEnum;
import com.covilla.common.PaymentEnum;
import com.covilla.model.PayFlow;
import com.covilla.model.mongo.setting.Payment;
import com.covilla.service.ServiceException;
import com.covilla.service.pay.PayFlowService;
import com.covilla.service.pay.WXPayService;
import com.covilla.service.setting.PaymentService;
import com.covilla.util.DateUtil;
import com.covilla.util.SpringContextUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.BaseApiResultMsg;
import com.covilla.vo.api.PayStateApiMsg;
import com.tencent.protocol.pay_protocol.ScanPayResData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tencent.protocol.reverse_protocol.ReverseReqData;
import com.tencent.protocol.reverse_protocol.ReverseResData;
import com.tencent.service.ReverseService;
import jodd.typeconverter.Convert;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信支付
 * Created by qmaolong on 2016/11/21.
 */
public class WeixinScanPay implements IOrderPay {
    Logger logger = LoggerFactory.getLogger(WeixinScanPay.class);

    public BaseApiResultMsg pay(String orderNo, BigDecimal orderAmount, BigDecimal cardAmount, String cardId, String body, String shopId, String operator, String remark) throws Exception{
        PaymentService paymentService = (PaymentService) SpringContextUtil.getBean("paymentService");
        WXPayService wxPayService = new WXPayService();
        ObjectId shop = new ObjectId(shopId);
        Payment payment = paymentService.findPayment(shop, PaymentEnum.wxPay.getName());
        if (ValidatorUtil.isNull(payment) || ValidatorUtil.isNull(payment.getWxAppId()) || ValidatorUtil.isNull(payment.getWxMchId())){
            throw new ServiceException("门店支付参数不全");
        }else if(!payment.getActivated()){
            throw new ServiceException("门店微信支付未启用");
        }

        //查找是否已有支付记录
        PayFlowService payFlowService = (PayFlowService)SpringContextUtil.getBean("payFlowService");
        PayFlow payFlow = payFlowService.findByOrderNo(orderNo);
        if(ValidatorUtil.isNull(payFlow)){
            payFlow = new PayFlow(orderNo, null, cardAmount, orderNo, shopId, PaymentEnum.wxPay.getName(), payment.getWxAppId(), payment.getWxMchId(), null, cardId, 0, "NEW");
            payFlowService.save(payFlow);
        }

        if(ValidatorUtil.isNull(body)){
            body = orderNo;
        }

        ScanPayResData scanPayResData = wxPayService.scanPayByBusiness(payment.getWxAppId(), payment.getWxMchId(), shopId, body, orderNo, cardAmount, "", cardId);

        if (!"SUCCESS".equals(scanPayResData.getReturn_code())){ //1.支付调用失败
            throw new ServiceException(scanPayResData.getReturn_msg());
        }

        PayStateApiMsg resultMsg = null;
        if (!PayStateEnum.success.getWxPayCode().equals(scanPayResData.getResult_code())){ //2.调用成功，业务交易不成功
            String errorCode = scanPayResData.getErr_code();
            PayStateEnum payStateEnum = PayStateEnum.findByWXCode(errorCode);
            if(ValidatorUtil.isNotNull(payStateEnum)){
                resultMsg = PayStateApiMsg.buildStateMsg(PaymentEnum.wxPay.getName(), payStateEnum.getName(), payStateEnum.getDesc(), payStateEnum.getOptionAdvise(), orderNo, scanPayResData.getTransaction_id(), Convert.toString(cardAmount), scanPayResData.getTime_end());
            }else {
                resultMsg = PayStateApiMsg.buildFailMsg(PaymentEnum.wxPay.getName(), scanPayResData.getErr_code_des(), scanPayResData.getOut_trade_no(), scanPayResData.getTransaction_id(), scanPayResData.getTotal_fee(), scanPayResData.getTime_end());
            }
        }else {//3.交易成功
            Double totalAmount = Convert.toDouble(Convert.toInteger(scanPayResData.getTotal_fee())/100);
            resultMsg = PayStateApiMsg.buildSuccessMsg(PaymentEnum.wxPay.getName(), scanPayResData.getOut_trade_no(), scanPayResData.getTransaction_id(), Convert.toString(totalAmount), scanPayResData.getTime_end());
        }
        //更新支付状态
        payFlow.setSeriesNo(resultMsg.getSeriesNumber());
        payFlow.setState(resultMsg.getPayState());
        payFlowService.updateByOrderNo(payFlow);

        return resultMsg;
    }

    public PayStateApiMsg rollBackPay(PayFlow payFlow, String operator, String remark) throws Exception{
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");
        WXPayService wxPayService = new WXPayService();
        try {
            ReverseResData reverseResData = wxPayService.rollBackPay(payFlow);

            PayStateApiMsg resultMsg = null;
            if (ValidatorUtil.isNull(reverseResData) || !"SUCCESS".equals(reverseResData.getReturn_code())){ //1.调用失败
                throw new ServiceException(reverseResData.getReturn_msg());
            }else if (!PayStateEnum.success.getWxPayCode().equals(reverseResData.getResult_code())){ //2.调用成功，业务交易不成功
                String errorCode = reverseResData.getErr_code();
                PayStateEnum payStateEnum = PayStateEnum.findByWXCode(errorCode);
                if(ValidatorUtil.isNotNull(payStateEnum)){
                    resultMsg = PayStateApiMsg.buildStateMsg(PaymentEnum.wxPay.getName(), payStateEnum.getName(), payStateEnum.getDesc(), payStateEnum.getOptionAdvise(), payFlow.getOrderNo(), payFlow.getSeriesNo(), Convert.toString(payFlow.getAmount()), "");
                }else {
                    return PayStateApiMsg.buildFailMsg(PaymentEnum.wxPay.getName(), reverseResData.getErr_code_des(), payFlow.getOrderNo(), payFlow.getSeriesNo(), payFlow.getAmount().toString(), "");
                }
            }else {//3.业务成功
                payFlow.setState("REVOKED");
                payFlowService.updateByOrderNo(payFlow);
                resultMsg = PayStateApiMsg.buildStateMsg(PaymentEnum.wxPay.getName(), PayStateEnum.revoked.getName(), "", "", payFlow.getOrderNo(), payFlow.getSeriesNo(), payFlow.getAmount().toString(), DateUtil.formateDateToStr(payFlow.getCreateTime(), "yyyyMMddHHmmss"));
            }
            return resultMsg;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public PayStateApiMsg checkPayState(String orderNo, String shopId) throws Exception{
        PaymentService paymentService = (PaymentService) SpringContextUtil.getBean("paymentService");
        WXPayService wxPayService = new WXPayService();
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");

        PayFlow payFlow = payFlowService.findByOrderNo(orderNo);
        Payment payment = paymentService.findPayment(new ObjectId(shopId), PaymentEnum.wxPay.getName());
        if(ValidatorUtil.isNull(payment)||ValidatorUtil.isNull(payment.getWxMchId())){
            throw new ServiceException("微信支付参数未配置");
        }
        ScanPayQueryResData scanPayResData = wxPayService.checkPayState(payment.getWxMchId(), orderNo);


        PayStateApiMsg resultMsg = null;

        if (ValidatorUtil.isNull(scanPayResData) || !"SUCCESS".equals(scanPayResData.getReturn_code())){ //1.调用失败
            throw new ServiceException(scanPayResData.getReturn_msg());
        }else if (!"SUCCESS".equals(scanPayResData.getResult_code())){ //2.调用成功，业务交易不成功
            String errorCode = scanPayResData.getErr_code();
            if(errorCode.equals("SYSTEMERROR")){//2.1系统错误或银行错误时，马上重新查询
                resultMsg = checkPayState(payFlow.getMchId(), orderNo);
            }else {//2.2其他情况为付款失败
                ReverseReqData requestData = new ReverseReqData(null, orderNo, payFlow.getAppId(), payFlow.getMchId());
                new ReverseService().request(requestData);
                throw new ServiceException("【查询失败】查询系统中不存在此交易订单号");
            }
        }else {//3.查询成功，返回支付状态
            String tradeState = scanPayResData.getTrade_state();

            PayStateEnum payStateEnum = PayStateEnum.findByWXCode(tradeState);
            if(ValidatorUtil.isNotNull(payStateEnum)){
                Double totalAmount = ValidatorUtil.isNotNull(scanPayResData.getTotal_fee())?Convert.toDouble(Convert.toInteger(scanPayResData.getTotal_fee())/100):null;
                resultMsg = PayStateApiMsg.buildStateMsg(PaymentEnum.wxPay.getName(), payStateEnum.getName(), payStateEnum.getDesc(), payStateEnum.getOptionAdvise(), scanPayResData.getOut_trade_no(), scanPayResData.getTransaction_id(), Convert.toString(payFlow.getAmount()), scanPayResData.getTime_end());
            }else {
                resultMsg =  PayStateApiMsg.buildFailMsg(PaymentEnum.wxPay.getName(), scanPayResData.getErr_code_des(), scanPayResData.getOut_trade_no(), scanPayResData.getTransaction_id(), scanPayResData.getTotal_fee().toString(), DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss"));
            }
        }

        //更新支付状态
        if(ValidatorUtil.isNotNull(scanPayResData.getTransaction_id())){
            payFlow.setSeriesNo(scanPayResData.getTransaction_id());
        }
        payFlow.setState(resultMsg.getPayState());
        payFlowService.updateByOrderNo(payFlow);

        return resultMsg;
    }
}
