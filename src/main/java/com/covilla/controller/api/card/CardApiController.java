package com.covilla.controller.api.card;

import com.covilla.service.ServiceException;
import com.covilla.service.api.CardApiService;
import com.covilla.vo.BaseApiResultMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 卡业务api
 * Created by qmaolong on 2016/9/23.
 */
@Controller
@RequestMapping("api/card")
public class CardApiController {
    Logger logger = LoggerFactory.getLogger(CardApiController.class);
    @Autowired
    private CardApiService cardApiService;

    /**
     * 获取卡信息
     * @param mediumKey
     * @param shopId
     * @return
     */
    @RequestMapping("/getCardInfo")
    @ResponseBody
    public BaseApiResultMsg getCardInfo(String mediumKey, String shopId){
        try {
            return cardApiService.getCardInfo(mediumKey, shopId);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 卡消费
     * @param orderNo
     * @param orderAmount
     * @param cardPayAmount
     * @return
     */
    @RequestMapping("/payOrder")
    @ResponseBody
    public BaseApiResultMsg payOrder(String orderNo, BigDecimal orderAmount, BigDecimal cardPayAmount, String cardId, String body, String name, String shopId, String operator, String remark){
        try {
            return cardApiService.orderPay(orderNo, orderAmount, cardPayAmount, cardId, body, name, shopId, operator, remark);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    @RequestMapping("checkPayState")
    @ResponseBody
    public BaseApiResultMsg checkPayState(String orderNo, String seriesNumber, String shopId){
        try {
            return cardApiService.checkPayState(orderNo, seriesNumber, shopId);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 卡充值
     * @param cardId
     * @param amount
     * @param shopId
     * @param operator
     * @param remark
     * @return
     */
    @RequestMapping("cardCharge")
    @ResponseBody
    public BaseApiResultMsg cardCharge(String orderNo, String cardId, BigDecimal amount, String shopId, String operator, String remark){
        try {
            return cardApiService.cardCharge(orderNo, cardId, shopId, amount, operator, remark);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 消费撤销
     * @param orderNo
     * @param shopId
     * @param operator
     * @param remark
     * @return
     */
    @RequestMapping("rollBackPay")
    @ResponseBody
    public BaseApiResultMsg rollBackPay(String orderNo, String seriesNumber, String shopId, String operator, String remark){
        try {
            return cardApiService.rollbackPay(orderNo, seriesNumber, shopId, operator, remark);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 撤销充值
     * @param seriesNumber
     * @param shopId
     * @param operator
     * @param remark
     * @return
     */
    @RequestMapping("rollBackCharge")
    @ResponseBody
    public BaseApiResultMsg rollBackCharge(String seriesNumber, String shopId, String operator, String remark){
        try {
            return cardApiService.rollbackCharge(seriesNumber, shopId, operator, remark);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 计算可提现金额
     * @param cardId
     * @param shopId
     * @return
     */
    @RequestMapping("getWithDrawAmount")
    @ResponseBody
    public BaseApiResultMsg getWithDrawAmount(String cardId, String shopId){
        try {
            return cardApiService.calculateWithDrawAmount(cardId, shopId);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 卡余额提现
     * @param cardId
     * @param operator
     * @param remark
     * @return
     */
    @RequestMapping("withDraw")
    @ResponseBody
    public BaseApiResultMsg withDraw(String cardId, String shopId, String operator, String remark){
        try {
            return cardApiService.withDraw(cardId, shopId, operator, remark);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 注销卡
     * @param cardId
     * @param operator
     * @param remark
     * @return
     */
    @RequestMapping("returnCard")
    @ResponseBody
    public BaseApiResultMsg returnCard(String cardId, String shopId, String operator, String remark){
        try {
            return cardApiService.disableCard(cardId, shopId, operator, remark);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 激活卡
     * @param cardId
     * @param shopId
     * @param operator
     * @param remark
     * @return
     */
    @RequestMapping("activateCard")
    @ResponseBody
    public BaseApiResultMsg activateCard(String cardId, String shopId, String operator, String remark){
        try {
            return cardApiService.activateCard(cardId, shopId, operator, remark);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 使用折扣
     * @param cardId
     * @param operator
     * @param remark
     * @return
     */
    @RequestMapping("makeDiscount")
    @ResponseBody
    public BaseApiResultMsg useDiscount(String cardId, String orderNo, BigDecimal orderAmount, BigDecimal discountAmount, String discountRuleName, String shopId, String operator, String remark){
        try {
            return cardApiService.makeDiscount(cardId, orderNo, orderAmount, discountAmount, discountRuleName, shopId, operator, remark);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

    /**
     * 撤销折扣
     * @param operator
     * @param remark
     * @return
     */
    @RequestMapping("rollbackDiscount")
    @ResponseBody
    public BaseApiResultMsg rollbackDiscount(String seriesNo, String shopId, String operator, String remark){
        try {
            return cardApiService.rollbackDiscount(seriesNo, shopId, operator, remark);
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
    }

}
