package com.covilla.controller.bk.food;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.OperationEnum;
import com.covilla.model.mongo.food.Taste;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.food.TastService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.SerializationUtil;
import com.covilla.util.ValidatorUtil;
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
 * 口味管理
 * Created by qmaolong on 2016/8/30.
 */
@Controller
@RequestMapping("merchant/admin/taste")
public class TastController {
    private Logger logger = LoggerFactory.getLogger(TastController.class);
    @Autowired
    private TastService tastService;
    @Autowired
    private ShopService shopService;

    /**
     * 单位管理
     * @return
     */
    @RequestMapping("tastes")
    public String unit(){

        return "bk/food/taste-list";
    }

    /**
     * 单位列表
     * @param session
     * @return
     */
    @RequestMapping("getTasteList")
    @ResponseBody
    public List<Taste> getUnitList(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getTastVo();
        }
        return null;
    }

    /**
     * 操作
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editTaste")
    @ResponseBody
    @AOPLogAnnotation(description = "口味")
    public ResultMsg editCashiers(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            if(OperationEnum.delete.getCode().equals(oper)){
                List<Taste> tastes = SerializationUtil.deSerializeList(dataStr, Taste.class);
                tastService.delete(tastes, shopId);
            }else {
                Taste taste = SerializationUtil.deSerializeObject(dataStr, Taste.class);
                tastService.updateTaste(taste, shopId, oper);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }
}
