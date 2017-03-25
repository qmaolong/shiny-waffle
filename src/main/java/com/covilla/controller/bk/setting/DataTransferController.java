package com.covilla.controller.bk.setting;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.common.DataItemEnum;
import com.covilla.service.ServiceException;
import com.covilla.service.food.FoodService;
import com.covilla.service.setting.DataTransferService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ContentUtil;
import com.covilla.util.DateUtil;
import com.covilla.vo.ResultMsg;
import org.bson.types.ObjectId;
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
import java.util.Date;

/**
 * Created by qmaolong on 2016/12/9.
 */
@Controller
@RequestMapping("/merchant/admin/setting")
public class DataTransferController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private DataTransferService dataTransferService;

    /**
     * 数据传输页
     * @return
     */
    @RequestMapping("/dataTransfer")
    public String dataTransfer(Model model){
        model.addAttribute("dataItems", DataItemEnum.values());
        return "bk/setting/data-transfer";
    }

    /**
     * 数据导出页
     * @return
     */
    @RequestMapping("/dataOutport")
    @ResponseBody
    @AOPLogAnnotation(description = "数据导出")
    public byte[] dataOutport(HttpSession session, HttpServletResponse response){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode(DateUtil.formateDateToStr(new Date(), "yyyy-MM-dd-HHmmss")+".backup","UTF-8"));
            return dataTransferService.getBackupData(shopId);
        }catch (Exception e){
            response.setStatus(400);
            return null;
        }
    }

    /**
     * 数据导入
     * @return
     */
    @RequestMapping("/dataImport")
    @ResponseBody
    @AOPLogAnnotation(description = "数据导入")
    public ResultMsg dataImport(MultipartFile file, String recoverItem, HttpServletRequest request, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            String path = request.getSession().getServletContext().getRealPath("static/asset/upload");
            String fileName = file.getOriginalFilename();
            if(!fileName.endsWith(".backup")){
                return ResultMsg.buildFailMsg("-1", "文件格式不支持~");
            }
            File targetFile = new File(path, fileName);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            byte[] shopByter = ContentUtil.readFileByBytes(targetFile);
            dataTransferService.importData(shopByter, shopId, recoverItem);
        } catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e) {
            return ResultMsg.buildFailMsg("-1", "系统错误");
        }
        return ResultMsg.buildSuccessMsg();
    }


}
