package com.covilla.service.api;

import com.covilla.common.*;
import com.covilla.model.*;
import com.covilla.model.mongo.card.Card;
import com.covilla.model.mongo.card.CardType;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.ServiceException;
import com.covilla.service.api.orderPay.IOrderPay;
import com.covilla.service.api.orderPay.OrderPayFactory;
import com.covilla.service.card.CardFlowService;
import com.covilla.service.card.CardService;
import com.covilla.service.card.CardTypeService;
import com.covilla.service.coupon.CouponService;
import com.covilla.service.order.OrderService;
import com.covilla.service.pay.AliPayService;
import com.covilla.service.pay.PayFlowService;
import com.covilla.service.pay.WXPayService;
import com.covilla.service.setting.PaymentService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.BaseApiResultMsg;
import com.covilla.vo.api.*;
import jodd.typeconverter.Convert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 卡API Service
 * Created by qmaolong on 2016/11/21.
 */
@Service
@Transactional
public class CardApiService {
    @Autowired
    private CardFlowService cardFlowService;
    @Autowired
    private CardService cardService;
    @Autowired
    private CardTypeService cardTypeService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private PayFlowService payFlowService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WXPayService wxPayService;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private OrderService orderService;

    /**
     * 获取门店所属卡流水
     * @param shopId
     * @return
     */
    public List<CardFlow> getByOwnerShop(String shopId){
        return cardFlowService.getByOwnerShop(shopId);
    }

    /**
     * 获取门店操作卡流水
     * @param shopId
     * @return
     */
    public List<CardFlow> getByOperateShop(String shopId){
        return cardFlowService.getByOperateShop(shopId);
    }


    //=================================================API方法=============================================================================================================

    /**
     * 获取卡信息
     * @param mediumKey
     * @param shopId
     * @return
     */
    public BaseApiResultMsg getCardInfo(String mediumKey, String shopId){
        Query query = new Query().addCriteria(Criteria.where("mediumKey").is(mediumKey).and("shopId").is(new ObjectId(shopId)));
        List<CardApiMsg> msgs = cardService.getMongoTemplate().find(query, CardApiMsg.class);

        if(ValidatorUtil.isNull(msgs) || msgs.size() == 0){
            throw new ServiceException("未找到相关卡信息~");
        }else if(!shopId.equals(msgs.get(0).getShopId().toString())){
            Shop mainShop = shopService.findMainShop(new ObjectId(shopId));
            if(!msgs.get(0).getShopId().toString().equals(shopId)){
                throw new ServiceException("当前门店没有权限操作此卡");
            }
        }
        CardType cardType = cardTypeService.findBy_id(msgs.get(0).getCardTypeId());
        msgs.get(0).setCardTypeObject(cardType);

        return msgs.get(0);
    }

    /**
     * 消费
     * @param orderNo
     * @param cardId
     * @return
     */
    public BaseApiResultMsg orderPay(String orderNo, BigDecimal orderAmount, BigDecimal cardAmount, String cardId, String body, String name, String shopId, String operator, String remark) throws Exception{
        PayFlow payFlow = payFlowService.findByOrderNo(orderNo);

        if(ValidatorUtil.isNotNull(payFlow)){
            //验证是否允许再次支付
            PayStateEnum payState = PayStateEnum.findByLocalCode(payFlow.getState());
            if(!payState.isAllowRepay()){
                return PayStateApiMsg.buildFailMsg(payFlow.getPayType(), payState.getDesc(), orderNo, payFlow.getSeriesNo(), Convert.toString(cardAmount), "");
            }
        }
        IOrderPay orderPay = new OrderPayFactory().getPayInstant(cardId, name);
        return orderPay.pay(orderNo, orderAmount, cardAmount, cardId, body, shopId, operator, remark);
    }

    /**
     * 查找支付状态
     * @param orderNo
     * @param shopId
     * @return
     */
    public BaseApiResultMsg checkPayState(String orderNo, String seriesNumber, String shopId) throws Exception{
        PayFlow payFlow = null;
        if(ValidatorUtil.isNotNull(orderNo)){
            payFlow = payFlowService.findByOrderNo(orderNo);
        }else {
            payFlow = payFlowService.findBySeriesNo(seriesNumber);
        }
        if(ValidatorUtil.isNull(payFlow)){
            throw new ServiceException("未找到支付记录~");
        }
        return new OrderPayFactory().getPayInstant(payFlow.getAuthCode(), payFlow.getPayType()).checkPayState(orderNo, shopId);
    }

