package com.covilla.service.order;

import com.covilla.common.*;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.OrderMongoDao;
import com.covilla.model.CardFlow;
import com.covilla.model.mongo.card.Card;
import com.covilla.model.mongo.card.CardType;
import com.covilla.model.mongo.order.Order;
import com.covilla.model.mongo.order.PayItem;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.service.api.CardApiService;
import com.covilla.service.card.CardFlowService;
import com.covilla.service.card.CardService;
import com.covilla.service.card.CardTypeService;
import com.covilla.util.DateUtil;
import com.covilla.util.RandomUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.filter.QueryFilter;
import com.covilla.weixin.OrderMessage;
import jodd.typeconverter.Convert;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qmaolong on 2016/10/13.
 */
@Service
@Transactional
public class OrderService extends BaseMongoService<Order> {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderMongoDao orderDao;
    @Autowired
    private OrderMongoDao orderMongoDao;
    @Autowired
    private CardService cardService;
    @Autowired
    private CardFlowService cardFlowService;
    @Autowired
    private CardTypeService cardTypeService;
    @Autowired
    private CardApiService cardApiService;
    private static AtomicInteger ATOMIC = new AtomicInteger(1);
    protected BaseMongoDao<Order> getBaseMongoDao(){
        return orderMongoDao;
    }


    /**
     * 获取所有销售订单
     * @param filter
     * @return
     */
    /*public List<Order> findAllBusinessOrder(QueryFilter filter){
        return orderDao.findByFilter(filter, OrderTypeEnum.getBusinessTypes(), null);
    }*/

    /**
     * 获取所有营业订单
     * @param filter
     * @return
     */
    public List<Order> findOrdersByFilterAndType(QueryFilter filter, List<Integer> types){
        List<Order> orderList = orderDao.findByFilter(filter, types, null);
        List<Order> result = new ArrayList<Order>();

        for (Order order : orderList){
            if(ValidatorUtil.isNotNull(filter.getDayOn())){
                Integer dayOn = Convert.toInteger(filter.getDayOn().replaceAll(":", ""));
                Integer orderPayTime = Convert.toInteger(DateUtil.formateDateToStr(order.getPayTime(), "HHmm"));
                if (dayOn > orderPayTime){
                    continue;
                }
            }
            if(ValidatorUtil.isNotNull(filter.getDayOff())){
                Integer dayOff = Convert.toInteger(filter.getDayOff().replaceAll(":", ""));
                Integer orderPayTime = Convert.toInteger(DateUtil.formateDateToStr(order.getPayTime(), "HHmm"));
                if (dayOff < orderPayTime){
                    continue;
                }
            }
            result.add(order);
        }
        return result;
    }

    //当月已支付业务订单
    public List<Order> findCurrentMonthOrders(ObjectId shopId){
        Date firstDay = DateUtil.formateDate(new Date(), "yyyy-MM-01");
        Query query = new Query(Criteria.where("payTime").gte(firstDay).and("shopId").is(shopId).and("orderType").in(OrderTypeEnum.getBusinessTypes()).and("state").in(OrderStateEnum.getPayedStates()));
        List<Order> orderList = getMongoTemplate().find(query, Order.class);
        return orderList;
    }

    //一周已支付业务订单
    public List<Order> findCurrentWeekOrders(ObjectId shopId){
        Date minDate = DateUtil.addDay(DateUtil.formateDate(new Date(), "yyyy-MM-dd"), -6);
        Query query = new Query(Criteria.where("payTime").gte(minDate).and("shopId").is(shopId).and("orderType").in(OrderTypeEnum.getBusinessTypes()).and("state").in(OrderStateEnum.getPayedStates()));
        List<Order> orderList = getMongoTemplate().find(query, Order.class);
        return orderList;
    }

