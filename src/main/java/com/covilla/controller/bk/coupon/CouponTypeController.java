package com.covilla.controller.bk.coupon;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.OperationEnum;
import com.covilla.controller.wx.CustomerController;
import com.covilla.model.mongo.card.CardDiscountRule;
import com.covilla.model.mongo.card.CardType;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.coupon.CouponTypeService;
import com.covilla.service.shop.ShopService;
import com.covilla.service.user.UserService;
import com.covilla.util.SerializationUtil;
import com.covilla.util.SpringSecurityUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.ResultMsg;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by qmaolong on 2016/9/14.
 */
@Controller
@RequestMapping("merchant/admin/coupon")
public class CouponTypeController {
    Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private CouponTypeService couponTypeService;
    @Autowired
    private UserService userService;

    /**
     * 卡类型管理
     * @return
     */
    @RequestMapping("couponTypes")
    public String CardTypes(){
        return "bk/coupon/coupon-types";
    }

    /**
     * 卡类型列表
     * @return
     */
    @RequestMapping("getCouponTypeList")
    @ResponseBody
    public List<CardType> getCardTypeList(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        List<CardType> couponTypes = couponTypeService.findAvailableCouponType(shopId);
        return couponTypes;
    }

    /**
     * 卡类型编辑页面
     * @param model
     * @return
     */
    @RequestMapping("couponTypeInput")
    public String couponTypeInput(Model model, String oper){
        model.addAttribute("oper", oper);
        return "bk/coupon/coupon-type-input";
    }

    /**
     * 编辑卡类型
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editCouponType")
    @ResponseBody
    @AOPLogAnnotation(description = "券类型")
    public ResultMsg editCardType(String dataStr, String discountRulesStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            List<CardDiscountRule> discountRules = SerializationUtil.deSerializeList(discountRulesStr, CardDiscountRule.class);
            if(OperationEnum.add.getCode().equals(oper)){//新增
                CardType couponType = SerializationUtil.deSerializeObject(dataStr, CardType.class);
                couponTypeService.addCouponType(couponType, discountRules, shopId, user.get_id());
            }else if(OperationEnum.edit.getCode().equals(oper)){//编辑
                CardType couponType = SerializationUtil.deSerializeObject(dataStr, CardType.class);
                couponTypeService.editCouponType(couponType, discountRules, shopId, user.get_id());
            }else if(OperationEnum.delete.getCode().equals(oper)){//删除
                List<CardType> couponTypes = SerializationUtil.deSerializeList(dataStr, CardType.class);
                couponTypeService.deleteCouponType(couponTypes, shopId, user);
            }
        }catch (ServiceException se){
            logger.error(se.getMessage());
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    //=======================================优惠规则===========================================
    /**
     * 优惠规则列表
     * @param couponId
     * @return
     */
    @RequestMapping("getDiscountRules")
    @ResponseBody
    public List<CardDiscountRule> getDiscountRules(Integer couponId, HttpSession session){
        CardType couponType = couponTypeService.findById(couponId);
        if(ValidatorUtil.isNotNull(couponType) && ValidatorUtil.isNotNull(couponType.getDiscountRules())){
            return couponType.getDiscountRules();
        }
        return null;
    }

    /**
     * 优惠编辑页
     * @return
     */
    @RequestMapping("disountRulesInput")
    public String discountRulesInput(Integer couponTypeId, Integer id, Model model, String oper){
        model.addAttribute("oper", oper);
        return "bk/coupon/discount-rules-input";
    }

}
