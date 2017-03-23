package com.covilla.controller.bk.setting;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.LogOptionEnum;
import com.covilla.service.ServiceException;
import com.covilla.service.setting.AdvertiseService;
import com.covilla.vo.ResultMsg;
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
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by qmaolong on 2017/1/11.
 */
@Controller
@RequestMapping("/merchant/admin/setting")
public class AdvertiseController {
    Logger logger = LoggerFactory.getLogger(AdvertiseController.class);
    @Autowired
    private AdvertiseService advertiseService;

    @RequestMapping("advertise")
    public String advertise(){
        return "/bk/setting/advertise";
    }

    @RequestMapping("getAdvertise")
    @ResponseBody
    public List<Map<String, Object>> getAdvertise(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        return advertiseService.getAdvertise(shopId);
    }

    @RequestMapping("advertiseInput")
    public String advertiseInput(String oper, Model model){
        model.addAttribute("oper", oper);
        return "/bk/setting/advertise-input";
    }

    @RequestMapping("addAdvertise")
    @ResponseBody
    @AOPLogAnnotation(description = "新增广告图片")
    public ResultMsg editAdvertise(MultipartFile file, HttpSession session, HttpServletRequest request){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            advertiseService.addAdvertise(file, shopId, request);
        }catch (ServiceException se){
            logger.error(se.getMessage());
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("deleteAdvertise")
    @ResponseBody
    @AOPLogAnnotation(description = "删除广告图片")
    public ResultMsg deleteAdvertise(String imgUrl, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            advertiseService.deleteAdvertise(imgUrl, shopId);
        }catch (ServiceException se){
            logger.error(se.getMessage());
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }

    @RequestMapping("sortAdvertise")
    @ResponseBody
    @AOPLogAnnotation(description = "广告重新排序", option = LogOptionEnum.BUSINESS)
    public ResultMsg sortAdvertise(String dataStr, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            advertiseService.sortAdvertise(dataStr, shopId);
        }catch (ServiceException se){
            logger.error(se.getMessage());
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultMsg.buildFailMsg("-1", "操作失败！");
        }
        return ResultMsg.buildSuccessMsg();
    }
}
