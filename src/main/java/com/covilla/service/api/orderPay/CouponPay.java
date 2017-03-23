package com.covilla.service.api.orderPay;

import com.covilla.common.*;
import com.covilla.model.CardFlow;
import com.covilla.model.PayFlow;
import com.covilla.model.mongo.card.Card;
import com.covilla.service.card.CardFlowService;
import com.covilla.service.coupon.CouponService;
import com.covilla.service.pay.PayFlowService;
import com.covilla.util.DateUtil;
import com.covilla.util.SpringContextUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.BaseApiResultMsg;
import com.covilla.vo.api.ConsumeApiMsg;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 券支付
 * Created by qmaolong on 2016/12/22.
 */
public class CouponPay implements IOrderPay {
    public ConsumeApiMsg pay(String orderNo, BigDecimal orderAmount, BigDecimal cardAmount, String cardId, String body, String shopId, String operator, String remark){
        try {
            new ObjectId(cardId);
        }catch (Exception e){
            return ConsumeApiMsg.buildFailMsg("没有这个券~");
        }

        CouponService couponService = (CouponService) SpringContextUtil.getBean("couponService");
        CardFlowService cardFlowService = (CardFlowService) SpringContextUtil.getBean("cardFlowService");
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");

        Card coupon = couponService.findCouponBy_id(new ObjectId(cardId));
        if(ValidatorUtil.isNull(coupon)){
            return ConsumeApiMsg.buildFailMsg("券信息提取失败~");
        }else if(!CardTypeEnum.coupon.getCode().equals(coupon.getMedium())){
            return ConsumeApiMsg.buildFailMsg("当前卡不可用于券支付~");
        }

        if(!CardStateEnum.activated.getCode().equals(coupon.getCardState())){
            return ConsumeApiMsg.buildFailMsg("券" + CardStateEnum.getNameByCode(coupon.getCardState()) + ",不可使用");
        }

        if(ValidatorUtil.isNotNull(cardAmount) && coupon.getBalance().compareTo(cardAmount) < 0){
            return ConsumeApiMsg.buildStateMsg(PaymentEnum.card.getName(), PayStateEnum.notEnough.getName(), "当前券面值" + coupon.getBalance() + ",不足以完成支付", "更换其他支付方式", orderNo, null, cardAmount.toString(), null);
        }

        String seriesNumber = cardFlowService.generateSeriesNumber(BusinessTypeEnum.payOrder.getPrefix());
        //保存流水信息
        CardFlow cardFlow = new CardFlow();
        cardFlow.setCardOwnerShop(coupon.getShopId().toString());
        cardFlow.setCardUseShop(shopId);
        cardFlow.setSeriesNo(seriesNumber);
        cardFlow.setAmount(cardAmount);
        cardFlow.setOption(BusinessTypeEnum.payOrder.getType());
        cardFlow.setCardId(cardId);
        cardFlow.setCardTagNo(coupon.getTagId());
        cardFlow.setOrderNo(orderNo);
        cardFlow.setCreateTime(new Date());
        cardFlow.setCreator(operator);
        cardFlow.setLastBalance(coupon.getBalance());
        cardFlow.setCurBalance(coupon.getBalance());
        cardFlow.setRemark(remark);
        cardFlow.setCardMediumKey(coupon.getMediumKey());
        cardFlow.setCardMedium(coupon.getMedium());
        cardFlow.setIsPublic(coupon.getIsPublic());
        cardFlowService.addCardFlow(cardFlow);

        //保存支付记录
        PayFlow payFlow = new PayFlow(orderNo, seriesNumber, cardAmount, orderNo, shopId, PaymentEnum.coupon.getName(), null, null, null, cardId, 0, PayStateEnum.success.getName());
        payFlowService.save(payFlow);

        //券状态为已使用
        coupon.setCardState(CardStateEnum.used.getCode());
        coupon.setOrderId(orderNo);
        coupon.setUsedTime(payFlow.getCreateTime());
        couponService.editCard(coupon);

        return ConsumeApiMsg.buildSuccessMsg(PaymentEnum.coupon.getName(), orderNo, seriesNumber, cardAmount.toString(), DateUtil.formateDateToStr(cardFlow.getCreateTime(), "yyyyMMddHHmmss"), cardFlow.getLastBalance(), cardFlow.getCurBalance(), cardFlow.getLastPoint(), cardFlow.getCurPoint());
    }

