package com.covilla.controller.bk.card;

import com.alibaba.fastjson.JSONArray;
import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.OperationEnum;
import com.covilla.controller.wx.CustomerController;
import com.covilla.model.mongo.card.CardChargeRule;
import com.covilla.model.mongo.card.CardDiscountRule;
import com.covilla.model.mongo.card.CardType;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.card.CardTypeService;
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
@RequestMapping("merchant/admin/card")
public class CardTypeController {
    Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private CardTypeService cardTypeService;
    @Autowired
    private UserService userService;

    /**
     * 卡类型管理
     * @return
     */
    @RequestMapping("cardTypes")
    public String CardTypes(){
        return "bk/card/card-types";
    }

    /**
     * 卡类型列表
     * @return
     */
    @RequestMapping("getCardTypeList")
    @ResponseBody
    public List<CardType> getCardTypeList(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        List<CardType> cardTypes = cardTypeService.findAvailableCardType(shopId);
        return cardTypes;
    }

    /**
     * 卡类型编辑页面
     * @param model
     * @return
     */
    @RequestMapping("cardTypeInput")
    public String cardTypeInput(Model model, String oper){
        model.addAttribute("oper", oper);
        return "bk/card/card-type-input";
    }

    /**
     * 编辑卡类型
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editCardType")
    @ResponseBody
    @AOPLogAnnotation(description = "卡类型")
    public ResultMsg editCardType(String dataStr, String discountRulesStr, String chargeRulesStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            List<CardDiscountRule> discountRules = SerializationUtil.deSerializeList(discountRulesStr, CardDiscountRule.class);
            List<CardChargeRule> chargeRules = SerializationUtil.deSerializeList(chargeRulesStr, CardChargeRule.class);
            if(OperationEnum.add.getCode().equals(oper)){//新增
                CardType cardType = SerializationUtil.deSerializeObject(dataStr, CardType.class);
                cardTypeService.addCardType(cardType, discountRules, chargeRules, shopId, user.get_id());
            }else if(OperationEnum.edit.getCode().equals(oper)){//编辑
                CardType cardType = SerializationUtil.deSerializeObject(dataStr, CardType.class);
                cardTypeService.editCardType(cardType, discountRules, chargeRules,  shopId, user.get_id());
            }else if(OperationEnum.delete.getCode().equals(oper)){//删除
                List<CardType> cardTypes = SerializationUtil.deSerializeList(dataStr, CardType.class);
                cardTypeService.deleteCardType(cardTypes, shopId, user);
            }
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    //=======================================优惠规则===========================================
    /**
     * 卡优惠规则
     * @return
     */
    @RequestMapping("cardDiscountRules")
    public String cardDiscountRules(Integer cardId, Model model){
        model.addAttribute("cardId", cardId);
        return "bk/card/card-discount-rules";
    }

    /**
     * 优惠规则列表
     * @param cardId
     * @return
     */
    @RequestMapping("getDiscountRules")
    @ResponseBody
    public List<CardDiscountRule> getDiscountRules(Integer cardId, HttpSession session){
        CardType cardType = cardTypeService.findById(cardId);
        if(ValidatorUtil.isNotNull(cardType) && ValidatorUtil.isNotNull(cardType.getDiscountRules())){
            return cardType.getDiscountRules();
        }
        return null;
    }

    /**
     * 优惠编辑页
     * @return
     */
    @RequestMapping("disountRulesInput")
    public String discountRulesInput(Integer cardTypeId, Integer id, Model model, String oper){
        model.addAttribute("oper", oper);
        return "bk/card/discount-rules-input";
    }

    /**
     * 编辑适用菜品
     * @param model
     * @return
     */
    @RequestMapping("discountRulesFitFoods")
    public String discountRulesFitFoods(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return "redirect:/merchant/exit";
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("categories", JSONArray.toJSON(shop.getCategories()));
        }
        return "bk/card/discount-rules-fit-foods";
    }

    //=========================================充值规则=========================================
    /**
     * 卡充值规则
     * @return
     */
    @RequestMapping("cardChargeRules")
    public String cardChargeRules(){
        return "bk/card/card-charge-rules";
    }

    /**
     * 充值规则编辑页
     * @return
     */
    @RequestMapping("chargeRulesInput")
    public String chargeRulesInput(String oper, Model model){
        model.addAttribute("oper", oper);
        return "bk/card/charge-rules-input";
    }

}
