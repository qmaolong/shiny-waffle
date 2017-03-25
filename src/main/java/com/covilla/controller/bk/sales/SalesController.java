package com.covilla.controller.bk.sales;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.LogOptionEnum;
import com.covilla.model.mongo.card.CardDiscountRule;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.organization.SectionService;
import com.covilla.service.sales.SalesService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.SpringSecurityUtil;
import com.covilla.vo.ResultMsg;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 运营管理
 * Created by qmaolong on 2016/8/30.
 */
@Controller
@RequestMapping("merchant/admin/sales")
public class SalesController {
    Logger logger = LoggerFactory.getLogger(SalesController.class);
    @Autowired
    private SectionService sectionService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SalesService salesService;

    /**
     * 折扣方案
     * @return
     */
    @RequestMapping("discountRules")
    public String discountRules(){
        return "bk/sales/discount-rules";
    }

    @RequestMapping("specialFoods")
    public String specialFoods(HttpSession session){

        return "bk/sales/special-foods";
    }

    /**
     * 获取非会员的折扣方案
     * @param session
     * @return
     */
    @RequestMapping("getDescountRules")
    @ResponseBody
    public List<CardDiscountRule> getDiscountRules(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        return salesService.getDiscountRules(shopId);
    }

    /**
     * 编辑优惠规则
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editDiscountRule")
    @ResponseBody
    @AOPLogAnnotation(description = "优惠规则")
    public ResultMsg editDiscountRule(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            salesService.editDiscountRules(dataStr, shopId, user, oper);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("sortDiscountRules")
    @ResponseBody
    @AOPLogAnnotation(description = "优惠规则排序",option = LogOptionEnum.BUSINESS)
    public ResultMsg sortDiscountRules(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            salesService.sortDiscountRules(dataStr, shopId, user);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

}
