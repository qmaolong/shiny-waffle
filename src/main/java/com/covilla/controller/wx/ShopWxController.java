package com.covilla.controller.wx;

import com.covilla.common.OrderTypeEnum;
import com.covilla.common.RoleEnum;
import com.covilla.model.CardFlow;
import com.covilla.model.mongo.order.Order;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.card.CardFlowService;
import com.covilla.service.order.OrderService;
import com.covilla.service.order.StatisticService;
import com.covilla.service.shop.ShopService;
import com.covilla.service.user.UserService;
import com.covilla.util.SerializationUtil;
import com.covilla.vo.filter.QueryFilter;
import com.covilla.vo.result.BusinessData;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by qmaolong on 2017/1/4.
 */
@Controller
@RequestMapping("wx/shop")
public class ShopWxController {
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CardFlowService cardFlowService;

    @RequestMapping("charts")
    public String charts(Model model, HttpServletRequest request){
        String openId = request.getAttribute("openId").toString();
        User user = userService.findByOpenId(openId, false);
        List<Shop> shopList = shopService.findShopsByUser(user);
        model.addAttribute("shops", shopList);
        return "wx/shop/charts";
    }

    @RequestMapping("shops")
    public String shops(Model model, HttpServletRequest request){
        String openId = request.getAttribute("openId").toString();
        User user = userService.findByOpenId(openId, false);
        List<Shop> shopList = shopService.findShopsByUser(user);
        if (RoleEnum.owner.getCode().equals(user.getRole())){

        }
        model.addAttribute("shops", shopList);
        return "wx/shop/shop-list";
    }

    @RequestMapping("getChartsData")
    @ResponseBody
    public BusinessData getChartsData(String dataStr){
        QueryFilter filter = SerializationUtil.deSerializeObject(dataStr, QueryFilter.class);
        //获取订单
        List<Order> orderList = orderService.findOrdersByFilterAndType(filter, OrderTypeEnum.getFlowTypes());
        //获取卡流水
        List<CardFlow> cardFlowList = cardFlowService.getCardFlowByUseShop(filter);
        //订单统计
        BusinessData result = statisticService.calBusinessData(filter, orderList, cardFlowList);
        return result;
    }

    /**
     * 门店信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("shopInfo")
    public String shopInfo(String id, Model model){
        Shop shop = shopService.findBy_id(new ObjectId(id));
        model.addAttribute("shop", shop);
        return "wx/shop/shop-info";
    }

}
