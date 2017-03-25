package com.covilla.controller.bk.card;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.*;
import com.covilla.model.CardFlow;
import com.covilla.model.mongo.card.*;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.card.CardBatchService;
import com.covilla.service.card.CardFlowService;
import com.covilla.service.card.CardService;
import com.covilla.service.card.CardTypeService;
import com.covilla.service.coupon.CouponTypeService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.SerializationUtil;
import com.covilla.util.SpringSecurityUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.BaseApiResultMsg;
import com.covilla.vo.ResultMsg;
import com.covilla.vo.card.CardFilterVo;
import com.covilla.vo.card.CardImportVo;
import com.covilla.vo.card.CouponSendVo;
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
 * Created by qmaolong on 2016/9/22.
 */
@Controller
@RequestMapping("merchant/admin/card")
public class CardController {
    Logger logger = LoggerFactory.getLogger(CardController.class);
    @Autowired
    private CardService cardService;
    @Autowired
    private CardTypeService cardTypeService;
    @Autowired
    private CardFlowService cardFlowService;
    @Autowired
    private CardBatchService cardBatchService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CouponTypeService couponTypeService;

    /**
     * 卡列表页
     * @return
     */
    @RequestMapping("/cards")
    public String cards(Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        List<CardType> cardTypes = cardTypeService.findAvailableCardType(shopId);
        model.addAttribute("cardTypes", cardTypes);
        return "bk/card/cards";
    }

    /**
     * 卡操作
     * @param oper
     * @param model
     * @return
     */
    @RequestMapping("/cardInput")
    public String cardInput(String oper, Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        //获取本店方案
        List<CardType> cardTypes = cardTypeService.findAvailableCardType(shopId);
        model.addAttribute("cardTypes", cardTypes);
        //获取总店方案
        List<CardType> publicCardTypes = cardTypeService.findMainShopCardType(shopId, user);
        model.addAttribute("publicCardType", publicCardTypes);

        model.addAttribute("oper", oper);
        return "bk/card/card-input";
    }

    /**
     * 卡列表数据
     * @param session
     * @return
     */
    @RequestMapping("getCardList")
    @ResponseBody
    public List<Card> getCardList(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        CardFilterVo filterVo = SerializationUtil.deSerializeObject(dataStr, CardFilterVo.class);
        return cardService.findCards(filterVo, shopId, false);
    }

    /**
     * 卡编辑
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("cardEdit")
    @ResponseBody
    @AOPLogAnnotation(description = "会员卡",displayTitle = "卡编号",displayItemFromDataStr = "tagId")
    public ResultMsg cardEdit(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            if(OperationEnum.add.getCode().equals(oper)){//新增
                Card card = SerializationUtil.deSerializeObject(dataStr, Card.class);
                System.out.print("");
                cardService.addCard(card, shopId, user);
            }else if(OperationEnum.edit.getCode().equals(oper)){//编辑
                Card card = SerializationUtil.deSerializeObject(dataStr, Card.class);
                card.setSource(Card.SOURCE_BACKEND);
                cardService.editCard(card);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    /**
     * 获取卡信息
     * @param tagId
     * @param session
     * @return
     */
    @RequestMapping("getCardInfo")
    @ResponseBody
    public ResultMsg getCardInfo(String tagId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);

        Card card = cardService.findByTagIdAndShop(tagId, shopId);

