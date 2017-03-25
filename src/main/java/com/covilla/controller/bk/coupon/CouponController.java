package com.covilla.controller.bk.coupon;

import com.alibaba.fastjson.JSONArray;
import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.CardTypeEnum;
import com.covilla.common.Constant;
import com.covilla.common.LogOptionEnum;
import com.covilla.common.OperationEnum;
import com.covilla.model.mongo.card.Card;
import com.covilla.model.mongo.card.CardBatch;
import com.covilla.model.mongo.card.CardPublish;
import com.covilla.model.mongo.card.CardType;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.card.CardBatchService;
import com.covilla.service.card.CardService;
import com.covilla.service.coupon.CouponService;
import com.covilla.service.coupon.CouponTypeService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.SerializationUtil;
import com.covilla.util.SpringSecurityUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.ResultMsg;
import com.covilla.vo.card.CardFilterVo;
import com.covilla.vo.card.CardImportVo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 优惠券
 * Created by qmaolong on 2016/9/22.
 */
@Controller
@RequestMapping("merchant/admin/coupon")
public class CouponController {
    Logger logger = LoggerFactory.getLogger(CouponController.class);
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponTypeService couponTypeService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CardBatchService cardBatchService;
    @Autowired
    private CardService cardService;

    /**
     * 券列表页
     * @return
     */
    @RequestMapping("/coupons")
    public String coupons(Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        List<CardType> couponTypes = couponTypeService.findAvailableCouponType(shopId);
        model.addAttribute("couponTypes", couponTypes);
        return "bk/coupon/coupons";
    }

    /**
     * 券操作
     * @param oper
     * @param model
     * @return
     */
    @RequestMapping("/couponInput")
    public String couponInput(String oper, Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        //获取本店方案
        List<CardType> couponTypes = couponTypeService.findAvailableCouponType(shopId);
        model.addAttribute("couponTypes", couponTypes);
        //获取总店方案
        List<CardType> publicCardTypes = couponTypeService.findMainShopCouponType(shopId);
        model.addAttribute("publicCardType", publicCardTypes);

        model.addAttribute("oper", oper);
        return "bk/coupon/coupon-input";
    }

    /**
     * 券列表数据
     * @param session
     * @return
     */
    @RequestMapping("getCouponList")
    @ResponseBody
    public List<Card> getCouponList(String dataStr, HttpSession session){
        CardFilterVo filterVo = SerializationUtil.deSerializeObject(dataStr, CardFilterVo.class);
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        return cardService.findCards(filterVo, shopId, true);
    }

    /**
     * 券编辑
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("couponEdit")
    @ResponseBody
    @AOPLogAnnotation(description = "券")
    public ResultMsg couponEdit(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            if(OperationEnum.add.getCode().equals(oper)){//新增
                Card coupon = SerializationUtil.deSerializeObject(dataStr, Card.class);
                System.out.print("");
                couponService.addCoupon(coupon, shopId, user);
            }else if(OperationEnum.edit.getCode().equals(oper)){//编辑
                Card coupon = SerializationUtil.deSerializeObject(dataStr, Card.class);
                coupon.setSource(Card.SOURCE_BACKEND);
                couponService.editCoupon(coupon);
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

    /**
     * 获取券信息
     * @param tagId
     * @param session
     * @return
     */
    @RequestMapping("getCardInfo")
    @ResponseBody
    public ResultMsg getCardInfo(String tagId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);

        Card coupon = couponService.findByTagIdAndShop(tagId, shopId);

