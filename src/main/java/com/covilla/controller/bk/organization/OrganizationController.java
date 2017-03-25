package com.covilla.controller.bk.organization;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.MemberRightEnum;
import com.covilla.common.OperationEnum;
import com.covilla.model.mongo.food.Food;
import com.covilla.model.mongo.organization.Clerk;
import com.covilla.model.mongo.organization.Section;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.organization.ClerkService;
import com.covilla.service.organization.SectionService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 组织管理
 * Created by qmaolong on 2016/8/30.
 */
@Controller
@RequestMapping("merchant/admin/organization")
public class OrganizationController {
    Logger logger = LoggerFactory.getLogger(OrganizationController.class);
    @Autowired
    private SectionService sectionService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ClerkService clerkService;

    /**
     * 部门管理
     * @return
     */
    @RequestMapping("sections")
    public String sections(Model model){
        MemberRightEnum[] memberRightEnums = MemberRightEnum.values();
        model.addAttribute("authorities", memberRightEnums);
        return "bk/organization/sections";
    }

    /**
     * 部门列表
     * @param session
     * @return
     */
    @RequestMapping("getSectionList")
    @ResponseBody
    public List<Section> getSectionList(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            return shop.getSections();
        }
        return null;
    }

    /**
     * 操作
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editSection")
    @ResponseBody
    @AOPLogAnnotation(description = "部门")
    public ResultMsg editSection(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            sectionService.updateSection(dataStr, shopId, oper);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }

    /**
     * 员工管理
     * @return
     */
    @RequestMapping("clerks")
    public String clerks(HttpSession session, Model model){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        if(ValidatorUtil.isNull(shopId)){
            return null;
        }
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            model.addAttribute("sectionList", shop.getSections());
        }
        MemberRightEnum[] memberRightEnums = MemberRightEnum.values();
        model.addAttribute("authorities", memberRightEnums);
        return "bk/organization/clerks";
    }

    /**
     * 员工列表
     * @return
     */
    @RequestMapping("getClerkList")
    @ResponseBody
    public List<Clerk> getClerkList(Integer sectionId, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        List<Clerk> result = new ArrayList<Clerk>();
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getSections())&&ValidatorUtil.isNotNull(sectionId)){
            for(Section section : shop.getSections()){
                if(sectionId.equals(section.getId()) && ValidatorUtil.isNotNull(section.getClerks())){
                    result = section.getClerks();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 操作员工信息
     * @param dataStr
     * @param oper
     * @param session
     * @return
     */
    @RequestMapping("editClerk")
    @ResponseBody
    @AOPLogAnnotation(description = "员工",displayTitle = "名称,id",displayItemFromDataStr = "name,id")
    public ResultMsg editClerk(String dataStr, String oper, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            if(OperationEnum.delete.getCode().equals(oper)){
                List<Clerk> clerks = SerializationUtil.deSerializeList(dataStr, Clerk.class);
                clerkService.deleteClerks(shopId, clerks);
            }else {
                Clerk clerk = SerializationUtil.deSerializeObject(dataStr, Clerk.class);
                clerkService.updateClerk(clerk, shopId, oper);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }

        return ResultMsg.buildSuccessMsg();
    }
}
