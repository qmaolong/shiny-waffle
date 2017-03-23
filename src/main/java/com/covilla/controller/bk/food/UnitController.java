package com.covilla.controller.bk.food;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.OperationEnum;
import com.covilla.model.mongo.food.Unit;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.food.UnitService;
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
 * 单位管理
 * Created by qmaolong on 2016/8/30.
 */
@Controller
@RequestMapping("merchant/admin/unit")
public class UnitController {
    private Logger logger = LoggerFactory.getLogger(UnitController.class);
    @Autowired
    private UnitService unitService;
    @Autowired
    private ShopService shopService;

    /**
     * 单位管理
     * @return
     */
    @RequestMapping("unit")
    public String unit(){

        return "bk/food/unit-list";
    }

    /**
     * 单位列表
     * @param session
     * @return
     */
    @RequestMapping("getUnitList")
    @ResponseBody
    public List<Unit> getUnitList(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getUnits();
        }
        return null;
    }

    /**
     * 操作
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editUnit")
    @ResponseBody
    @AOPLogAnnotation(description = "单位")
    public ResultMsg editUnit(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            if(OperationEnum.add.getCode().equals(oper)){
                Unit unit = SerializationUtil.deSerializeObject(dataStr, Unit.class);
                unitService.addUnitToShop(shopId, unit);
            }else if(OperationEnum.edit.getCode().equals(oper)){
                Unit unit = SerializationUtil.deSerializeObject(dataStr, Unit.class);
                unitService.updateUnit(shopId, unit);
            }else if(OperationEnum.delete.getCode().equals(oper)){
                List<Unit> units = SerializationUtil.deSerializeList(dataStr, Unit.class);
                unitService.deleteUnitFromShop(shopId, units);
            }else {
                return ResultMsg.buildFailMsg("-1", "操作失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("foodList")
    public String foodList(){
        return "bk/food/food-list";
    }
}
