package com.covilla.service.api.orderPay;

import com.covilla.common.*;
import com.covilla.model.CardFlow;
import com.covilla.model.PayFlow;
import com.covilla.model.mongo.card.Card;
import com.covilla.service.card.CardFlowService;
import com.covilla.service.card.CardService;
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
 * Created by qmaolong on 2016/11/21.
 */
public class CardPay implements IOrderPay {

    public ConsumeApiMsg pay(String orderNo, BigDecimal orderAmount, BigDecimal cardAmount, String cardId, String body, String shopId, String operator, String remark){
        try {
            new ObjectId(cardId);
        }catch (Exception e){
            return ConsumeApiMsg.buildFailMsg("没有这个卡~");
        }

        CardService cardService = (CardService) SpringContextUtil.getBean("cardService");
        CardFlowService cardFlowService = (CardFlowService) SpringContextUtil.getBean("cardFlowService");
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");

        Card card = cardService.findByCardId(cardId);
        if(ValidatorUtil.isNull(card)){
            return ConsumeApiMsg.buildFailMsg("卡信息提取失败~");
        }else if(CardTypeEnum.coupon.getCode().equals(card.getMedium())){
            return ConsumeApiMsg.buildFailMsg("当前券不可用于卡支付~");
        }

        if(!CardStateEnum.activated.getCode().equals(card.getCardState())){
            return ConsumeApiMsg.buildFailMsg("卡" + CardStateEnum.getNameByCode(card.getCardState()) + ",不可使用");
        }

        if(ValidatorUtil.isNotNull(cardAmount) && card.getBalance().compareTo(cardAmount) < 0){
            return ConsumeApiMsg.buildStateMsg(PaymentEnum.card.getName(), PayStateEnum.notEnough.getName(), "当前卡余额" + card.getBalance() + ",不足以完成支付", "更换卡或其他支付方式", orderNo, null, cardAmount.toString(), null);
        }

        String seriesNumber = cardFlowService.generateSeriesNumber(BusinessTypeEnum.payOrder.getPrefix());

        //保存卡流水信息
        CardFlow cardFlow = new CardFlow();
        cardFlow.setCardOwnerShop(card.getShopId().toString());
        cardFlow.setCardUseShop(shopId);
        cardFlow.setSeriesNo(seriesNumber);
        cardFlow.setAmount(cardAmount);
        cardFlow.setOption(BusinessTypeEnum.payOrder.getType());
        cardFlow.setCardId(cardId);
        cardFlow.setCardTagNo(card.getTagId());
        cardFlow.setOrderNo(orderNo);
        cardFlow.setCreateTime(new Date());
        cardFlow.setCreator(operator);
        cardFlow.setLastBalance(card.getBalance());
        cardFlow.setCurBalance(card.getBalance().subtract(cardAmount));
        cardFlow.setRemark(remark);
        cardFlow.setCardMediumKey(card.getMediumKey());
        cardFlow.setCardMedium(card.getMedium());
        cardFlow.setIsPublic(card.getIsPublic());
        //积分计算
        /*if(card.getCardTypeObject().getIsSupportPoint() && ValidatorUtil.isNotNull(orderAmount)){
            cardFlow.setLastPoint(card.getPoint());
            cardFlow.setCurPoint((ValidatorUtil.isNotNull(card.getPoint())?card.getPoint():0) + Convert.toInteger(orderAmount));
            card.setPoint(cardFlow.getCurPoint());
        }*/
        //修改卡信息
        card.setBalance(cardFlow.getCurBalance());
        cardFlowService.addCardFlow(cardFlow);

        //保存支付记录
        PayFlow payFlow = new PayFlow(orderNo, seriesNumber, cardAmount, orderNo, shopId, PaymentEnum.card.getName(), null, null, null, cardId, 0, PayStateEnum.success.getName());
        payFlowService.save(payFlow);

        cardService.editCard(card);
        return ConsumeApiMsg.buildSuccessMsg(PaymentEnum.card.getName(), orderNo, seriesNumber, cardAmount.toString(), DateUtil.formateDateToStr(cardFlow.getCreateTime(), "yyyyMMddHHmmss"), cardFlow.getLastBalance(), cardFlow.getCurBalance(), cardFlow.getLastPoint(), cardFlow.getCurPoint());
    }

    public BaseApiResultMsg rollBackPay(PayFlow payFlow, String operator, String remark){
        CardFlowService cardFlowService = (CardFlowService) SpringContextUtil.getBean("cardFlowService");
        CardService cardService = (CardService) SpringContextUtil.getBean("cardService");
        PayFlowService payFlowService = (PayFlowService) SpringContextUtil.getBean("payFlowService");
        //撤销卡支付流水
        CardFlow cardFlow = cardFlowService.getBySeriesNo(payFlow.getSeriesNo(), BusinessTypeEnum.payOrder.getType());
        if(ValidatorUtil.isNull(cardFlow)){
            ConsumeApiMsg.buildFailMsg("流水信息提取失败~");
        }

        if(ValidatorUtil.isNotNull(cardFlow.getIsRollback()) && cardFlow.getIsRollback().equals(1)){
            ConsumeApiMsg.buildFailMsg("重复撤销~");
        }

        Card card = cardService.findBy_id(new ObjectId(cardFlow.getCardId()));
        if(ValidatorUtil.isNull(card)){
            ConsumeApiMsg.buildFailMsg("卡信息提取失败~");
        }else if(!cardService.authCheck(card, payFlow.getShopId())){;//验证权限
            ConsumeApiMsg.buildFailMsg("当前门店没有权限操作此卡");
        }

        String seriesNo = cardFlowService.generateSeriesNumber(BusinessTypeEnum.rollbackPay.getPrefix());

        CardFlow newCardFlow = new CardFlow();
        newCardFlow.setCardOwnerShop(card.getShopId().toString());
        newCardFlow.setSeriesNo(seriesNo);
        newCardFlow.setAmount(cardFlow.getAmount());
        newCardFlow.setOption(BusinessTypeEnum.rollbackPay.getType());
        newCardFlow.setCardId(cardFlow.getCardId());
        newCardFlow.setCardTagNo(cardFlow.getCardTagNo());
        newCardFlow.setCreateTime(new Date());
        newCardFlow.setCreator(operator);
        newCardFlow.setLastBalance(card.getBalance());
        newCardFlow.setCurBalance(card.getBalance().add(cardFlow.getAmount()));
        newCardFlow.setRemark(remark);
        newCardFlow.setOrderNo(cardFlow.getOrderNo());
        newCardFlow.setCardUseShop(payFlow.getShopId());
        newCardFlow.setLastPoint(card.getPoint());
//        newCardFlow.setCurPoint(card.getPoint() - Convert.toInteger(cardFlow.getAmount()));
        newCardFlow.setCardMediumKey(card.getMediumKey());
        newCardFlow.setCardMedium(card.getMedium());
        newCardFlow.setRollbackId(cardFlow.getSeriesNo());
        newCardFlow.setIsPublic(card.getIsPublic());

        //旧流水标记撤销
        cardFlow.setIsRollback(1);
        payFlow.setState(PayStateEnum.revoked.getName());

        //修改卡信息
        card.setBalance(newCardFlow.getCurBalance());
//        card.setPoint(newCardFlow.getCurPoint());

        cardFlowService.addCardFlow(newCardFlow);
        cardFlowService.updateFlow(cardFlow);
        cardService.editCard(card);
        payFlowService.updateByOrderNo(payFlow);

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
