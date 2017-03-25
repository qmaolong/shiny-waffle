package com.covilla.controller.bk.order;

import com.covilla.common.Constant;
import com.covilla.common.OrderTypeEnum;
import com.covilla.model.CardFlow;
import com.covilla.model.mongo.order.Order;
import com.covilla.model.mongo.order.TradeDays;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.card.CardFlowService;
import com.covilla.service.order.OrderService;
import com.covilla.service.order.StatisticService;
import com.covilla.service.order.TradeDaysService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.DateUtil;
import com.covilla.util.SerializationUtil;
import com.covilla.util.SpringSecurityUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.filter.QueryFilter;
import com.covilla.vo.result.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by qmaolong on 2016/10/13.
 */
@Controller
@RequestMapping("merchant/admin/order")
public class StatisticController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private TradeDaysService tradeDaysService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private CardFlowService cardFlowService;

    //订单列表
    @RequestMapping("orders")
    public String orders(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("payments", shop.getPayments());
            model.addAttribute("sections", shop.getSections());
            model.addAttribute("queryModules", shop.getStatisticModules());

            List<Shop> shopList = new ArrayList<Shop>();
            if(shop.getSuperFlag()){
                shopList = shopService.findByOwner(shop.getOwner());
            }else {
                shopList.add(shop);
            }
            model.addAttribute("shopList", shopList);
        }
        return "bk/order/orders";
    }

    /**
     * 获取订单列表
     * @param session
     * @return
     */
    @RequestMapping("getOrders")
    @ResponseBody
    public List<Order> getOrders(String dataStr, HttpSession session){
        QueryFilter filter = SerializationUtil.deSerializeObject(dataStr, QueryFilter.class);
        if (ValidatorUtil.isNull(filter.getShopId())){
            User user = SpringSecurityUtil.getCurrentUser();
            List<ObjectId> shopIds = shopService.findIdsByUser(user);
            filter.setShopIds(shopIds);
        }
        return orderService.findOrdersByFilterAndType(filter, OrderTypeEnum.getBusinessTypes());
    }

    /**
     * 订单详情
     * @param orderNo
     * @param model
     * @return
     */
    @RequestMapping("orderDetail")
    public String orderDetail(String orderNo, Model model){
        Order order = orderService.findById(orderNo);
        model.addAttribute("order", order);
        return "bk/order/order-detail";
    }

    /**
     * 交接班记录
     * @return
     */
    @RequestMapping("tradeDays")
    public String tradeDays(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("payments", shop.getPayments());
            model.addAttribute("sections", shop.getSections());
            model.addAttribute("queryModules", shop.getStatisticModules());

            List<Shop> shopList = new ArrayList<Shop>();
            if(shop.getSuperFlag()){
                shopList = shopService.findByOwner(shop.getOwner());
            }else {
                shopList.add(shop);
            }
            model.addAttribute("shopList", shopList);
        }
        return "bk/order/tradeDays";
    }

    /**
     * 获取交接班记录列表
     * @param session
     * @return
     */
    @RequestMapping("getTradeDays")
    @ResponseBody
    public List<TradeDays> getTradeDays(String dataStr, HttpSession session){
        QueryFilter filter = SerializationUtil.deSerializeObject(dataStr, QueryFilter.class);
        if (ValidatorUtil.isNull(filter.getShopId())){
            User user = SpringSecurityUtil.getCurrentUser();
            List<ObjectId> shopIds = shopService.findIdsByUser(user);
            filter.setShopIds(shopIds);
        }
        return tradeDaysService.findByShopId(filter);
    }

    /**
     * 交接班记录详情
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("tradeDayDetail")
    public String tradeDayDetail(String id, Model model){
        TradeDays tradeDays = tradeDaysService.findById(id);
        model.addAttribute("tradeDay", tradeDays);
        return "bk/order/tradeDay-detail";
    }

    /**
     * 图表
     * @return
     */
    @RequestMapping("charts")
    public String charts(){
        return "bk/order/charts";
    }

    /**
     * 获取图表数据
     * @param session
     * @return
     */
    @RequestMapping("getChartsData")
    @ResponseBody
    public Map<String, Object> getChartsData(HttpSession session){
        Map<String, Object> result = new HashMap<String, Object>();
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        //获取当月订单
        List<Order> monthOrders = orderService.findCurrentMonthOrders(shopId);
        //获取当周订单
        List<Order> weekOrders = orderService.findCurrentWeekOrders(shopId);
        //一周销售情况
        Map<String, Object> weekData = statisticService.calWeekData(weekOrders);
        result.put("weekData", weekData);
        //一周菜品销售情况
        Map<String, Object> weekFood = statisticService.calWeekFood(weekOrders);
        result.put("weekFood", weekFood);
        //当月收入
        BigDecimal monthIncome = statisticService.monthIncome(monthOrders);
        result.put("monthIncome", monthIncome);
        //当月订单数
        result.put("monthOrderCount", monthOrders.size());
        //当月客流量
        Integer monthPeople = statisticService.monthPeople(monthOrders);
        result.put("monthPeople", monthPeople);
        //当天客流量
        Integer dayPeople = statisticService.dayPeople(DateUtil.addDay(new Date(), 0), DateUtil.addDay(new Date(), 0), shopId);
        result.put("dayPeople", dayPeople);
        result.put("month", new Date().getMonth() + 1);
        return result;
    }

    //营业情况统计
    @RequestMapping("businessData")
    public String businessConfition(Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("payments", shop.getPayments());
            model.addAttribute("sections", shop.getSections());
            model.addAttribute("queryModules", shop.getStatisticModules());

            List<Shop> shopList = new ArrayList<Shop>();
            if(shop.getSuperFlag()){
                shopList = shopService.findByOwner(shop.getOwner());
            }else {
                shopList.add(shop);
            }
            model.addAttribute("shopList", shopList);
        }
        return "bk/order/business-data";
    }

    @RequestMapping("getBusinessData")
    @ResponseBody
    public List<BusinessData> getBusinessData(String dataStr, HttpSession session){
        QueryFilter filter = SerializationUtil.deSerializeObject(dataStr, QueryFilter.class);

        if (ValidatorUtil.isNull(filter.getShopId())){
            User user = SpringSecurityUtil.getCurrentUser();
            List<ObjectId> shopIds = shopService.findIdsByUser(user);
            filter.setShopIds(shopIds);
        }
        List<BusinessData> result = new ArrayList<BusinessData>();
        //获取订单
        List<Order> orderList = orderService.findOrdersByFilterAndType(filter, OrderTypeEnum.getFlowTypes());
        //获取卡流水
        List<CardFlow> cardFlowList = cardFlowService.getCardFlowByUseShop(filter);
        //订单统计
        BusinessData data = statisticService.calBusinessData(filter, orderList, cardFlowList);
        result.add(data);
        return result;
    }

    @RequestMapping("businessDataDetail")
    public String businessDataDetail(String content, Model model){
        BusinessData data = SerializationUtil.deSerializeObject(content, BusinessData.class);
        /*try{
            data.setShopName(new String(data.getShopName().getBytes("iso8859-1"),"utf-8"));
            data.setDateStr(new String(data.getDateStr().getBytes("iso8859-1"),"utf-8"));
        }catch (Exception e){

        }*/
        model.addAttribute("data", data);
        return "bk/order/business-data-detail";
    }

    /**
     * 周期对比
     * @param dataStr
     * @return
     */
    @RequestMapping("periodCompare")
    @ResponseBody
    public List<BusinessData> periodCompare(String dataStr, HttpSession session){
        QueryFilter filter = SerializationUtil.deSerializeObject(dataStr, QueryFilter.class);
        if (ValidatorUtil.isNull(filter.getShopId())){
            User user = SpringSecurityUtil.getCurrentUser();
            List<ObjectId> shopIds = shopService.findIdsByUser(user);
            filter.setShopIds(shopIds);
        }
        List<BusinessData> result = new ArrayList<BusinessData>();

        List<QueryFilter> queryFilters = statisticService.generateQueryFiltersByCompareFilter(filter);
        for (QueryFilter queryFilter : queryFilters){
            List<Order> orderList = orderService.findOrdersByFilterAndType(queryFilter, OrderTypeEnum.getFlowTypes());
            List<CardFlow> cardFlowList = cardFlowService.getCardFlowByUseShop(queryFilter);
            BusinessData data = statisticService.calBusinessData(queryFilter, orderList, cardFlowList);
            result.add(data);
        }
        return result;
    }

    //商品销售情况统计
    @RequestMapping("foodSellData")
    public String foodSellData(Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("payments", shop.getPayments());
            model.addAttribute("sections", shop.getSections());
            model.addAttribute("queryModules", shop.getStatisticModules());

            List<Shop> shopList = new ArrayList<Shop>();
            if(shop.getSuperFlag()){
                shopList = shopService.findByOwner(shop.getOwner());
            }else {
                shopList.add(shop);
            }
            model.addAttribute("shopList", shopList);
        }
        return "bk/order/food-sell-data";
    }

    /**
     * 获取商品销售数据
     * @param dataStr
     * @param session
     * @return
     */
    @RequestMapping("getFoodSellData")
    @ResponseBody
    public List<FoodSellData> getFoodSellData(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        QueryFilter filter = SerializationUtil.deSerializeObject(dataStr, QueryFilter.class);
        List<FoodSellData> result = statisticService.getFoodSellData(filter);
        return result;
    }

    //大类销售情况统计
    @RequestMapping("categorySellData")
    public String categorySellData(Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("payments", shop.getPayments());
            model.addAttribute("sections", shop.getSections());
            model.addAttribute("queryModules", shop.getStatisticModules());

            List<Shop> shopList = new ArrayList<Shop>();
            if(shop.getSuperFlag()){
                shopList = shopService.findByOwner(shop.getOwner());
            }else {
                shopList.add(shop);
            }
            model.addAttribute("shopList", shopList);
        }
        return "bk/order/category-sell-data";
    }

    /**
     * 获取商品销售数据
     * @param dataStr
     * @param session
     * @return
     */
    @RequestMapping("getCategorySellData")
    @ResponseBody
    public List<FoodSellData> getCategorySellData(String dataStr, HttpSession session){
        QueryFilter filter = SerializationUtil.deSerializeObject(dataStr, QueryFilter.class);
        List<FoodSellData> result = statisticService.calCategorySellData(filter);
        return result;
    }

    /**
     * 支付方式统计
     * @return
     */
    @RequestMapping("payTypeData")
    public String payTypeData(){
        return "bk/order/pay-type-data";
    }

    /**
     * 支付方式数据统计
     * @param dataStr
     * @param session
     * @return
     */
    /*@RequestMapping("getPayTypeData")
    @ResponseBody
    public List<OrderTypeData> getPayTypeData(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        QueryFilter filter = MyJsonUtil.deSerializeObject(dataStr, QueryFilter.class);
        List<OrderTypeData> result = statisticService.calPayTypeData(filter);
        return result;
    }*/

    /**
     * 订单类型统计
     * @return
     */
    @RequestMapping("orderTypeData")
    public String orderTypeData(){
        return "bk/order/order-type-data";
    }

    /**
     * 订单类型数据统计
     * @param dataStr
     * @param session
     * @return
     */
    /*@RequestMapping("getOrderTypeData")
    @ResponseBody
    public List<OrderTypeData> getOrderTypeData(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        QueryFilter filter = MyJsonUtil.deSerializeObject(dataStr, QueryFilter.class);
        List<OrderTypeData> result = statisticService.calOrderTypeData(filter, shopId);
        return result;
    }*/

    /**
     * 订单类型统计
     * @return
     */
    @RequestMapping("orderTimeData")
    public String orderTimeData(){
        return "bk/order/order-time-data";
    }

    /**
     * 订单类型数据统计
     * @return
     */
    /*@RequestMapping("getOrderTimeData")
    @ResponseBody
    public List<OrderTypeData> orderTimeData(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        QueryFilter filter = MyJsonUtil.deSerializeObject(dataStr, QueryFilter.class);
        List<OrderTypeData> result = statisticService.calOrderTimeData(filter);
        return result;
    }*/

    @RequestMapping("cardData")
    public String cardData(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("payments", shop.getPayments());
            model.addAttribute("sections", shop.getSections());
            model.addAttribute("queryModules", shop.getStatisticModules());

            List<Shop> shopList = new ArrayList<Shop>();
            if(shop.getSuperFlag()){
                shopList = shopService.findByOwner(shop.getOwner());
            }else {
                shopList.add(shop);
            }
            model.addAttribute("shopList", shopList);
        }
        return "bk/order/card-data";
    }

    @RequestMapping("getCardData")
    @ResponseBody
    public List<CardData> getCardData(String dataStr, HttpSession session){
        QueryFilter filter = SerializationUtil.deSerializeObject(dataStr, QueryFilter.class);
        if (ValidatorUtil.isNull(filter.getShopId())){
            User user = SpringSecurityUtil.getCurrentUser();
            List<ObjectId> shopIds = shopService.findIdsByUser(user);
            filter.setShopIds(shopIds);
        }
        List<CardData> result = new ArrayList<CardData>();
        result.add(statisticService.calCardData(filter));
        return result;
    }

    /*@RequestMapping("couponData")
    public String couponData(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("payments", shop.getPayments());
            model.addAttribute("sections", shop.getSections());
            model.addAttribute("queryModules", shop.getStatisticModules());

            List<Shop> shopList = new ArrayList<>();
            if(shop.getSuperFlag()){
                shopList = shopService.findByOwner(shop.getOwner());
            }else {
                shopList.add(shop);
            }
            model.addAttribute("shopList", shopList);
        }
        return "bk/order/coupon-data";
    }

    @RequestMapping("getCouponData")
    @ResponseBody
    public List<CardData> getCouponData(String dataStr, HttpSession session){
        QueryFilter filter = MyJsonUtil.deSerializeObject(dataStr, QueryFilter.class);
        filter.setIsCoupon(true);
        if (ValidatorUtil.isNull(filter.getShopId())){
            User user = (User)session.getAttribute(Constant.USER_SESSION);
            List<ObjectId> shopIds = shopService.findIdsByUser(user);
            filter.setShopIds(shopIds);
        }
        List<CardData> result = new ArrayList<>();
        result.add(statisticService.calCardData(filter));
        return result;
    }*/

}