    /**
     * 增加后台充值订单
     * @param
     */
    public void addChargeOrder(String orderNo, BigDecimal chargeAmount, BigDecimal giftAmount, ObjectId shopId, ObjectId cardId){
        Order order = new Order();
        order.setOrderType(OrderTypeEnum.chargeOrder.getCode());
        order.setOrderTypeName(OrderTypeEnum.chargeOrder.getName());
        order.setOrderTime(new Date());
        order.setId(orderNo);
        order.setTotalAmount(chargeAmount);
        order.setGivenAmount(giftAmount);
        order.setShopId(shopId);
        order.setCard(cardId);
        order.setAmount(chargeAmount);
        order.setActualAmount(chargeAmount);
        order.setCashAmount(chargeAmount);
        order.setPayTime( new Date());
        order.setOperatorName("后台管理员");

        List<PayItem> payItems = new ArrayList<PayItem>();
        PayItem payItem = new PayItem();
        payItem.setAmount(chargeAmount);
        payItem.setType(0);
        payItems.add(payItem);
        order.setPayItems(payItems);
        insert(order);
    }

    public String generateOrderNo(){
        String no = ATOMIC.getAndDecrement()%100 + 100 + "";
        return DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss") + RandomUtil.genRandomNumberString(2) + no;
    }

    public Order findRollbackOrder(String orderNo, ObjectId shopId){
        Query query = new Query(Criteria.where("refId").is(orderNo).and("orderType").is(OrderTypeEnum.rollbackOrder.getCode()).and("shopId").is(shopId));
        List<Order> orders = getMongoTemplate().find(query, Order.class);
        if (ValidatorUtil.isNotNull(orders))
            return orders.get(0);
        else
            return null;
    }

    /**
     * 订单完成
     * @return
     */
    public OrderMessage completeOrder(Order order){
        String orderNo = order.getId();
        ObjectId shopId = order.getShopId();
        ObjectId cardId = order.getCard();
        BigDecimal orderAmount = order.getAmount();

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setTime(new Date());
        orderMessage.setCardId(cardId.toString());
        orderMessage.setOrderId(orderNo);
        orderMessage.setShopId(shopId.toString());
        orderMessage.setType(0);//消费

        if (!OrderStateEnum.payedOrder.getCode().equals(order.getState())){
            throw new ServiceException("非已付款订单");
        }

        //卡相关
        if (ValidatorUtil.isNotNull(cardId)){
            Card card = cardService.findBy_id(cardId);
            if (ValidatorUtil.isNull(card)){
                throw new ServiceException("卡获取失败");
            }
            CardType cardType = cardTypeService.findBy_id(card.getCardTypeId());
            //积分
            if (ValidatorUtil.isNotNull(cardType) && ValidatorUtil.isNotNull(cardType.getIsSupportPoint()) && cardType.getIsSupportPoint()){
                String seriesNumber = cardFlowService.generateSeriesNumber(BusinessTypeEnum.addPoint.getPrefix());
                CardFlow cardFlow = new CardFlow();
                cardFlow.setCardOwnerShop(card.getShopId().toString());
                cardFlow.setSeriesNo(seriesNumber);
                cardFlow.setAmount(orderAmount);
                cardFlow.setOption(BusinessTypeEnum.addPoint.getType());
                cardFlow.setCardId(cardId.toString());
                cardFlow.setCardTagNo(card.getTagId());
                cardFlow.setOrderNo(orderNo);
                cardFlow.setCreateTime(new Date());
                cardFlow.setOrderAmount(orderAmount);
                cardFlow.setCardUseShop(shopId.toString());
                cardFlow.setCardMediumKey(card.getMediumKey());
                cardFlow.setCardMedium(card.getMedium());

                Integer lastPoint = ValidatorUtil.isNull(card.getPoint())?0:card.getPoint();
                cardFlow.setLastPoint(lastPoint);
                cardFlow.setCurPoint(lastPoint + orderAmount.intValue());
                cardFlow.setIsPublic(card.getIsPublic());

                cardFlowService.addCardFlow(cardFlow);
                card.setPoint(cardFlow.getCurPoint());

                orderMessage.setPoint(cardFlow.getCurPoint());
                orderMessage.setChangePoint(order.getAmount().intValue());
            }

            orderMessage.setAmount(card.getBalance());
            orderMessage.setChangeAmount(order.getAmount());

            //推送券给微会员
            if (CardTypeEnum.virtual.getCode().equals(card.getMedium())){
                cardService.sendCouponToCardByOrder(shopId, order);
            }
            cardService.updateDocument(card);
        }
        return orderMessage;
    }