    /**
     * 充值
     * @param cardId
     * @param shopId
     * @param chargeAmount
     * @param operator
     * @param remark
     * @return
     */
    public ChargeApiMsg cardCharge(String orderNo, String cardId, String shopId, BigDecimal chargeAmount, String operator, String remark){
        if(!cardService.authCheck(cardId, shopId)){
            throw new ServiceException("当前门店没有权限操作此卡");
        }
        return cardService.cardCharge(orderNo, cardId, shopId, chargeAmount, operator, remark, CardChargeTypeEnum.client.getCode());
    }

    /**
     * 消费撤销
     * @param orderNo
     * @param shopId
     * @param operator
     * @param remark
     * @return
     */
    public BaseApiResultMsg rollbackPay(String orderNo, String seriesNumber, String shopId, String operator, String remark) throws Exception{
        PayFlow payFlow = null;
        if(ValidatorUtil.isNotNull(orderNo)){
            payFlow = payFlowService.findByOrderNo(orderNo);
        }else {
            payFlow = payFlowService.findBySeriesNo(seriesNumber);
        }
        if(ValidatorUtil.isNull(payFlow)){
            throw new ServiceException("没有找到支付记录");
        }
        if(!payFlow.getShopId().equals(shopId)){
            throw new ServiceException("该门店没有权限操作此项记录");
        }
        //验证是否允许再次支付
        PayStateEnum payState = PayStateEnum.findByLocalCode(payFlow.getState());
        if(!payState.isAllowReRollback()){
            return PayStateApiMsg.buildFailMsg(payFlow.getPayType(), payState.getDesc(), orderNo, payFlow.getSeriesNo(), payFlow.getAmount().toString(), "");
        }
        IOrderPay orderPay = new OrderPayFactory().getPayInstant(payFlow.getAuthCode(), payFlow.getPayType());
        return orderPay.rollBackPay(payFlow, operator, remark);
    }

    /**
     * 撤销单个充值记录
     * @param seriesNumber
     * @param shopId
     * @param operator
     * @param remark
     * @return
     */
    public ChargeApiMsg rollbackCharge(String seriesNumber, String shopId, String operator, String remark){
        CardFlow cardFlow = cardFlowService.getBySeriesNo(seriesNumber, BusinessTypeEnum.charge.getType());
        if(ValidatorUtil.isNull(cardFlow)){
            throw new ServiceException("流水信息提取失败~");
        }

        if(ValidatorUtil.isNotNull(cardFlow.getIsRollback()) && cardFlow.getIsRollback().equals(1)){
            throw new ServiceException("重复撤销~");
        }

        Card card = cardService.findByCardId(cardFlow.getCardId());
        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("卡信息提取失败~");
        }else if(!cardService.authCheck(card, shopId)){;//验证权限
            throw new ServiceException("当前门店没有权限操作此卡");
        }

        String seriesNo = cardFlowService.generateSeriesNumber(BusinessTypeEnum.rollbackCharge.getPrefix());

        CardFlow newCardFlow = new CardFlow();
        newCardFlow.setCardOwnerShop(card.getShopId().toString());
        newCardFlow.setSeriesNo(seriesNo);
        newCardFlow.setAmount(cardFlow.getAmount());
        newCardFlow.setGiftAmount(cardFlow.getGiftAmount());
        newCardFlow.setOption(BusinessTypeEnum.rollbackCharge.getType());
        newCardFlow.setCardId(cardFlow.getCardId());
        newCardFlow.setCardTagNo(cardFlow.getCardTagNo());
        newCardFlow.setCreateTime(new Date());
        newCardFlow.setCreator(operator);
        newCardFlow.setLastBalance(card.getBalance());
        newCardFlow.setCurBalance(card.getBalance().subtract(cardFlow.getAmount()).subtract(cardFlow.getGiftAmount()));
        newCardFlow.setRemark(remark);
        newCardFlow.setCardUseShop(shopId);
        newCardFlow.setCardMediumKey(card.getMediumKey());
        newCardFlow.setCardMedium(card.getMedium());
        newCardFlow.setLastPoint(card.getPoint());
        newCardFlow.setCurPoint(card.getPoint());
        newCardFlow.setRollbackId(cardFlow.getSeriesNo());
        newCardFlow.setIsPublic(card.getIsPublic());

