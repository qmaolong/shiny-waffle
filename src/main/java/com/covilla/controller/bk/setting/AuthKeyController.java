package com.covilla.controller.bk.setting;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.Constant;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.ServiceException;
import com.covilla.service.authKey.AuthKeyService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 密钥文件
 * Created by qmaolong on 2016/12/6.
 */
@Controller
@RequestMapping("/merchant/admin/setting")
public class AuthKeyController {
    @Autowired
    private AuthKeyService authKeyService;
    @Autowired
    private ShopService shopService;

    @RequestMapping("/authKeys")
    public String authKeys(){
        return "bk/setting/auth-keys";
    }

    @RequestMapping("/getAuthKeys")
    @ResponseBody
    public List<Map<String, String>> getAuthKeys(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)){
            List<Map<String, String>> keys = new ArrayList<Map<String, String>>();
            if(ValidatorUtil.isNotNull(shop.getAuthKeys())){
                for (String authKey: shop.getAuthKeys()){
                    Map<String, String> temp = new HashMap<String, String>();
                    temp.put("key", authKey);
                    keys.add(temp);
                }
            }
            return keys;
        }
        return null;
    }

    @RequestMapping("/downloadAuthKey")
    @ResponseBody
    @AOPLogAnnotation(description = "下载密钥文件",displayTitle = "key",displayItemFromParameter = "key")
    public String downloadAuthKey(String key, HttpServletResponse response){
        try {
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode(key + ".key","UTF-8"));
            return authKeyService.encodeAuthKey(key);
        }catch (ServiceException se){
            response.setStatus(400);
            return null;
        } catch (Exception e){
            response.setStatus(400);
            return null;
        }

    }
}
