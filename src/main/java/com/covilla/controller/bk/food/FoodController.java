package com.covilla.controller.bk.food;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.*;
import com.covilla.model.mongo.food.Food;
import com.covilla.model.mongo.food.OptionContainer;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.ServiceException;
import com.covilla.service.food.FoodService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ContentUtil;
import com.covilla.util.DateUtil;
import com.covilla.util.SerializationUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.ResultMsg;
import com.covilla.vo.food.FoodImportVo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜品管理
 * Created by qmaolong on 2016/8/30.
 */
@Controller
@RequestMapping("merchant/admin/food")
public class FoodController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ShopService shopService;
    @Autowired
    private FoodService foodService;

    /**
     * 品项管理
     * @param model
     * @return
     */
    @RequestMapping("foodList")
    public String foodList(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("categories", JSONArray.toJSON(shop.getCategories()));
        }
        return "bk/food/food-list";
    }

    /**
     * 菜品列表
     * @param cateChildrenId
     * @return
     */
    @RequestMapping("getFoodList")
    @ResponseBody
    public List<Food> getFoodList( String cateChildrenId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return foodService.getAvailableFoods(shop.get_id(), cateChildrenId);
        }
        return new ArrayList<Food>();
    }

    @RequestMapping("foodInput")
    public String foodInput(Integer type, String oper, Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("categories", JSONArray.toJSON(shop.getCategories()));
            model.addAttribute("printers", shop.getPrinters());
            model.addAttribute("printerPolicies", shop.getPrintSchemes());
            model.addAttribute("units", shop.getUnits());
        }

        model.addAttribute("properties", FoodPropertyEnum.values());
        model.addAttribute("type", type);
        model.addAttribute("oper", oper);
        return "bk/food/food-input";
    }

    @RequestMapping("comboInput")
    public String comboInput(String oper, Model model){
        model.addAttribute("oper", oper);
        return "bk/food/combos-input";
    }

    @RequestMapping("chooseFood")
    public String chooseFood(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("categories", JSONArray.toJSON(shop.getCategories()));
        }
        return "bk/food/choose-food";
    }

    @RequestMapping("batchChooseFood")
    public String batchChooseFood(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("categories", JSONArray.toJSON(shop.getCategories()));
        }
        return "bk/food/batch-choose-food";
    }

    @RequestMapping("optionInput")
    public String optionInput(String oper, Model model, HttpSession session){
        model.addAttribute("oper", oper);
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        List<OptionContainer> hisOptions = foodService.findHisFoodOptions(shopId);
        model.addAttribute("hisOptions", JSONArray.toJSON(hisOptions));
        return "bk/food/options-input";
    }

    /**
     * 操作菜品
     * @param dataStr
     * @param oper
     * @return
     */
    @RequestMapping("editFood")
    @ResponseBody
    @AOPLogAnnotation(description = "菜品",displayTitle = "名称,ID",displayItemFromDataStr = "name,id")
    public ResultMsg editFood(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            foodService.editFood(dataStr, oper, shopId);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("batchSetPrinters")
    public String batchSetPrinters(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("categories", JSONArray.toJSON(shop.getCategories()));
            model.addAttribute("printers", shop.getPrinters());
            model.addAttribute("printerPolicies", shop.getPrintSchemes());
        }
        model.addAttribute("properties", FoodPropertyEnum.values());
        return "bk/food/batch-set-printers";
    }

    @RequestMapping("setPrinters")
    @ResponseBody
    @AOPLogAnnotation(description = "批量设置菜品")
    public ResultMsg setPrinters(String foods, String formData, HttpSession session){
        System.out.print(foods);
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            foodService.batchSetPrinters(foods, formData, shopId);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("foodImport")
    public String foodImport(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("printers", shop.getPrinters());
            model.addAttribute("printerPolicies", shop.getPrintSchemes());
        }
        return "bk/food/food-import";
    }

    @RequestMapping("foodImportPreview")
    @ResponseBody
    public ResultMsg foodImportPreview(MultipartFile file, HttpSession session, HttpServletRequest request){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            String path = request.getSession().getServletContext().getRealPath("static/asset/upload");
            String fileName = file.getOriginalFilename();
            if(!fileName.endsWith(".csv")){
                return ResultMsg.buildFailMsg("-1", "文件格式不支持~");
            }
            File targetFile = new File(path, fileName);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            String contentStr = ContentUtil.readFileByChars(targetFile);
            List<FoodImportVo> cardList = foodService.importPreview(contentStr, shopId);
            return ResultMsg.buildSuccessMsg(cardList);
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e) {
            return ResultMsg.buildFailMsg("-1", "系统错误");
        }
    }

    /**
     * 菜品导入预览页
     * @return
     */
    @RequestMapping("foodImportPreviewPage")
    public String foodImportPreviewPage(){
        return "bk/food/food-import-preview";
    }

    @RequestMapping("foodImportSubmit")
    @ResponseBody
    @AOPLogAnnotation(description = "菜品导入")
    public ResultMsg foodImportSubmit(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            Integer importCount = foodService.foodImportSubmit(dataStr, shopId);
            return ResultMsg.buildSuccessMsg(importCount);
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
    }

    /**
     * 菜品导出
     * @param cateId
     * @param session
     * @return
     */
    @RequestMapping("foodOutput")
    @ResponseBody
    @AOPLogAnnotation(description = "菜品导出")
    public byte[] foodOutput(String cateId, HttpSession session, HttpServletResponse response){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            String fileName = URLEncoder.encode("foods" + cateId + "-" + DateUtil.formateDateToStr(new Date(), "yyyyMMddHHmmss") +".csv","UTF-8");
            response.setHeader("content-disposition","attachment;filename="+ fileName);
            return foodService.foodOutput(shopId, cateId).getBytes();
        }catch (ServiceException se){
            response.setStatus(400);
            return null;
        } catch (Exception e){
            logger.error(e.getMessage());
            response.setStatus(400);
            return null;
        }
    }

    /**
     * 引用总店的菜品
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("quoteFromSuper")
    public String quoteFromSuper(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNotNull(shop)){
            model.addAttribute("categories", JSONArray.toJSON(shop.getCategories()));
        }
        Shop superShop = shopService.findMainShopByShopId(shopId);
        if (ValidatorUtil.isNotNull(superShop)){
            model.addAttribute("superCategories", JSONArray.toJSON(superShop.getCategories()));
        }
        return "bk/food/quote-from-super";
    }

    /**
     * 获取总店的菜品
     * @param cateChildrenId
     * @param session
     * @return
     */
    @RequestMapping("findSuperFood")
    @ResponseBody
    public List<Food> findSuperFood(String cateChildrenId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop superShop = shopService.findMainShopByShopId(shopId);
        if(ValidatorUtil.isNotNull(superShop)){
            List<Food> superFoods = foodService.getAvailableFoods(superShop.get_id(), cateChildrenId);
            List<Food> refFoods = foodService.findRefFoods(shopId);
            for (int i=0; i<superFoods.size(); i++){
                for (Food refFood : refFoods){
                    if (refFood.getId().equals(superFoods.get(i).getId())){
                        superFoods.remove(i);
                    }
                }
            }
            return superFoods;
        }
        return null;
    }

    @RequestMapping("quoteFromSuperPreview")
    public String quoteFromSuperPreview(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("categories", JSONArray.toJSON(shop.getCategories()));
            model.addAttribute("printers", shop.getPrinters());
            model.addAttribute("printerPolicies", shop.getPrintSchemes());
            model.addAttribute("units", shop.getUnits());
        }
        model.addAttribute("properties", FoodPropertyEnum.values());
        return "bk/food/quote-from-super-preview";
    }

    /**
     * 获取id已存在的菜品
     * @param foods
     * @param session
     * @return
     */
    /*@RequestMapping("findExistFoods")
    @ResponseBody
    public List<Food> findExistFoods(String foods, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        List<Food> foodList = JSONArray.parseArray(foods, Food.class);
        return foodService.findExistFoodsByIds(foodList, shopId);
    }*/

    @RequestMapping("quoteFromSuperSubmit")
    @ResponseBody
    @AOPLogAnnotation(description = "从总店导入菜品",option = LogOptionEnum.BUSINESS)
    public ResultMsg quoteFromSuperSubmit(String foodStr, String configStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            List<Food> foodList = SerializationUtil.deSerializeList(foodStr, Food.class);
            JSONObject config = JSONObject.parseObject(configStr);
            foodService.quoteFromSuperSubmit(foodList, config, shopId);
            return ResultMsg.buildSuccessMsg();
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
    }
}