        //修改旧流水状态
        cardFlow.setIsRollback(1);

        //修改卡信息
        card.setBalance(newCardFlow.getCurBalance());

        cardFlowService.addCardFlow(newCardFlow);
        cardFlowService.updateFlow(cardFlow);
        cardService.editCard(card);

        ChargeApiMsg result = new ChargeApiMsg();
        result.setSeriesNumber(seriesNumber);
        result.setCurBalance(newCardFlow.getCurBalance());
        result.setLastBalance(newCardFlow.getLastBalance());
        result.setGiftAmount(newCardFlow.getGiftAmount());
        result.setChargeAmount(newCardFlow.getAmount());

        return result;
    }

    /**
     * 计算卡可提现金额
     * @param cardId
     * @return
     */
    public WithDrawApiMsg calculateWithDrawAmount(String cardId, String shopId){
        Card card = cardService.findByCardId(cardId);
        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("卡信息提取失败~");
        }else if(!cardService.authCheck(card, shopId)){;//验证权限
            throw new ServiceException("当前门店没有权限操作此卡");
        }

        List<CardFlow> cardFlows = cardFlowService.findByCardIdSortDesc(cardId);

        BigDecimal lastBalance = card.getBalance();
        BigDecimal withDrawAmount = BigDecimal.ZERO;

        //计算可提现金额
        List<Long> rollbackIds = new ArrayList<Long>();
        for (CardFlow cardFlow : cardFlows){
            if(ValidatorUtil.isNotNull(cardFlow.getIsRollback())&&cardFlow.getIsRollback().equals(1)){
                continue;
            }
            if(lastBalance.compareTo(cardFlow.getAmount().add((ValidatorUtil.isNotNull(cardFlow.getGiftAmount())?cardFlow.getGiftAmount():BigDecimal.ZERO)))>=0){//满足撤销条件
                lastBalance = lastBalance.subtract(cardFlow.getAmount().add((ValidatorUtil.isNotNull(cardFlow.getGiftAmount())?cardFlow.getGiftAmount():BigDecimal.ZERO)));
                withDrawAmount = withDrawAmount.add(cardFlow.getAmount());
                rollbackIds.add(cardFlow.getId());
            }else if(lastBalance.compareTo(ValidatorUtil.isNotNull(cardFlow.getGiftAmount())?cardFlow.getGiftAmount():BigDecimal.ZERO) > 0){//剩余非赠送金额可提
                withDrawAmount = withDrawAmount.add(lastBalance.subtract(ValidatorUtil.isNotNull(cardFlow.getGiftAmount())?cardFlow.getGiftAmount():BigDecimal.ZERO));
                rollbackIds.add(cardFlow.getId());
                lastBalance = BigDecimal.ZERO;
            }else {//剩余赠送金额不可提
                break;
            }
        }

        //构造返回参数
        WithDrawApiMsg result = new WithDrawApiMsg();
        result.setLastBalance(card.getBalance());
        result.setWithDrawAmount(withDrawAmount);

        return result;
    }



    /**
     * 卡提现
     * @param cardId
     * @param operator
     * @param remark
     * @return
     */
    public WithDrawApiMsg withDraw(String cardId, String shopId, String operator, String remark){
        Card card = cardService.findByCardId(cardId);
        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("卡信息提取失败~");
        }else if(!cardService.authCheck(card, shopId)){;//验证权限
            throw new ServiceException("当前门店没有权限操作此卡");
        }

        List<CardFlow> cardFlows = cardFlowService.findByCardIdSortDesc(cardId);

        BigDecimal lastBalance = card.getBalance();
        BigDecimal withDrawAmount = BigDecimal.ZERO;

        //计算可提现金额
        List<Long> rollbackIds = new ArrayList<Long>();
        for (CardFlow cardFlow : cardFlows){
            if(ValidatorUtil.isNotNull(cardFlow.getIsRollback())&&cardFlow.getIsRollback().equals(1)){
                continue;
            }
            if(lastBalance.compareTo(cardFlow.getAmount().add(ValidatorUtil.isNotNull(cardFlow.getGiftAmount())?cardFlow.getGiftAmount():BigDecimal.ZERO)) >= 0){//满足撤销条件
                lastBalance = lastBalance.subtract(cardFlow.getAmount().add(ValidatorUtil.isNotNull(cardFlow.getGiftAmount())?cardFlow.getGiftAmount():BigDecimal.ZERO));
                withDrawAmount = withDrawAmount.add(cardFlow.getAmount());
                rollbackIds.add(cardFlow.getId());
            }else if(lastBalance.compareTo(ValidatorUtil.isNotNull(cardFlow.getGiftAmount())?cardFlow.getGiftAmount():BigDecimal.ZERO) > 0){//剩余非赠送金额可提
                withDrawAmount = withDrawAmount.add(lastBalance).subtract(ValidatorUtil.isNotNull(cardFlow.getGiftAmount())?cardFlow.getGiftAmount():BigDecimal.ZERO);
                rollbackIds.add(cardFlow.getId());
                lastBalance = BigDecimal.ZERO;
            }else {//剩余赠送金额不可提
                break;
            }
        }

        if(withDrawAmount.compareTo(BigDecimal.ZERO)<=0){
            throw new ServiceException("当前余额￥" + withDrawAmount + "不可提现~");
        }

        //更新充值记录为已撤销
        if(rollbackIds.size() > 0){
            /*CardFlow record = new CardFlow();
            record.setIsRollback(1);
            CardFlowExample example1 = new CardFlowExample();
            example1.createCriteria().andIdIn(rollbackIds);
            cardFlowService.updateByExample(record, example1);*/
            // TODO: 2017/3/23 明天写接口，请求服务器数据
        }

        //插入提现流水信息
        CardFlow newCardFlow = new CardFlow();
        newCardFlow.setCardOwnerShop(card.getShopId().toString());
        newCardFlow.setSeriesNo(cardFlowService.generateSeriesNumber(BusinessTypeEnum.withdraw.getPrefix()));
        newCardFlow.setAmount(card.getBalance());
        newCardFlow.setOption(BusinessTypeEnum.withdraw.getType());
        newCardFlow.setCardId(cardId);
        newCardFlow.setCardTagNo(card.getTagId());
        newCardFlow.setCreateTime(new Date());
        newCardFlow.setCreator(operator);
        newCardFlow.setLastBalance(card.getBalance());
        newCardFlow.setCurBalance(BigDecimal.ZERO);
        newCardFlow.setRemark(remark);
        newCardFlow.setCardUseShop(shopId);
        newCardFlow.setCardMediumKey(card.getMediumKey());
        newCardFlow.setCardMedium(card.getMedium());
        newCardFlow.setLastPoint(card.getPoint());
        newCardFlow.setCurPoint(card.getPoint());
        newCardFlow.setIsPublic(card.getIsPublic());
        cardFlowService.addCardFlow(newCardFlow);

        //构造返回参数
        WithDrawApiMsg result = new WithDrawApiMsg();
        result.setLastBalance(card.getBalance());
        result.setWithDrawAmount(withDrawAmount);
        result.setSeriesNumber(newCardFlow.getSeriesNo());

        //修改卡信息
        card.setBalance(BigDecimal.ZERO);
        cardService.editCard(card);

        return result;
    }

    /**
     * 注销卡
     * @param cardId
     * @param operator
     * @param remark
     * @return
     */
    public ReturnCardApiMsg disableCard(String cardId, String shopId, String operator, String remark){
        Card card = cardService.findByCardId(cardId);
        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("卡信息提取失败~");
        }else if(!cardService.authCheck(card, shopId)){;//验证权限
            throw new ServiceException("当前门店没有权限操作此卡");
        }else if(CardStateEnum.disabled.getCode().equals(card.getCardState())){
            throw new ServiceException("不可重复退还");
        }
        WithDrawApiMsg withDrawMsg = calculateWithDrawAmount(cardId, shopId);
        if(withDrawMsg.getWithDrawAmount().compareTo(BigDecimal.ZERO) > 0){
            withDrawMsg = withDraw(cardId, shopId, operator, remark);
        }
        cardService.deleteCard(new ObjectId(cardId));

        String seriesNumber = cardFlowService.generateSeriesNumber(BusinessTypeEnum.returnCard.getPrefix());

        //插入提现流水信息
        CardFlow newCardFlow = new CardFlow();
        newCardFlow.setCardOwnerShop(card.getShopId().toString());
        newCardFlow.setSeriesNo(seriesNumber);
        newCardFlow.setAmount(BigDecimal.ZERO);
        newCardFlow.setOption(BusinessTypeEnum.returnCard.getType());
        newCardFlow.setCardId(cardId);
        newCardFlow.setCardTagNo(card.getTagId());
        newCardFlow.setCreateTime(new Date());
        newCardFlow.setCreator(operator);
        newCardFlow.setLastBalance(card.getBalance());
        newCardFlow.setCurBalance(BigDecimal.ZERO);
        newCardFlow.setLastPoint(card.getPoint());
        newCardFlow.setCurPoint(0);
        newCardFlow.setRemark(remark);
        newCardFlow.setCardUseShop(shopId);
        newCardFlow.setCardMediumKey(card.getMediumKey());
        newCardFlow.setCardMedium(card.getMedium());
        newCardFlow.setIsPublic(card.getIsPublic());
        cardFlowService.addCardFlow(newCardFlow);

        ReturnCardApiMsg returnCardMsg = new ReturnCardApiMsg();
        returnCardMsg.setWithDrawAmount(withDrawMsg.getWithDrawAmount());
        returnCardMsg.setLastBalance(withDrawMsg.getLastBalance());
        returnCardMsg.setSeriesNumber(seriesNumber);

        return returnCardMsg;
    }

    /**
     * 激活卡
     * @param cardId
     * @param operator
     * @param remark
     * @return
     */
    public BaseApiResultMsg activateCard(String cardId, String shopId, String operator, String remark){
        Card card = cardService.findByCardId(cardId);

        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("不存在该卡号~");
        }else if(!cardService.authCheck(card, shopId)){;//验证权限
            throw new ServiceException("当前门店没有权限操作此卡");
        }else if(card.getCardState() > CardStateEnum.newCard.getCode()){
            throw new ServiceException("不可重复激活~");
        }

        //插入激活流水信息
        String seriesNumber = cardFlowService.generateSeriesNumber(BusinessTypeEnum.activate.getPrefix());
        CardFlow newCardFlow = new CardFlow();
        newCardFlow.setCardOwnerShop(card.getShopId().toString());
        newCardFlow.setSeriesNo(seriesNumber);
        newCardFlow.setAmount(BigDecimal.ZERO);
        newCardFlow.setOption(BusinessTypeEnum.activate.getType());
        newCardFlow.setCardId(cardId);
        newCardFlow.setCardTagNo(card.getTagId());
        newCardFlow.setCreateTime(new Date());
        newCardFlow.setCreator(operator);
        newCardFlow.setLastBalance(card.getBalance());
        newCardFlow.setCurBalance(card.getBalance());
        newCardFlow.setLastPoint(card.getPoint());
        newCardFlow.setCurPoint(card.getPoint());
        newCardFlow.setRemark(remark);
        newCardFlow.setCardUseShop(shopId);
        newCardFlow.setCardMediumKey(card.getMediumKey());
        newCardFlow.setCardMedium(card.getMedium());
        newCardFlow.setIsPublic(card.getIsPublic());
        cardFlowService.addCardFlow(newCardFlow);

        Query query = new Query(Criteria.where("_id").is(card.get_id()));
        Update update = new Update().set("cardState", CardStateEnum.activated.getCode()).set("activateTime", new Date());
        cardService.getMongoTemplate().updateFirst(query, update, Card.class);
        return new BaseApiResultMsg();
    }

    /**
     * 折扣
     * @param cardId
     * @param orderNo
     * @param orderAmount
     * @param discountAmount
     * @param discountRuleName
     * @param shopId
     * @param operator
     * @param remark
     * @return
     */
    public BaseApiResultMsg makeDiscount(String cardId, String orderNo, BigDecimal orderAmount, BigDecimal discountAmount, String discountRuleName, String shopId, String operator, String remark){
        Card card = cardService.findByCardId(cardId);
        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("不存在该卡券号~");
        }else if(!cardService.authCheck(card, shopId)){//验证权限
            throw new ServiceException("当前门店没有权限操作此卡");
        }
        if(!CardStateEnum.activated.getCode().equals(card.getCardState())){
            throw new ServiceException("卡券" + CardStateEnum.getNameByCode(card.getCardState()) + ",不可使用");
        }

        String seriesNumber = cardFlowService.generateSeriesNumber(BusinessTypeEnum.makeDiscount.getPrefix());
        //保存卡流水信息
        if(ValidatorUtil.isNotNull(discountAmount)){
            CardFlow cardFlow = new CardFlow();
            cardFlow.setCardOwnerShop(card.getShopId().toString());
            cardFlow.setSeriesNo(seriesNumber);
            cardFlow.setAmount(discountAmount);
            cardFlow.setOption(BusinessTypeEnum.makeDiscount.getType());
            cardFlow.setCardId(cardId);
            cardFlow.setCardTagNo(card.getTagId());
            cardFlow.setOrderNo(orderNo);
            cardFlow.setCreateTime(new Date());
            cardFlow.setCreator(operator);
            cardFlow.setRemark(remark);
            cardFlow.setOrderAmount(orderAmount);
//            cardFlow.setDiscountRuleName(discountRuleName);
            cardFlow.setCardUseShop(shopId);
            cardFlow.setCardMediumKey(card.getMediumKey());
            cardFlow.setCardMedium(card.getMedium());
            cardFlow.setLastPoint(card.getPoint());
            cardFlow.setCurPoint(card.getPoint());
            cardFlow.setIsPublic(card.getIsPublic());

            cardFlowService.addCardFlow(cardFlow);
        }

        //修改券状态为已使用
        if(CardTypeEnum.coupon.getCode().equals(card.getMedium())){
            couponService.couponUseAble(new ObjectId(cardId));
        }

        return new DiscountApiMsg(seriesNumber);
    }

    /**
     * 撤销使用折扣
     * @param seriesNumber
     * @param operator
     * @param remark
     * @return
     */
    public BaseApiResultMsg rollbackDiscount(String seriesNumber, String shopId, String operator, String remark){
        //撤销卡充值流水
        CardFlow cardFlow = cardFlowService.getBySeriesNo(seriesNumber, BusinessTypeEnum.makeDiscount.getType());

        if(ValidatorUtil.isNotNull(cardFlow.getIsRollback()) && cardFlow.getIsRollback().equals(1)){
            throw new ServiceException("重复撤销~");
        }

        Card card = cardService.findByCardId(cardFlow.getCardId());
        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("卡信息提取失败~");
        }else if(!cardService.authCheck(card, shopId)){//验证权限
            throw new ServiceException("当前门店没有权限操作此卡");
        }

        String seriesNo = cardFlowService.generateSeriesNumber(BusinessTypeEnum.rollBackDiscount.getPrefix());

        CardFlow newCardFlow = new CardFlow();
        newCardFlow.setCardOwnerShop(cardFlow.getCardOwnerShop());
        newCardFlow.setSeriesNo(seriesNo);
        newCardFlow.setOrderAmount(cardFlow.getOrderAmount());
        newCardFlow.setAmount(cardFlow.getAmount());
        newCardFlow.setOption(BusinessTypeEnum.rollBackDiscount.getType());
        newCardFlow.setCardId(cardFlow.getCardId());
        newCardFlow.setCardTagNo(cardFlow.getCardTagNo());
        newCardFlow.setCreateTime(new Date());
        newCardFlow.setCreator(operator);
        newCardFlow.setRemark(remark);
        newCardFlow.setCardUseShop(shopId);
        newCardFlow.setCardMediumKey(card.getMediumKey());
        newCardFlow.setCardMedium(card.getMedium());
        newCardFlow.setLastPoint(card.getPoint());
        newCardFlow.setCurPoint(card.getPoint());
        newCardFlow.setRollbackId(cardFlow.getSeriesNo());
        newCardFlow.setIsPublic(card.getIsPublic());

        //修改旧流水状态
        cardFlow.setIsRollback(1);

        cardFlowService.addCardFlow(newCardFlow);
        cardFlowService.updateFlow(cardFlow);
        //修改券信息
        if(CardTypeEnum.coupon.getCode().equals(card.getMedium())){
            card.setCardState(CardStateEnum.activated.getCode());
            cardService.editCard(card);
        }
        return new DiscountApiMsg(seriesNumber);
    }

}
