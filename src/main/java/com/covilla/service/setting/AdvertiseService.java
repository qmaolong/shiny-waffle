package com.covilla.service.setting;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.util.FileUploader;
import com.covilla.util.ValidatorUtil;
import org.apache.commons.collections.map.HashedMap;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qmaolong on 2017/1/11.
 */
@Service
public class AdvertiseService {
    @Autowired
    private ShopService shopService;

    public List<Map<String, Object>> getAdvertise(ObjectId shopId){
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getAdvertise())){
            for (String img : shop.getAdvertise()){
                Map<String, Object> item = new HashedMap();
                item.put("img", img);
                result.add(item);
            }
        }
        return result;
    }

    public void addAdvertise(MultipartFile file, ObjectId shopId, HttpServletRequest request){
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNull(shop)){
            throw new ServiceException("没有找到门店");
        }else if (ValidatorUtil.isNotNull(shop.getAdvertise()) && shop.getAdvertise().size()>=5){
            throw new ServiceException("广告图片最多不能超过5张");
        }else if (file.getSize() > 1048576){
            throw new ServiceException("图片大小不能超过1M");
        }
        //判断格式
        String[] supportImgs = {".jpg", ".JPG", ".jpeg", ".JPEG", ".png", ".PNG"};
        String format = null;
        for (String support : supportImgs){
            if (file.getOriginalFilename().endsWith(support)){
                format = support;
                break;
            }
        }
        if (ValidatorUtil.isNull(format)){
            throw new ServiceException("图片格式不支持");
        }

        String imgUrl = FileUploader.upload(file, FileUploader.FileTypeEnum.COMMON);

        if(ValidatorUtil.isNull(shop.getAdvertise())){
            shop.setAdvertise(new ArrayList<String>());
        }
        shop.getAdvertise().add(imgUrl);
        shopService.updateDocument(shop);
    }

    public void deleteAdvertise(String imgUrl, ObjectId shopId){
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNull(shop)){
            throw new ServiceException("没有找到门店");
        }else if (ValidatorUtil.isNotNull(shop.getAdvertise())){
            for (String adv : shop.getAdvertise()){
                if (adv.equals(imgUrl)){
                    shop.getAdvertise().remove(adv);
                    //删除文件
                    FileUploader.delete(imgUrl);
                    break;
                }
            }
            shopService.updateDocument(shop);
        }else {
            throw new ServiceException("没有可删除项");
        }
    }

    public void sortAdvertise(String dataStr, ObjectId shopId){
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNull(shop)){
            throw new ServiceException("没有找到门店");
        }
        JSONArray jsonArray = JSONArray.parseArray(dataStr);
        List<String> advertise = new ArrayList<String>();
        for (int i=0; i < jsonArray.size(); i++){
            JSONObject adv = (JSONObject)jsonArray.get(i);
            advertise.add(adv.getString("img"));
        }
        shop.setAdvertise(advertise);
        shopService.updateDocument(shop);
    }
}
