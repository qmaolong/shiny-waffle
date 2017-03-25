package com.covilla.controller.bk.food;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.OperationEnum;
import com.covilla.model.mongo.food.Category;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.Children;
import com.covilla.service.food.CategoryService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.SerializationUtil;
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
 * 分类管理
 * Created by qmaolong on 2016/8/30.
 */
@Controller
@RequestMapping("merchant/admin/category")
public class CategoryController {
    Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 单位管理
     * @return
     */
    @RequestMapping("categories")
    public String categories(){
        return "bk/food/category-list";
    }

    /**
     * 单位列表
     * @param session
     * @return
     */
    @RequestMapping("getCategories")
    @ResponseBody
    public List<Category> getCategories(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getCategories();
        }
        return null;
    }

    /**
     * 操作
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editCategory")
    @ResponseBody
    @AOPLogAnnotation(description = "大类")
    public ResultMsg editCategory(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            if(OperationEnum.add.getCode().equals(oper)){
                Category category = SerializationUtil.deSerializeObject(dataStr, Category.class);
                categoryService.addCategoryToShop(shopId, category);
            }else if(OperationEnum.edit.getCode().equals(oper)){
                Category category = SerializationUtil.deSerializeObject(dataStr, Category.class);
                categoryService.updateCategory(shopId, category);
            }else if(OperationEnum.delete.getCode().equals(oper)){
                List<Category> categories = SerializationUtil.deSerializeList(dataStr, Category.class);
                categoryService.deleteCategoryFromShop(shopId, categories);
            }else {
                return ResultMsg.buildFailMsg("-1", "操作失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("cateChildren")
    public String cateChildren(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return "redirect:/merchant/exit";
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("categories", shop.getCategories());
        }
        return "bk/food/cate-children";
    }

    /**
     * 获取小类
     * @param cateId
     * @return
     */
    @RequestMapping("getCateChildren")
    @ResponseBody
    public List<Children> getCateChildren(String cateId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCategories())){
            for(Category category : shop.getCategories()){
                if(cateId.equals(category.getId())){
                    return category.getChildren();
                }
            }
        }
        return null;
    }

    /**
     * 操作
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editCateChild")
    @ResponseBody
    @AOPLogAnnotation(description = "小类")
    public ResultMsg editCateChild(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            if(OperationEnum.delete.getCode().equals(oper)){
                List<Children> childrenList = SerializationUtil.deSerializeList(dataStr, Children.class);
                categoryService.deleteChildrens(shopId, childrenList);
            }else{
                Children children = SerializationUtil.deSerializeObject(dataStr, Children.class);
                categoryService.editChildren(shopId, children, oper);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

}
