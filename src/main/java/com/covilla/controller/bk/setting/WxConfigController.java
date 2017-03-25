package com.covilla.controller.bk.setting;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.model.mongo.setting.WxSupport;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.util.FileUploader;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.ResultMsg;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * 后台公众号配置
 * Created by qmaolong on 2017/2/23.
 */
@Controller
@RequestMapping("/merchant/admin/setting")
public class WxConfigController {
    @Autowired
    private ShopService shopService;

    @RequestMapping("/wxConfig")
    public String wxConfig(Model model, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNotNull(shop)){
            model.addAttribute("wxConfig", shop.getWxSupport());
        }
        return "bk/setting/wx-config";
    }

    @RequestMapping("editWxConfig")
    @ResponseBody
    @AOPLogAnnotation(description = "修改公众号参数")
    public ResultMsg editWxConfig(MultipartFile file, String appId, String appSecret, String appName, boolean useSystem, HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        try {
            Shop shop = shopService.findBy_id(shopId);
            WxSupport wxSupport = new WxSupport();
            wxSupport.setUseSystem(useSystem);

            if (!useSystem){//使用私有的配置
                wxSupport.setAppId(appId);
                wxSupport.setAppName(appName);
                wxSupport.setAppSecret(appSecret);
                //判断格式
                if (!file.getOriginalFilename().endsWith(".txt")){
                    throw new ServiceException("文件格式不支持");
                }
                if (file.getSize() > 1024){
                    throw new ServiceException("图片大小不能超过1k");
                }

                String relativePath = "MP_verify";
                String fileName = file.getOriginalFilename();
                FileUploader.upload(file, relativePath, fileName);
            }else {//使用系统配置
                Shop dataShop = shopService.findDataShop();
                if (ValidatorUtil.isNotNull(dataShop.getWxSupport())){
                    wxSupport = dataShop.getWxSupport();
                }
            }
            shop.setWxSupport(wxSupport);
            shopService.updateDocument(shop);

            return ResultMsg.buildSuccessMsg();
        }catch (ServiceException se){
            return ResultMsg.buildFailMsg("-1", se.getMessage());
        }catch (Exception e){
            return ResultMsg.buildFailMsg("-1", "系统错误~");
        }
    }
}