        if(ValidatorUtil.isNull(coupon)){
            return ResultMsg.buildFailMsg("-1", "不存在该券号");
        }
        return ResultMsg.buildSuccessMsg(coupon);
    }

    /**
     * 作废
     * @param tagId
     * @param session
     * @return
     */
    @RequestMapping("disableCoupon")
    @ResponseBody
    @AOPLogAnnotation(description = "券作废",displayTitle = "券编号",displayItemFromParameter = "tagId")
    public ResultMsg disableCoupon(String tagId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            couponService.disableCoupon(tagId, shopId);
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    //批量导入会员卡
    @RequestMapping("importCouponsForm")
    public String importCouponsForm(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        //获取本店方案
        List<CardType> couponTypes = couponTypeService.findAvailableCouponType(shopId);
        model.addAttribute("couponTypes", couponTypes);
        //获取总店方案
        List<CardType> publicCardTypes = couponTypeService.findMainShopCouponType(shopId);
        model.addAttribute("publicCardType", publicCardTypes);
        return "bk/coupon/coupon-import";
    }

    @RequestMapping("importCouponsPreview")
    public String importCouponsPreview(MultipartFile file, String name, Double balance, Boolean isPublic, String cardTypeIdStr, String startTime, String endTime, Integer cardState, HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            List<CardImportVo> cardList = couponService.previewCoupons(file, isPublic, cardTypeIdStr, startTime, endTime, cardState, shopId);
            model.addAttribute("cards", JSONArray.toJSON(cardList));
        }catch (Exception e){
            logger.error(e.getMessage());
            model.addAttribute("error", true);
        }

        return "bk/coupon/coupon-import-preview";
    }

    /**
     * 批量导入会员卡提交
     * @param session
     * @return
     */
    @RequestMapping("/importCouponsSubmit")
    @ResponseBody
    @AOPLogAnnotation(description = "批量导入会员卡")
    public ResultMsg importCoupons(String dataStr,  HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            int importCount = couponService.importCoupons(dataStr, shopId, user.get_id().toString());
            return ResultMsg.buildSuccessMsg(importCount);
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
    }

    //批量生成券
    @RequestMapping("generateCouponsForm")
    public String generateCouponsForm(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        //获取本店方案
        List<CardType> couponTypes = couponTypeService.findAvailableCouponType(shopId);
        model.addAttribute("couponTypes", couponTypes);
        //获取总店方案
        List<CardType> publicCardTypes = couponTypeService.findMainShopCouponType(shopId);
        model.addAttribute("publicCardType", publicCardTypes);
        return "bk/coupon/coupon-generate";
    }

    /**
     * 批量生成会员卡提交
     * @param session
     * @return
     */
    @RequestMapping("/generateCouponsSubmit")
    @ResponseBody
    @AOPLogAnnotation(description = "批量生成券",option = LogOptionEnum.GENERATE_CARD)
    public ResultMsg generateCouponsSubmit(String dataStr,  HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            CardBatch cardBatch = JSONArray.parseObject(dataStr, CardBatch.class);
            cardBatch.setMedium(CardTypeEnum.coupon.getCode());
            couponService.generateCards(cardBatch, shopId, user.get_id().toString());
            return ResultMsg.buildSuccessMsg();
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
    }

    @RequestMapping("couponBatch")
    public String couponBatch(){
        return "bk/coupon/coupon-batches";
    }

    /**
     * 获取批次
     * @param session
     * @return
     */
    @RequestMapping("getCouponBatches")
    @ResponseBody
    public List<CardBatch> getCouponBatches(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        return cardBatchService.findBatches(shopId, true);
    }

    /**
     * 同一批次券管理
     * @return
     */
    @RequestMapping("batchCoupons")
    public String batchCoupons(String batchId, Model model){
        model.addAttribute("batchId", batchId);
        return "bk/coupon/batch-coupons";
    }

    /**
     * 获取同一批次的券
     * @param batchId
     * @param session
     * @return
     */
    @RequestMapping("getBatchCoupons")
    @ResponseBody
    public List<Card> getBatchCoupons(String batchId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        return couponService.findByBatchId(shopId, batchId);
    }

    /**
     * 批次信息
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("batchDetail")
    public String batchDetail(String id, HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);

        CardBatch cardBatch = cardBatchService.findByBatchId(id, shopId);
        model.addAttribute("oper", "look");
        //获取本店方案
        List<CardType> couponTypes = couponTypeService.findAvailableCouponType(shopId);
        model.addAttribute("couponTypes", couponTypes);
        //获取总店方案
        List<CardType> publicCardTypes = couponTypeService.findMainShopCouponType(shopId);
        model.addAttribute("publicCardType", publicCardTypes);
        if(ValidatorUtil.isNotNull(cardBatch)&&ValidatorUtil.isNotNull(cardBatch.getNoLength())){
            return "bk/coupon/coupon-generate";
        }
        return "bk/coupon/coupon-import";
    }


    /**
     * 券流水管理
     * @return
     */
    @RequestMapping("/couponFlows")
    public String couponFlows(){
        return "bk/coupon/coupon-flows";
    }

    /**
     * 获取券流水
     * @param session
     * @return
     */
    /*@RequestMapping("getCardFlows")
    @ResponseBody
    public List<BusinessCardFlow> getCardFlows(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        return businessFlowService.getCardFlow(shopId.toString());
    }

    *//**
     * 流水详情
     * @return
     *//*
    @RequestMapping("couponFlowDetail")
    public String couponFlowDetail(String seriesNumber, Model model){
        BusinessCardFlow flow = businessFlowService.getFlowBySeriesNo(seriesNumber);
        model.addAttribute("flow", flow);
        return "bk/coupon/coupon-flow-detail";
    }*/

    /**
     * 优惠券发放规则
     * @return
     */
    @RequestMapping("couponPublishRules")
    public String couponSendRules(){
        return "bk/coupon/coupon-send-rules";
    }

    @RequestMapping("getCouponPublishRules")
    @ResponseBody
    public List<CardPublish> getCouponSendRules(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getTicketPublish();
        }
        return null;
    }

    @RequestMapping("couponPublishInput")
    public String couponPublishInput(String oper, Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        //获取本店方案
        List<CardType> couponTypes = couponTypeService.findAvailableCouponType(shopId);
        model.addAttribute("couponTypes", couponTypes);

        if(ValidatorUtil.isNull(shop.getSuperFlag()) || !shop.getSuperFlag()){//分店获取总店公开的方案
            //获取总店公开的方案
            List<CardType> publicCardTypes = couponTypeService.findMainShopCouponType(shopId);
            model.addAttribute("publicCouponTypes", publicCardTypes);
        }
        model.addAttribute("oper", oper);
        return "bk/coupon/coupon-send-rule-input";
    }

    @RequestMapping("editCouponPublishRule")
    @ResponseBody
    @AOPLogAnnotation(description = "券发放规则")
    public ResultMsg editCouponPublishRule(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            couponService.editCouponPublishRule(dataStr, oper, shopId);
        }catch (ServiceException se){
            logger.error(se.getMessage());
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("activateCoupon")
    @ResponseBody
    @AOPLogAnnotation(description = "券激活",displayTitle = "券编号",displayItemFromParameter = "tagId")
    public ResultMsg activateCoupon(String tagId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            couponService.activateCoupon(tagId, shopId);
        }catch (ServiceException se){
            logger.error(se.getMessage());
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

}