        if(ValidatorUtil.isNull(card)){
            return ResultMsg.buildFailMsg("-1", "不存在该卡号");
        }
        return ResultMsg.buildSuccessMsg(card);
    }

    /**
     * 卡充值
     * @param session
     * @return
     */
    @RequestMapping("cardCharge")
    @ResponseBody
    @AOPLogAnnotation(description = "卡充值",displayTitle = "卡编号,充值金额",displayItemFromDataStr = "tagId,chargeAmount")
    public BaseApiResultMsg cardCharge(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            JSONObject object = JSONObject.parseObject(dataStr);
            return cardService.cardCharge(null, object.getString("objectIdStr"), shopId.toString(), object.getBigDecimal("chargeAmount"), user.get_id().toString(), object.getString("remark"), CardChargeTypeEnum.backend.getCode());
        } catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "操作失败！");
        }
    }

    /**
     * 激活卡
     * @param tagId
     * @param session
     * @return
     */
    @RequestMapping("activateCard")
    @ResponseBody
    @AOPLogAnnotation(description = "卡激活")
    public ResultMsg activateCard(String tagId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            cardService.activateCard(tagId, shopId);
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    /**
     * 冻结卡
     * @param tagId
     * @param session
     * @return
     */
    @RequestMapping("frozenOrUnfrozenCard")
    @ResponseBody
    @AOPLogAnnotation(description = "冻结/解冻会员卡",displayTitle = "卡编号",displayItemFromParameter = "tagId")
    public ResultMsg frozenCard(String tagId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            cardService.frozenOrUnfrozenCard(tagId, shopId);
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    //批量导入会员卡
    @RequestMapping("importCardsForm")
    public String importCardsForm(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        //获取本店方案
        List<CardType> cardTypes = cardTypeService.findAvailableCardType(shopId);
        model.addAttribute("cardTypes", cardTypes);
        //获取总店公开方案
        List<CardType> publicCardTypes = cardTypeService.findMainShopCardType(shopId, user);
        model.addAttribute("publicCardType", publicCardTypes);
        return "bk/card/card-import";
    }

    @RequestMapping("importCardsPreview")
    public String importCardsPreview(MultipartFile file, Integer medium, Boolean isPublic, String cardTypeIdStr, String startTime, String endTime, Integer cardState, HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            List<CardImportVo> cardList = cardService.previewCards(file, medium, isPublic, cardTypeIdStr, startTime, endTime, cardState, shopId);
            model.addAttribute("cards", JSONArray.toJSON(cardList));
        }catch (Exception e){
            logger.error(e.getMessage());
            model.addAttribute("error", true);
        }

        return "bk/card/card-import-preview";
    }

    /**
     * 批量导入会员卡提交
     * @param session
     * @return
     */
    @RequestMapping("/importCardsSubmit")
    @ResponseBody
    @AOPLogAnnotation(description = "批量导入会员卡")
    public ResultMsg importCards(String dataStr,  HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            int importCount = cardService.importCards(dataStr, shopId, user.get_id().toString());
            return ResultMsg.buildSuccessMsg(importCount);
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
    }

    @RequestMapping("cardBatch")
    public String cardBatch(){
        return "bk/card/card-batches";
    }

    /**
     * 获取批次
     * @param session
     * @return
     */
    @RequestMapping("getCardBatches")
    @ResponseBody
    public List<CardBatch> getCouponBatches(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        return cardBatchService.findBatches(shopId, false);
    }

    /**
     * 同一批次券管理
     * @return
     */
    @RequestMapping("batchCards")
    public String batchCoupons(String batchId, Model model){
        model.addAttribute("batchId", batchId);
        return "bk/card/batch-cards";
    }

    /**
     * 获取同一批次的券
     * @param batchId
     * @param session
     * @return
     */
    @RequestMapping("getBatchCards")
    @ResponseBody
    public List<Card> getBatchCoupons(String batchId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        return cardService.findByBatchId(shopId, batchId);
    }


    /**
     * 卡流水管理
     * @return
     */
    @RequestMapping("/cardFlows")
    public String cardFlows(){
        return "bk/card/card-flows";
    }

    /**
     * 获取卡流水
     * @param session
     * @return
     */
    @RequestMapping("getCardFlows")
    @ResponseBody
    public List<CardFlow> getCardFlows(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        return cardFlowService.getByOwnerShop(shopId.toString());
    }

    /**
     * 流水详情
     * @return
     */
    @RequestMapping("cardFlowDetail")
    public String cardFlowDetail(String seriesNumber, Model model){
        CardFlow flow = cardFlowService.getBySeriesNo(seriesNumber, null);
        model.addAttribute("flow", flow);
        return "bk/card/card-flow-detail";
    }

    @RequestMapping("generateCardsForm")
    public String generateCards(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        //获取本店方案
        List<CardType> cardTypes = cardTypeService.findAvailableCardType(shopId);
        model.addAttribute("cardTypes", cardTypes);
        //获取总店方案
        List<CardType> publicCardTypes = cardTypeService.findMainShopCardType(shopId, user);
        model.addAttribute("publicCardType", publicCardTypes);
        return "bk/card/card-generate";
    }

    /**A
     * 批量生成会员卡提交
     * @param session
     * @return
     */
    @RequestMapping("/generateCardsSubmit")
    @ResponseBody
    @AOPLogAnnotation(description = "卡生成",option = LogOptionEnum.GENERATE_CARD)
    public ResultMsg generateCardsSubmit(String dataStr,  HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        User user = SpringSecurityUtil.getCurrentUser();
        try {
            CardBatch cardBatch = JSONArray.parseObject(dataStr, CardBatch.class);
            cardService.generateCards(cardBatch, shopId, user.get_id().toString());
            return ResultMsg.buildSuccessMsg();
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
    }


    @RequestMapping("weixinMembers")
    public String weixinMember(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        List<CardType> cardTypes = cardTypeService.findAvailableCardType(shopId);
        model.addAttribute("cardTypes", cardTypes);
        return "/bk/card/weixin-members";
    }

    @RequestMapping("getWeixinMembers")
    @ResponseBody
    public List<Card> getWeixinMembers(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        CardFilterVo filterVo = SerializationUtil.deSerializeObject(dataStr, CardFilterVo.class);
        if (ValidatorUtil.isNull(filterVo)){
            filterVo = new CardFilterVo();
        }
        filterVo.setMedium(CardTypeEnum.virtual.getCode());
        return cardService.findCards(filterVo, shopId, false);
    }

    /**
     * 发送优惠券给微会员
     * @return
     */
    @RequestMapping("sendCoupon2Card")
    public String sendCouponToCard(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        //获取本店方案
        List<CardType> cardTypes = cardTypeService.findAvailableCardType(shopId);
        model.addAttribute("cardTypes", cardTypes);
        //获取总店方案
        List<CardType> couponTypes = couponTypeService.findAvailableCouponType(shopId);
        model.addAttribute("couponTypes", couponTypes);
        return "/bk/card/send-coupon-to-card";
    }

    @RequestMapping("sendCoupon2CardSubmit")
    @ResponseBody
    @AOPLogAnnotation(description = "定向发送券给微会员")
    public ResultMsg sendCoupon2CardSubmit(String dataStr, String couponTypesStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            CardFilterVo filterVo = SerializationUtil.deSerializeObject(dataStr, CardFilterVo.class);
            List<CouponSendVo> couponSendVos = SerializationUtil.deSerializeList(couponTypesStr, CouponSendVo.class);
            Integer count = cardService.sendCoupon2Card(filterVo, couponSendVos, shopId);
            return ResultMsg.buildSuccessMsg(count);
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
    }

}
