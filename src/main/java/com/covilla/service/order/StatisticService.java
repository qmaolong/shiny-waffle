package com.covilla.service.order;

import com.covilla.common.*;
import com.covilla.model.CardFlow;
import com.covilla.model.mongo.order.Order;
import com.covilla.model.mongo.order.OrderItem;
import com.covilla.model.mongo.order.PayItem;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.card.CardFlowService;
import com.covilla.service.food.CategoryService;
import com.covilla.service.pay.PayFlowService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.DateUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.filter.QueryFilter;
import com.covilla.vo.result.BusinessData;
import com.covilla.vo.result.CardData;
import com.covilla.vo.result.FoodSellData;
import jodd.typeconverter.Convert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 数据统计
 * Created by qmaolong on 2016/12/20.
 */
@Service
public class StatisticService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayFlowService payFlowService;
    @Autowired
    private CardFlowService cardFlowService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CategoryService categoryService;

    //统计一周销售状况
    public Map<String, Object> calWeekData(List<Order> orderList){
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> dayList = new ArrayList<String>();
        List<BigDecimal> amountList = new ArrayList<BigDecimal>();
        for (int i=0; i<7; i++){
            Date date = DateUtil.addDay(new Date(), i*-1);
            dayList.add(DateUtil.formateDateToStr(date, "MM月dd日"));
            BigDecimal amount = BigDecimal.ZERO;
            for (Order order: orderList){
                if(DateUtil.isSameDay(date, order.getPayTime())&& OrderStateEnum.payedOrder.getCode().equals(order.getState())){
                    for (PayItem item : order.getPayItems()){
                        amount = amount.add(item.getAmount());
                    }
                }
            }
            amountList.add(amount);
        }
        result.put("days", dayList);
        result.put("amounts", amountList);

        return result;
    }
    //统计一周食品销售状况
    public Map<String, Object> calWeekFood(List<Order> orderList){
        Map<String, Object> result = new HashMap<String, Object>();

        List<FoodSellData> foodSellDatas = calFoodSellData(orderList);

        //重新排序
        Collections.sort(foodSellDatas, new Comparator<FoodSellData>(){
            public int compare(FoodSellData storeSuiteType1, FoodSellData storeSuiteType2){
                Integer count1 = Convert.toInteger(storeSuiteType1.getTotalCount());
                Integer count2 = Convert.toInteger(storeSuiteType2.getTotalCount());
                return count2.compareTo(count1);
            }
        });

        List<String> names = new ArrayList<String>();
        List<Integer> counts = new ArrayList<Integer>();
        int i = 0;
        for(FoodSellData item : foodSellDatas){
            names.add(item.getOrderItem().getFoodName());
            counts.add(item.getTotalCount());
            ++i;
            if(i>=10){
                break;
            }
        }
        result.put("names", names);
        result.put("values", counts);

        return result;
    }

    public List<FoodSellData> calFoodSellData(List<Order> orderList){
        Map<String, FoodSellData> foodItem = new HashMap<String, FoodSellData>();
        for (Order order : orderList){
            if(ValidatorUtil.isNull(order.getOrderItems())){
                continue;
            }
            for (OrderItem orderItem : order.getOrderItems()){
                //筛选费赠送、非子菜项
                if(!orderItem.getGiven()&&!orderItem.getSpecial()&& !OrderItem.FoodTypeEnum.suiteFood.getCode().equals(orderItem.getFoodType())){
                    if(ValidatorUtil.isNotNull(foodItem.get(orderItem.getFoodId()))){
                        FoodSellData temp = foodItem.get(orderItem.getFoodId());
                        temp.setTotalCount(temp.getTotalCount()+orderItem.getCount());
                        temp.setTotalAmount(temp.getTotalAmount()+orderItem.getCount()*orderItem.getPrice());
                    }else {
                        FoodSellData temp = new FoodSellData();
                        temp.setTotalCount(orderItem.getCount());
                        temp.setTotalAmount(orderItem.getCount()*orderItem.getPrice());
                        temp.setOrderItem(orderItem);
                        foodItem.put(orderItem.getFoodId(), temp);
                    }
                }
            }
        }

        List<FoodSellData> items = new ArrayList<FoodSellData>();
        for(Map.Entry<String,  FoodSellData> entry:foodItem.entrySet()){
            items.add(entry.getValue());
        }

        return items;
    }

    //当月收入
    public BigDecimal monthIncome(List<Order> orderList){
        BigDecimal result = BigDecimal.ZERO;
        for (Order order : orderList){
            if(OrderStateEnum.payedOrder.getCode().equals(order.getState())){
                for (PayItem item : order.getPayItems()){
                    result = result.add(item.getAmount());
                }
            }
        }
        return result;
    }

    //当月客流量
    public Integer monthPeople(List<Order> orderList){
        Integer result = 0;
        for (Order order : orderList){
            result += order.getPeople();
        }
        return result;
    }

    //一段时间段客流量
    public Integer dayPeople(Date startDate, Date endDate, ObjectId shopId){
        Query query = new Query(Criteria.where("payTime").gte(startDate).lt(DateUtil.addDay(endDate, 1)).and("shopId").is(shopId).and("orderType").in(OrderTypeEnum.getBusinessTypes()));
        List<Order> orderList = orderService.getMongoTemplate().find(query, Order.class);
        Integer result = 0;
        for (Order order : orderList){
            result += order.getPeople();
        }
        return result;
    }

    /**
     * 计算订单数据
     * @param filter
     * @param orderList
     * @return
     */
    public BusinessData calBusinessData(QueryFilter filter, List<Order> orderList, List<CardFlow> cardFlowList){
        BusinessData result = new BusinessData();
        String dateStr = DateUtil.dateSection(filter.getStartDate(), filter.getEndDate());
        result.setDateStr(dateStr);
        if(ValidatorUtil.isNotNull(filter.getShopId())){
            Shop shop = shopService.findBy_id(new ObjectId(filter.getShopId()));
            result.setShopName(shop.getName());
            result.setSuperFlag(shop.getSuperFlag());
        }else {
            result.setShopName("全部");
            result.setSuperFlag(null);
        }

        for (Order order : orderList){
            if (!OrderStateEnum.payedOrder.getCode().equals(order.getState())){
                continue;
            }
            if (OrderTypeEnum.getBusinessTypes().indexOf(order.getOrderType()) >= 0){//营业订单
                result.setOrderCount(result.getOrderCount() + 1);
                result.setPeople(result.getPeople() + order.getPeople());
                result.setSubAmount(result.getSubAmount().add(order.getSubAmount()));
                result.setAmount(result.getAmount().add(order.getAmount()));
                for (PayItem item : order.getPayItems()){
                    if(0== item.getType()){
                        result.setCashAmount(result.getCashAmount().add(item.getAmount()));
                    }else if(1 == item.getType()){
                        result.setCardAmount(result.getCardAmount().add(item.getAmount()));
                    }else if(2 == item.getType()){
                        result.setBankAmount(result.getBankAmount().add(item.getAmount()));
                    }else if(3 == item.getType()){
                        result.setCashAmount(result.getCashAmount().add(item.getAmount()));
                    }else if(4 == item.getType()){
                        result.setCouponAmount(result.getCouponAmount().add(item.getAmount()));
                    }else if(5 == item.getType()){
                        result.setOtherAmount(result.getOtherAmount().add(item.getAmount()));
                    }
                    if (OrderTypeEnum.deskOrder.getCode().equals(order.getOrderType())){
                        result.setDeskOrderAmount(result.getDeskOrderAmount().add(item.getAmount()));
                    }else if(OrderTypeEnum.fastOrder.getCode().equals(order.getOrderType())){
                        result.setFastOrderAmount(result.getFastOrderAmount().add(item.getAmount()));
                    }else if (OrderTypeEnum.takeOut.getCode().equals(order.getOrderType())){
                        result.setTakeOutAmount(result.getTakeOutAmount().add(item.getAmount()));
                    }
                    result.setBusinessIncome(result.getBusinessIncome().add(item.getAmount()));
                }
                result.setDiscountAmount(result.getDiscountAmount().add(order.getDiscountAmount()));
                result.setSpecialDiscountAmount(result.getSpecialDiscountAmount().add(order.getSpecialDiscountAmount()));
                result.setItemDiscountAmount(result.getItemDiscountAmount().add(order.getItemDiscountAmount()));

            }else if (OrderTypeEnum.otherOrder.getCode().equals(order.getOrderType())){//其他订单
                result.setOtherAmount(result.getOtherAmount().add(order.getAmount()));
            }else {//卡相关
                /*if(OrderTypeEnum.chargeOrder.getCode().equals(order.getOrderType())){
                    result.setChargeAmount(order.getAmount());
                }else if(OrderTypeEnum.returnCard.getCode().equals(order.getOrderType())){
                    result.setReturnCardAmount(result.getReturnCardAmount().add(order.getAmount()));
                }*/
            }
        }

        //卡相关
        for (CardFlow cardFlow : cardFlowList){
            if(ValidatorUtil.isNotNull(cardFlow.getIsRollback()) && 1 == cardFlow.getIsRollback()){
                continue;
            }
            if(BusinessTypeEnum.charge.getType().equals(cardFlow.getOption())){
                result.setChargeAmount(result.getChargeAmount().add(cardFlow.getAmount()));
            }else if(BusinessTypeEnum.returnCard.getType().equals(cardFlow.getOption())){
                result.setReturnCardAmount(result.getReturnCardAmount().add(cardFlow.getAmount()));
            }else if(BusinessTypeEnum.withdraw.getType().equals(cardFlow.getOption())){
                result.setReturnCardAmount(result.getReturnCardAmount().add(cardFlow.getAmount()));
            }else if (BusinessTypeEnum.makeDiscount.getType().equals(cardFlow.getOption())){
                if (CardTypeEnum.coupon.getCode().equals(cardFlow.getCardMedium())){
                    result.setCouponDiscountAmount(result.getCouponDiscountAmount().add(cardFlow.getAmount()));
                }else {
                    result.setCardDiscountAmount(result.getCardDiscountAmount().add(cardFlow.getAmount()));
                }
            }
        }
        result.setTotalIncome(result.getBusinessIncome().add(result.getOtherAmount()).add(result.getChargeAmount()).subtract(result.getCardAmount()).subtract(result.getReturnCardAmount()));

        return result;
    }

    /**
     * 根据对比filter获取多个查询filter
     * @param compareFilter
     * @return
     */
    public List<QueryFilter> generateQueryFiltersByCompareFilter(QueryFilter compareFilter){
        if(ValidatorUtil.isNull(compareFilter.getEndDate())){
            compareFilter.setEndDate(new Date());
        }
        List<QueryFilter> result = new ArrayList<QueryFilter>();
        if(QueryFilter.PeriodEnum.week.getName().equals(compareFilter.getPeriod())){//以周为单位
            Date firstDate = compareFilter.getStartDate();
            Integer weekDay = firstDate.getDay();

            firstDate = DateUtil.addDay(firstDate, compareFilter.getSpecialTimeOn() - weekDay);
            //第一个周期
            QueryFilter filter = new QueryFilter();
            filter.setShopId(compareFilter.getShopId());
            filter.setShopIds(compareFilter.getShopIds());
            filter.setStartDate(firstDate);
            filter.setEndDate(DateUtil.addDay(firstDate, compareFilter.getSpecialTimeOff() - compareFilter.getSpecialTimeOn()));
            filter.setDayOn(compareFilter.getDayOn());
            filter.setDayOff(compareFilter.getDayOff());
            result.add(filter);
            firstDate = DateUtil.addDay(firstDate, 7);

            while (true){
                QueryFilter temp = new QueryFilter();
                temp.setShopId(compareFilter.getShopId());
                temp.setShopIds(compareFilter.getShopIds());
                temp.setStartDate(firstDate);
                temp.setEndDate(DateUtil.addDay(firstDate, compareFilter.getSpecialTimeOff() - compareFilter.getSpecialTimeOn()));
                temp.setDayOn(compareFilter.getDayOn());
                temp.setDayOff(compareFilter.getDayOff());
                result.add(temp);
                firstDate = DateUtil.addDay(firstDate, 7);
                if(firstDate.after(compareFilter.getEndDate())){
                    break;
                }
            }
        }else if(QueryFilter.PeriodEnum.month.getName().equals(compareFilter.getPeriod())){//以月为单位
            Date firstDate = compareFilter.getStartDate();
            if(compareFilter.getStartDate().getDate() > compareFilter.getSpecialTimeOff()){
                DateUtil.addMonth(firstDate, 1);
            }
            firstDate.setDate(compareFilter.getSpecialTimeOn());
            QueryFilter filter = new QueryFilter();
            filter.setShopId(compareFilter.getShopId());
            filter.setShopIds(compareFilter.getShopIds());
            filter.setStartDate(firstDate);
            filter.setEndDate(DateUtil.addDay(firstDate, compareFilter.getSpecialTimeOff() - compareFilter.getSpecialTimeOn()));
            filter.setDayOn(compareFilter.getDayOn());
            filter.setDayOff(compareFilter.getDayOff());
            result.add(filter);
            firstDate = DateUtil.addMonth(firstDate, 1);
            while (true){
                QueryFilter temp = new QueryFilter();
                temp.setShopId(compareFilter.getShopId());
                filter.setShopIds(compareFilter.getShopIds());
                temp.setStartDate(firstDate);
                temp.setEndDate(DateUtil.addDay(firstDate, compareFilter.getSpecialTimeOff() - compareFilter.getSpecialTimeOn()));
                temp.setDayOn(compareFilter.getDayOn());
                temp.setDayOff(compareFilter.getDayOff());
                result.add(temp);
                firstDate = DateUtil.addMonth(firstDate, 1);
                if(firstDate.after(compareFilter.getEndDate())){
                    break;
                }
            }
        }else if(QueryFilter.PeriodEnum.year.getName().equals(compareFilter.getPeriod())){//以年为单位
            Date firstDate = DateUtil.formateDate(compareFilter.getStartDate(), "yyyy-MM-01");
            if (compareFilter.getStartDate().getMonth() > compareFilter.getSpecialTimeOff()){
                firstDate = DateUtil.addYear(firstDate, 1);
            }
            firstDate.setMonth(compareFilter.getSpecialTimeOn() - 1);
            QueryFilter filter = new QueryFilter();
            filter.setShopId(compareFilter.getShopId());
            filter.setStartDate(firstDate);
            filter.setEndDate(DateUtil.addDay(DateUtil.addMonth(firstDate, compareFilter.getSpecialTimeOff() - compareFilter.getSpecialTimeOn()), -1));
            filter.setDayOn(compareFilter.getDayOn());
            filter.setDayOff(compareFilter.getDayOff());
            result.add(filter);
            firstDate = DateUtil.addYear(firstDate, 1);
            while (true){
                QueryFilter temp = new QueryFilter();
                temp.setShopId(compareFilter.getShopId());
                temp.setStartDate(firstDate);
                temp.setEndDate(DateUtil.addDay(DateUtil.addMonth(firstDate, compareFilter.getSpecialTimeOff() - compareFilter.getSpecialTimeOn()), -1));
                temp.setDayOn(compareFilter.getDayOn());
                temp.setDayOff(compareFilter.getDayOff());
                result.add(temp);
                firstDate = DateUtil.addYear(firstDate, 1);
                if(firstDate.after(compareFilter.getEndDate())){
                    break;
                }
            }
        }
        return result;
    }

    public List<FoodSellData> getFoodSellData(QueryFilter filter){
        List<Order> orderList = orderService.findOrdersByFilterAndType(filter, OrderTypeEnum.getBusinessTypes());
        return calFoodSellData(orderList);
    }

    public List<FoodSellData> calCategorySellData(QueryFilter filter){
        List<Order> orderList = orderService.findOrdersByFilterAndType(filter, OrderTypeEnum.getBusinessTypes());
        Map<String, FoodSellData> foodItem = new HashMap<String, FoodSellData>();
        for (Order order : orderList){
            if(ValidatorUtil.isNull(order.getOrderItems())){
                continue;
            }
            for (OrderItem orderItem : order.getOrderItems()){
                //筛选费赠送、非子菜项
                if(!orderItem.getGiven()&&!orderItem.getSpecial()&& !OrderItem.FoodTypeEnum.suiteFood.getCode().equals(orderItem.getFoodType())){
                    String id = orderItem.getFoodId().substring(0, 2);
                    if(ValidatorUtil.isNotNull(foodItem.get(id))){
                        FoodSellData temp = foodItem.get(id);
                        temp.setTotalCount(temp.getTotalCount()+orderItem.getCount());
                        temp.setTotalAmount(temp.getTotalAmount()+orderItem.getCount()*orderItem.getPrice());
                    }else {
                        FoodSellData temp = new FoodSellData();
                        temp.setTotalCount(orderItem.getCount());
                        temp.setTotalAmount(orderItem.getCount()*orderItem.getPrice());
                        temp.setOrderItem(orderItem);
                        temp.setId(id);
                        temp.setName(categoryService.getCategoryNameById(id, new ObjectId(filter.getShopId())));
                        foodItem.put(id, temp);
                    }
                }
            }
        }

        List<FoodSellData> items = new ArrayList<FoodSellData>();
        for(Map.Entry<String,  FoodSellData> entry:foodItem.entrySet()){
            items.add(entry.getValue());
        }

        return items;
    }

    /**
     * 计算支付方式数据
     * @param filter
     * @return
     */
    /*public List<OrderTypeData> calPayTypeData(QueryFilter filter){
        List<Order> orderList = orderService.findAllBusinessOrder(filter);

        List<OrderTypeData> result = new ArrayList<>();

        Integer cashTimes = 0, cardTimes = 0, bankTimes = 0, couponTimes = 0, otherTimes = 0;
        BigDecimal cashAmount = BigDecimal.ZERO, cardAmount = BigDecimal.ZERO, bankAmount = BigDecimal.ZERO, couponAmount = BigDecimal.ZERO, otherAmount = BigDecimal.ZERO;

        for (Order order : orderList){
            if (ValidatorUtil.isNotNull(order.getPayItems()) && OrderStateEnum.payedOrder.getCode().equals(order.getState())){
                for (PayItem item : order.getPayItems()){
                    if(0== item.getType()){
                        cashAmount = cashAmount.add(item.getAmount());
                        cashTimes ++;
                    }else if(1 == item.getType()){
                        cardAmount = cardAmount.add(item.getAmount());
                        cardTimes ++;
                    }else if(2 == item.getType()){
                        bankAmount = bankAmount.add(item.getAmount());
                        bankTimes ++;
                    }else if(3 == item.getType()){
                        cashAmount = cashAmount.subtract(item.getAmount());
                    }else if(4 == item.getType()){
                        couponAmount = couponAmount.add(item.getAmount());
                        couponTimes ++;
                    }else if(5 == item.getType()){
                        otherAmount = otherAmount.add(item.getAmount());
                        otherTimes ++;
                    }
                }
            }
        }

        String dateStr = DateUtil.dateSection(filter.getStartDate(), filter.getEndDate());
        result.add(new OrderTypeData(PaymentEnum.cash.getName(), PaymentEnum.cash.getDesc(), cashTimes, cashAmount, dateStr));
        result.add(new OrderTypeData(PaymentEnum.card.getName(), PaymentEnum.card.getDesc(), cardTimes, cardAmount, dateStr));
        result.add(new OrderTypeData(PaymentEnum.bank.getName(), PaymentEnum.bank.getDesc(), bankTimes, bankAmount, dateStr));
        result.add(new OrderTypeData(PaymentEnum.coupon.getName(), PaymentEnum.coupon.getDesc(), couponTimes, couponAmount, dateStr));


        BigDecimal weixinPayAmount = BigDecimal.ZERO, aliPayAmount = BigDecimal.ZERO;
        Integer weixinPayTimes = 0, aliPayTimes = 0;
        List<PayFlow> payFlows = payFlowService.getPayFlow(filter, filter.getShopId());
        for (PayFlow payFlow : payFlows){
            if (PaymentEnum.aliPay.getName().equals(payFlow.getPayType())&&(PayStateEnum.success.getName().equals(payFlow.getState())||PayStateEnum.orderPaid.getName().equals(payFlow.getState()))){
                aliPayAmount = aliPayAmount.add(new BigDecimal(payFlow.getAmount()));
                aliPayTimes ++;
            }else if(PaymentEnum.wxPay.getName().equals(payFlow.getPayType())&&(PayStateEnum.success.getName().equals(payFlow.getState())||PayStateEnum.orderPaid.getName().equals(payFlow.getState()))){
                weixinPayAmount = aliPayAmount.add(new BigDecimal(payFlow.getAmount()));
                weixinPayTimes ++;
            }
        }
        result.add(new OrderTypeData(PaymentEnum.aliPay.getName(), PaymentEnum.aliPay.getDesc(), aliPayTimes, aliPayAmount, dateStr));
        result.add(new OrderTypeData(PaymentEnum.wxPay.getName(), PaymentEnum.wxPay.getDesc(), weixinPayTimes, weixinPayAmount, dateStr));

        return result;
    }*/

    /*public List<OrderTypeData> calOrderTypeData(QueryFilter filter, ObjectId shopId){
        List<Order> orderList = orderService.findAllBusinessOrder(filter);

        List<OrderTypeData> result = new ArrayList<>();

        String dateStr = DateUtil.dateSection(filter.getStartDate(), filter.getEndDate());
        List<OrderTypeEnum> businessTypes = OrderTypeEnum.getBusinessTypesEnum();
        for (OrderTypeEnum type : businessTypes){
            BigDecimal amount = BigDecimal.ZERO;
            Integer times = 0;
            for (Order order: orderList){
                if(type.getCode().equals(order.getOrderType())&&OrderStateEnum.payedOrder.getCode().equals(order.getState())){
                    amount = amount.add(order.getPayedAmount());
                    times ++;
                }
            }
            OrderTypeData data = new OrderTypeData(type.getCode().toString(), type.getName(), times, amount, dateStr);
            result.add(data);
        }
        return result;
    }*/

    /*public List<OrderTypeData> calOrderTimeData(QueryFilter filter){
        List<Order> orderList = orderService.findAllBusinessOrder(filter);
        List<OrderTypeData> result = new ArrayList<>();

        String dateStr = DateUtil.dateSection(filter.getStartDate(), filter.getEndDate());
        OrderTypeData.OrderTimeEnum[] businessTypes = OrderTypeData.OrderTimeEnum.values();
        for (OrderTypeData.OrderTimeEnum type : businessTypes){
            BigDecimal amount = BigDecimal.ZERO;
            Integer times = 0;
            for (Order order: orderList){
                String time = DateUtil.formateDateToStr(order.getPayTime(), "HH:mm:ss");
                String today = DateUtil.formateDateToStr(new Date(), "yyyy-MM-dd");
                //日期转成当天
                Date orderTime = DateUtil.formateStrToDate(today + time, "yyyy-MM-ddHH:mm:ss");
                if((orderTime.after(type.getStartDate()) || orderTime.equals(type.getStartDate()))&&orderTime.before(type.getEndDate())&&OrderStateEnum.payedOrder.getCode().equals(order.getState())){
                    amount = amount.add(order.getPayedAmount());
                    times ++;
                }
            }
            OrderTypeData data = new OrderTypeData(type.getName(), type.getName(), times, amount, dateStr);
            result.add(data);
        }
        return result;
    }*/

    public CardData calCardData(QueryFilter filter){
        CardData data = new CardData();
        if(ValidatorUtil.isNotNull(filter.getShopId())){
            Shop shop = shopService.findBy_id(filter.getShopId());
            data.setShopName(shop.getName());
            data.setSuperFlag(shop.getSuperFlag());
            data.setIsCoupon(filter.getIsCoupon());
        }else {
            data.setShopName("全部");
        }
        data.setIsPublic(filter.getIsPublic());

        List<CardFlow> cardFlowList = cardFlowService.getCardFlowByUseShop(filter);

        for (CardFlow cardFlow : cardFlowList){
            if(ValidatorUtil.isNotNull(cardFlow.getIsRollback())&&cardFlow.getIsRollback().equals(1)){
                continue;
            }
            if(BusinessTypeEnum.charge.getType().equals(cardFlow.getOption())&& ValidatorUtil.isNull(cardFlow.getIsRollback())){
                data.setChargeAmount(data.getChargeAmount().add(cardFlow.getAmount()));
            }else if(BusinessTypeEnum.returnCard.getType().equals(cardFlow.getOption())&& ValidatorUtil.isNull(cardFlow.getIsRollback())){
                data.setReturnAmount(data.getReturnAmount().add(cardFlow.getAmount()));
            }else if(BusinessTypeEnum.payOrder.getType().equals(cardFlow.getOption())&& ValidatorUtil.isNull(cardFlow.getIsRollback())){
                data.setPayAmount(data.getPayAmount().add(cardFlow.getAmount()));
            }else if(BusinessTypeEnum.makeDiscount.getType().equals(cardFlow.getOption()) && ValidatorUtil.isNull(cardFlow.getIsRollback())){
                data.setDiscountAmount(data.getDiscountAmount().add(cardFlow.getAmount()));
            }
        }
        return data;
    }

    public boolean queryModuleExist(QueryFilter queryFilter, ObjectId shopId ){
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getStatisticModules())){
            for (QueryFilter temp : shop.getStatisticModules()){
//                if(temp.getStartDate().equals())
            }
        }
        return false;
    }
}
