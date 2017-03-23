package com.covilla.controller.bk.purchase;

import com.covilla.common.Constant;
import com.covilla.model.mongo.purchase.Area;
import com.covilla.model.mongo.purchase.Supplier;
import com.covilla.service.ServiceException;
import com.covilla.service.purchase.SupplierService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.ResultMsg;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by qmaolong on 2017/3/15.
 */
@Controller
@RequestMapping("/merchant/admin/purchase")
public class PurchaseController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ShopService shopService;
    @Autowired
    private SupplierService supplierService;
    private ObjectId shopId;

    @ModelAttribute
    public void setShopId(HttpSession session){
        shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
    }

    /**
     * 区域
     * @return
     */
    @RequestMapping("areas")
    public String area(){
        return "bk/purchase/areas";
    }

    @RequestMapping("getAreas")
    @ResponseBody
    public List<Area> getAreas(){
        return supplierService.getAreasByShop(shopId);
    }

    @RequestMapping("editArea")
    @ResponseBody
    public ResultMsg editArea(String dataStr, String oper){
        try {
            supplierService.editArea(dataStr, oper, shopId);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("suppliers")
    public String suppliers(Model model){
        List<Area> areas = supplierService.getAreasByShop(shopId);
        model.addAttribute("areas", areas);
        return "bk/purchase/suppliers";
    }

    @RequestMapping("getSuppliers")
    @ResponseBody
    public List<Supplier> getSuppliers(String areaId){
        Area area = supplierService.findAreaByShop(areaId, shopId);
        if (ValidatorUtil.isNotNull(area)){
            return area.getSuppliers();
        }
        return null;
    }

    @RequestMapping("editSupplier")
    @ResponseBody
    public ResultMsg editSupplier(String dataStr, String oper){
        try {
            supplierService.editSupplier(dataStr, oper, shopId);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.toString());
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
        return ResultMsg.buildSuccessMsg();
    }
}
