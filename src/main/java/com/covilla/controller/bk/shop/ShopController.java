package com.covilla.controller.bk.shop;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.OperationEnum;
import com.covilla.model.mongo.shop.Desk;
import com.covilla.model.mongo.shop.DeskCat;
import com.covilla.model.mongo.shop.Room;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.Cashier;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.authKey.AuthKeyService;
import com.covilla.service.shop.*;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qmaolong on 2016/8/24.
 */
@Controller
@RequestMapping("merchant/admin/shop")
public class ShopController {
    Logger logger = LoggerFactory.getLogger(ShopController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private DeskCatService deskCatService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private DeskService deskService;
    @Autowired
    private CashierService cashierService;
    @Autowired
    private PrinterService printerService;
    @Autowired
    private AuthKeyService authKeyService;

    /**
     * 门店管理
     * @param model
     * @return
     */
    @RequestMapping("shopList")
    public String shopList(Model model){
        User user = SpringSecurityUtil.getCurrentUser();
        List<Shop> shops = shopService.findByOwner(user.get_id());
        model.addAttribute("shopList", shops);
        return "bk/shop/shop-list";
    }

    /**
     * 门店列表获取
     * @param session
     * @return
     */
    @RequestMapping("getShopList")
    @ResponseBody
    public List<Shop> getShopList(HttpSession session){
        User user = SpringSecurityUtil.getCurrentUser();
        if(ValidatorUtil.isNull(user)){
            return null;
        }
        List<Shop> result = shopService.findByOwner(user.get_id());
        return result;
    }

    /**
     * 门店编辑提交
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editShop")
    @ResponseBody
    @AOPLogAnnotation(description = "门店信息")
    public ResultMsg editShop(String dataStr, String oper, HttpSession session){
        User user = SpringSecurityUtil.getCurrentUser();
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            if(OperationEnum.add.getCode().equals(oper)){
                Shop shop = SerializationUtil.deSerializeObject(dataStr, Shop.class);
                shopService.addShopByUser(shop, user, shopId);
            }else if(OperationEnum.edit.getCode().equals(oper)){
                Shop shop = SerializationUtil.deSerializeObject(dataStr, Shop.class);
                shopService.updateShopInfo(shop);
            }else if(OperationEnum.delete.getCode().equals(oper)){
                List<Shop> shopList = SerializationUtil.deSerializeList(dataStr, Shop.class);
                for (Shop shop: shopList){
                    if(shop.getObjectIdStr().equals(shopId.toString())){
                        return ResultMsg.buildFailMsg("-1", "不允许删除当前门店！");
                    }
                }
                shopService.deleteShopInfo(shopList);
            }else {
                return ResultMsg.buildFailMsg("-1", "操作失败！");
            }
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    //================================桌型管理============================================================
    /**
     *桌型管理
     * @return
     */
    @RequestMapping("deskCat")
    public String deskCat(){
        return "bk/shop/desk-cat";
    }

    /**
     * 桌型列表
     * @return
     */
    @RequestMapping("getDeskCatList")
    @ResponseBody
    public List<DeskCat> getDeskCatList(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getDeskCat();
        }
        return null;
    }

    /**
     * 操作桌型
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editDeskCat")
    @ResponseBody
    @AOPLogAnnotation(description = "桌型")
    public ResultMsg editDeskCat(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            deskCatService.updateDeskCat(dataStr, shopId, oper);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    //======================================包间管理================================================================

    /**
     * 包间管理
     * @return
     */
    @RequestMapping("rooms")
    public String rooms(){
        return "bk/shop/rooms";
    }

    /**
     * 包间列表
     * @return
     */
    @RequestMapping("getRoomsList")
    @ResponseBody
    public List<Room> getRoomsList(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getRooms();
        }
        return null;
    }

    /**
     * 操作房间
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editRooms")
    @ResponseBody
    @AOPLogAnnotation(description = "区域")
    public ResultMsg editRooms(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            roomService.updateRoom(dataStr, shopId, oper);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    //==========================================桌台管理===========================================================

    /**
     * 桌台管理
     * @return
     */
    @RequestMapping("desks")
    public String desks(Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("roomsList", shop.getRooms());
            model.addAttribute("deskCatList", shop.getDeskCat());
        }
        return "bk/shop/desks";
    }

    /**
     * 桌台列表
     * @return
     */
    @RequestMapping("getDesksList")
    @ResponseBody
    public List<Desk> getRoomsList(Integer roomId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        List<Desk> result = new ArrayList<Desk>();
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getRooms())){
            for(Room room : shop.getRooms()){
                if(room.getId().equals(roomId) && ValidatorUtil.isNotNull(room.getDesks())){
                    result = room.getDesks();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 操作桌台
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editDesks")
    @ResponseBody
    @AOPLogAnnotation(description = "桌台")
    public ResultMsg editDesks(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            if(OperationEnum.delete.getCode().equals(oper)){
                List<Desk> desks = SerializationUtil.deSerializeList(dataStr, Desk.class);
                deskService.deleteClerks(shopId, desks);
            }else {
                Desk desk = SerializationUtil.deSerializeObject(dataStr, Desk.class);
                deskService.updateDesk(desk, shopId, oper);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    //============================================收银台管理==========================================================

    /**
     * 收银台管理
     * @return
     */
    @RequestMapping("cashiers")
    public String cashiers(){
        return "bk/shop/cashiers";
    }

    /**
     * 收银台列表
     * @return
     */
    @RequestMapping("getCashiersList")
    @ResponseBody
    public List<Cashier> getCashiersList(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getCashiers();
        }
        return null;
    }

    /**
     * 操作收银台
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editCashiers")
    @ResponseBody
    @AOPLogAnnotation(description = "收银台")
    public ResultMsg editCashiers(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            cashierService.updateCashier(dataStr, shopId, oper);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    /**
     * 解绑收银台
     * @param dataStr
     * @param session
     * @return
     */
    @RequestMapping("unBindCashier")
    @ResponseBody
    @AOPLogAnnotation(description = "收银台解绑")
    public ResultMsg unBindCashier(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            cashierService.unBindCashier(dataStr, shopId);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

}