    /**
     * 撤销订单
     * @return
     */
    public OrderMessage rollbackOrder(Order order){
        Order originOrder = findById(order.getRefId());

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setTime(new Date());
        orderMessage.setAmount(originOrder.getAmount());
        orderMessage.setOrderId(order.getId());
        orderMessage.setShopId(order.getShopId().toString());
        orderMessage.setType(2);//撤销消费

        //撤销卡相关操作
        if (ValidatorUtil.isNotNull(order.getCard())){
            orderMessage.setCardId(originOrder.getCard().toString());
            Card card = cardService.findBy_id(originOrder.getCard());

            //撤销卡积分
            CardFlow cardFlow = cardFlowService.findAddPointFlow(originOrder.getId(), originOrder.getShopId().toString());
            if(ValidatorUtil.isNull(cardFlow.getIsRollback()) || !cardFlow.getIsRollback().equals(1)){
                String newSeriesNo = cardFlowService.generateSeriesNumber(BusinessTypeEnum.rollbackPoint.getPrefix());
                CardFlow newCardFlow = new CardFlow();
                newCardFlow.setCardOwnerShop(cardFlow.getCardOwnerShop());
                newCardFlow.setSeriesNo(newSeriesNo);
                newCardFlow.setOrderAmount(cardFlow.getOrderAmount());
                newCardFlow.setAmount(cardFlow.getAmount());
                newCardFlow.setOption(BusinessTypeEnum.rollbackPoint.getType());
                newCardFlow.setCardId(cardFlow.getCardId());
                newCardFlow.setCardTagNo(cardFlow.getCardTagNo());
                newCardFlow.setCreateTime(new Date());
                newCardFlow.setCardUseShop(order.getShopId().toString());
                newCardFlow.setCardMediumKey(card.getMediumKey());
                newCardFlow.setCardMedium(card.getMedium());

                Integer lastPoint = ValidatorUtil.isNull(card.getPoint())?0:card.getPoint();
                newCardFlow.setLastPoint(lastPoint);
                newCardFlow.setCurPoint(lastPoint - cardFlow.getAmount().intValue());
                newCardFlow.setRollbackId(cardFlow.getSeriesNo());
                newCardFlow.setIsPublic(card.getIsPublic());

                cardFlowService.addCardFlow(newCardFlow);

                //修改旧流水状态
                cardFlow.setIsRollback(1);
                cardFlowService.updateFlow(cardFlow);

                orderMessage.setPoint(newCardFlow.getCurPoint());
                orderMessage.setChangePoint(originOrder.getAmount().intValue() * -1);
                orderMessage.setChangeAmount(originOrder.getAmount());
                orderMessage.setAmount(card.getBalance());

                //修改卡信息
                card.setPoint(newCardFlow.getCurPoint());
                cardService.updateDocument(card);
            }

            orderMessage.setChangeAmount(order.getAmount());
            orderMessage.setAmount(card.getBalance());

            //收回已推送给微会员的券
            if (CardTypeEnum.virtual.getCode().equals(card.getMedium())){
                List<Card> cards = cardService.findCouponsSendByOrderId(order.getId(), order.getShopId());
                if (ValidatorUtil.isNotNull(cards)){
                    for (Card coupon : cards){
                        if (CardStateEnum.used.getCode().equals(coupon.getCardState())){
                            continue;
                        }
                        coupon.setCardState(CardStateEnum.disabled.getCode());
                        cardService.updateDocument(coupon);
                    }
                }
            }
        }

        //恢复折扣券状态
        List<CardFlow> discountFlows = cardFlowService.findDiscountFlows(originOrder.getId(), originOrder.getShopId().toString());
        if (ValidatorUtil.isNotNull(discountFlows)){
            for (CardFlow discountFlow : discountFlows){
                try {
                    cardApiService.rollbackDiscount(discountFlow.getSeriesNo(), discountFlow.getCardUseShop(), null, null);
                }catch (ServiceException se){
                    logger.error(String.format("折扣回退失败，tagId：%s", discountFlow.getCardTagNo()));
                    continue;
                }
            }
        }

        return orderMessage;
    }

}
