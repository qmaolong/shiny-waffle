package com.covilla.service.setting;

import com.covilla.common.DataItemEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.food.Category;
import com.covilla.model.mongo.food.Food;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.Children;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.service.food.FoodService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.DESUtil;
import com.covilla.util.SerializationUtil;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qmaolong on 2016/12/9.
 */
@Service
public class DataTransferService extends BaseMongoService<Shop> {
    @Autowired
    private ShopService shopService;
    @Autowired
    private FoodService foodService;
    private static String KEY = "co-villa";

    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    public byte[] getBackupData(ObjectId shopId) throws Exception{
        Shop shop = shopService.findBy_id(shopId);
        List<Food> foods = foodService.findByOwner(shopId);
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCategories())){
            for(Category category : shop.getCategories()){
                for(Children children : category.getChildren()){
                    for (Food food : foods){
                        if(food.getId().indexOf(children.getId())==0){
                            if(ValidatorUtil.isNull(children.getFoods())){
                                children.setFoods(new ArrayList<Food>());
                            }
                            children.getFoods().add(food);
                        }
                    }
                }
            }
        }
        String data = SerializationUtil.serializeObject(shop);
        return DESUtil.encrypt(data.getBytes("UTF-8"), KEY.getBytes("UTF-8"));
    }

    public void importData(byte[] dataShop, ObjectId shopId, String recoverItem) throws Exception{
        String strs = new BASE64Encoder().encode(dataShop);
        String shopStr = DESUtil.decrypt(strs, KEY);
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNull(shop)){
            throw new ServiceException("门店提取失败");
        }
        String[] recoverItems = recoverItem.split(",");

//        JSONObject dataShopObject = MyJsonUtil.deSerializeObject(shopStr);
        Shop backShop = SerializationUtil.deSerializeObject(shopStr, Shop.class);

        Query query = new Query(Criteria.where("_id").is(shopId));
        Update update = new Update().set("dataStatus", 0);
        for (String item : recoverItems){
            DataItemEnum dataItemEnum = DataItemEnum.findByCode(item);
            if(DataItemEnum.food.getCode().equals(item)){//恢复菜品
                importFood(shopStr, shopId);
            }else if(DataItemEnum.category.getCode().equals(item)){//恢复门店数据
                update.set(item, backShop.getCategories());
            }else if(DataItemEnum.controlForms.getCode().equals(item)){
                update.set(item, backShop.getControlForms());
            }else if(DataItemEnum.kitchenForms.getCode().equals(item)){
                update.set(item, backShop.getKitchenForms());
            }else if(DataItemEnum.organize.getCode().equals(item)){
                update.set(item, backShop.getSections());
            }else if(DataItemEnum.printSchemes.getCode().equals(item)){
                update.set(item, backShop.getPrintSchemes());
            }else if(DataItemEnum.taste.getCode().equals(item)){
                update.set(item, backShop.getTastes());
            }else if(DataItemEnum.ticketForms.getCode().equals(item)){
                update.set(item, backShop.getTicketForms());
            }else if(DataItemEnum.unit.getCode().equals(item)){
                update.set(item, backShop.getUnits());
            }
            updateModifyTime("shop", shopId, dataItemEnum.getModifyItem());
        }
        shopService.getMongoTemplate().updateFirst(query, update, Shop.class);
    }

    private void importFood(String dataShop, ObjectId shopId){
        Shop shop = SerializationUtil.deSerializeObject(dataShop, Shop.class);

        //删除旧有数据
        List<Food> oldFood = foodService.findByOwner(shopId);
        if(ValidatorUtil.isNotNull(oldFood)){
            foodService.deleteFood(oldFood, shopId);
        }

        //更新大小类
        /*Query query = new Query(Criteria.where("_id").is(shopId));
        shopService.getMongoTemplate().updateFirst(query, new Update().set("categories", shop.getCategories()), Shop.class);*/

        //新增数据
        List<Food> foodList = new ArrayList<Food>();
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCategories())){
            for (Category category : shop.getCategories()){
                for (Children child : category.getChildren()){
                    if(ValidatorUtil.isNotNull(child.getFoods())){
                        foodList.addAll(child.getFoods());
                    }
                }
            }
        }
        for (Food food: foodList){
            food.setOwner(shopId);
            foodService.getMongoTemplate().insert(food);
        }
    }
}
