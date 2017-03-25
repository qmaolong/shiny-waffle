package com.covilla.controller.bk.shop;

import com.alibaba.fastjson.JSONArray;
import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.model.mongo.printer.PrintScheme;
import com.covilla.model.mongo.printer.TicketForm;
import com.covilla.model.mongo.printer.TicketItemForm;
import com.covilla.model.mongo.shop.Printer;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.ServiceException;
import com.covilla.service.config.ConfigService;
import com.covilla.service.shop.PrinterService;
import com.covilla.service.shop.ShopService;
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
 * Created by qmaolong on 2016/10/10.
 */
@Controller
@RequestMapping("merchant/admin/printer")
public class PrinterController {
    private Logger logger = LoggerFactory.getLogger(PrinterController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private PrinterService printerService;
    @Autowired
    private ConfigService configService;

    //打印机列表
    @RequestMapping("printers")
    public String printers(){
        return "bk/printer/printers";
    }

    //打印机编辑页
    @RequestMapping("printerInput")
    public String printerInput(String oper, String id, Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("printers", shop.getPrinters());
            model.addAttribute("kitchenForms", shop.getKitchenForms());
            model.addAttribute("controlForms", shop.getControlForms());
        }
        model.addAttribute("id", id);
        model.addAttribute("oper", oper);
        return "bk/printer/printer-input";
    }

    //获取打印机列表
    @RequestMapping("getPrinters")
    @ResponseBody
    public List<Printer> getPrinters(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getPrinters();
        }
        return null;
    }

    //编辑打印机提交
    @RequestMapping("editPrinter")
    @ResponseBody
    @AOPLogAnnotation(description = "打印机")
    public ResultMsg editPrinter(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            printerService.updatePrinter(dataStr, shopId, oper);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    //厨打格式
    @RequestMapping("kitchenForms")
    public String kitchenForms(){
        return "bk/printer/kitchen-forms";
    }

    //小票格式
    @RequestMapping("ticketForms")
    public String ticketForms(){
        return "bk/printer/ticket-forms";
    }

    //控菜格式
    @RequestMapping("controlForms")
    public String controlForms(){
        return "bk/printer/control-forms";
    }

    //格式编辑页
    @RequestMapping("ticketFormInput")
    public String kitchenInput(String oper, Integer category, Model model){
        model.addAttribute("oper", oper);
        model.addAttribute("category", category);
        return "bk/printer/kitchen-form-input";
    }

    //获取格式打印项
    @RequestMapping("getTicketForms")
    @ResponseBody
    public List<TicketForm> getKitchenForms(HttpSession session, Integer category){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        return printerService.getTicketForms(shopId, category);
    }

    //打印项编辑页
    @RequestMapping("itemInput")
    public String itemInput(String oper, Integer category, Integer zone, Model model){
        model.addAttribute("oper", oper);
        model.addAttribute("category", category);
        model.addAttribute("zone", zone);
        return "bk/printer/ticket-item-input";
    }

    //编辑打印格式
    @RequestMapping("editTicketForm")
    @ResponseBody
    @AOPLogAnnotation(description = "打印格式")
    public ResultMsg editKitchenForm(String dataStr, Integer category, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            printerService.updateTicketForm(dataStr, shopId, category, oper);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    //选择打印项
    @RequestMapping("chooseItems")
    public String chooseItems(Integer category, Integer zone, Model model){
        model.addAttribute("category", category);
        model.addAttribute("zone", zone);
        return "bk/printer/choose-items";
    }

    //获取打印项列表
    @RequestMapping("getItems")
    @ResponseBody
    public List<TicketItemForm> getItems(Integer category, Integer zone){
        return configService.getTicketItems(category, zone);
    }

    /**
     * 厨打方案
     * @return
     */
    @RequestMapping("kitchenPrintSchemes")
    public String kitchenPrintSchemes(){
        return "bk/printer/kitchen-print-scheme";
    }

    /**
     * 获取厨打方案
     * @param session
     * @return
     */
    @RequestMapping("getPrintSchemes")
    @ResponseBody
    public List<PrintScheme> getPrintSchemes(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getPrintSchemes();
        }
        return null;
    }

    //编辑页
    @RequestMapping("printSchemeInput")
    public String printSchemeInput(String oper, Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        model.addAttribute("rooms", JSONArray.toJSON(shop.getRooms()));
        model.addAttribute("oper", oper);
        return "bk/printer/print-scheme-input";
    }

    /**
     * 编辑提交
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("printSchemeEdit")
    @ResponseBody
    @AOPLogAnnotation(description = "厨打方案")
    public ResultMsg printSchemeEdit(String dataStr, String itemsStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            printerService.updatePrinterPolicy(dataStr, itemsStr, shopId, oper);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("printSchemeItemInput")
    public String printSchemeItemInput(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("printers", shop.getPrinters());
        }
        return "bk/printer/printer-policy-item-input";
    }
}