    public BaseApiResultMsg rollBackPay(PayFlow payFlow, String operator, String remark){
        CardFlowService cardFlowService = (CardFlowService) SpringContextUtil.getBean("cardFlowService");
        CouponService couponService = (CouponService) SpringContextUtil.getBean("couponService");
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");
        //撤销支付流水
        CardFlow cardFlow = cardFlowService.getBySeriesNo(payFlow.getSeriesNo(), BusinessTypeEnum.payOrder.getType());
        if(ValidatorUtil.isNull(cardFlow)){
            ConsumeApiMsg.buildFailMsg("流水信息提取失败~");
        }

        if(ValidatorUtil.isNotNull(cardFlow.getIsRollback()) && cardFlow.getIsRollback().equals(1)){
            ConsumeApiMsg.buildFailMsg("重复撤销~");
        }

        Card coupon = couponService.findCouponBy_id(new ObjectId(cardFlow.getCardId()));
        if(ValidatorUtil.isNull(coupon)){
            ConsumeApiMsg.buildFailMsg("券信息提取失败~");
        }else if(!couponService.authCheck(coupon, payFlow.getShopId())){;//验证权限
            ConsumeApiMsg.buildFailMsg("当前门店没有权限操作此券");
        }

        String seriesNo = cardFlowService.generateSeriesNumber(BusinessTypeEnum.rollbackPay.getPrefix());

        CardFlow newCardFlow = new CardFlow();
        newCardFlow.setCardOwnerShop(coupon.getShopId().toString());
        newCardFlow.setSeriesNo(seriesNo);
        newCardFlow.setAmount(cardFlow.getAmount());
        newCardFlow.setOption(BusinessTypeEnum.rollbackPay.getType());
        newCardFlow.setCardId(cardFlow.getCardId());
        newCardFlow.setCardTagNo(cardFlow.getCardTagNo());
        newCardFlow.setCreateTime(new Date());
        newCardFlow.setCreator(operator);
        newCardFlow.setRemark(remark);
        newCardFlow.setOrderNo(cardFlow.getOrderNo());
        newCardFlow.setCardUseShop(payFlow.getShopId());
        newCardFlow.setCardMediumKey(coupon.getMediumKey());
        newCardFlow.setCardMedium(coupon.getMedium());
        newCardFlow.setRollbackId(cardFlow.getSeriesNo());
        newCardFlow.setIsPublic(coupon.getIsPublic());

        //旧流水标记撤销
        cardFlow.setIsRollback(1);
        payFlow.setState(PayStateEnum.revoked.getName());

        cardFlowService.addCardFlow(newCardFlow);
        cardFlowService.updateFlow(cardFlow);
        payFlowService.updateByOrderNo(payFlow);

        //修改券状态为已激活
        coupon.setCardState(CardStateEnum.activated.getCode());
        couponService.editCard(coupon);

        ConsumeApiMsg result = new ConsumeApiMsg();
        result.setPayState(PayStateEnum.revoked.getName());
        result.setSeriesNumber(payFlow.getSeriesNo());
        result.setCurBalance(newCardFlow.getCurBalance());
        result.setLastBalance(newCardFlow.getLastBalance());
        result.setLastPoint(newCardFlow.getLastPoint());
        result.setCurPoint(newCardFlow.getCurPoint());
        result.setOrderNo(cardFlow.getOrderNo());
        result.setPayAmount(cardFlow.getAmount().toString());
        result.setEndTime(DateUtil.formateDateToStr(cardFlow.getCreateTime(), "yyyyMMddHHmmss"));

        return result;
    }

    public ConsumeApiMsg checkPayState(String orderNo, String shopId) throws Exception{
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");
        PayFlow payFlow = payFlowService.findByOrderNo(orderNo);

        ConsumeApiMsg result = new ConsumeApiMsg();
        result.setPayState(payFlow.getState());
        result.setSeriesNumber(payFlow.getSeriesNo());
        result.setOrderNo(payFlow.getOrderNo());
        result.setPayAmount(payFlow.getAmount().toString());
        result.setEndTime(DateUtil.formateDateToStr(payFlow.getCreateTime(), "yyyyMMddHHmmss"));

        return result;
    }
}
