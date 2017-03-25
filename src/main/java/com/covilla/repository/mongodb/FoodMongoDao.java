package com.covilla.repository.mongodb;

import com.alibaba.fastjson.JSONObject;
import com.covilla.model.mongo.food.Food;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
@Repository
public class FoodMongoDao extends BaseMongoDao<Food>{
    public void batchSetPrinters(ObjectId shopId, List<String> ids, JSONObject object){
        Query query = new Query(Criteria.where("owner").is(shopId).and("id").in(ids));
        Update update = new Update().set("dataStatus", 0);
        if(!"continue".equals(object.getString("printer1"))){
            update.set("printer1", object.getString("printer1"));
        }
        if(!"continue".equals(object.getString("printer2"))){
            update.set("printer2", object.getString("printer2"));
        }
        if(!"continue".equals(object.getString("printer3"))){
            update.set("printer3", object.getString("printer3"));
        }
        if(!"continue".equals(object.getString("printerControl"))){
            update.set("printerControl", object.getString("printerControl"));
        }
        if(!"continue".equals(object.getString("printerPolicy"))){
            update.set("printerPolicy", object.getString("printerPolicy"));
        }
        if("true".equals(object.getString("setProperty"))){
            update.set("property", ValidatorUtil.isNotNull(object.getString("property"))?object.getString("property").split(","):null);
        }
        if(!"continue".equals(object.getString("foodState"))){
            update.set("state", object.getString("foodState"));
        }
        getMongoTemplate().updateMulti(query, update, Food.class);
    }
}
