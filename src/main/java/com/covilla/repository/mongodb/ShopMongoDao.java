package com.covilla.repository.mongodb;

import com.covilla.model.mongo.shop.Shop;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
@Repository
public class ShopMongoDao extends BaseMongoDao<Shop>{
    /**
     * 查询门店类型个数
     * @param owner
     * @param isSuper
     * @return
     */
    public Long countByOwnerAndType(ObjectId owner, Boolean isSuper){
        Query query = new Query(Criteria.where("owner").is(owner).and("superFlag").is(isSuper));
        return getMongoTemplate().count(query, Shop.class);
    }

    public List<ObjectId> findIdsByOwner(ObjectId owner){
        DBObject dbObject = new BasicDBObject();
		dbObject.put("owner", owner);
        DBObject fieldObject = new BasicDBObject();
        fieldObject.put("_id", true);
        BasicQuery basicQuery = new BasicQuery(dbObject, fieldObject);
        List<Shop> shopList = getMongoTemplate().find(basicQuery, clazz);
        List<ObjectId> result = new ArrayList<ObjectId>();
        for (Shop shop : shopList){
            result.add(shop.get_id());
        }
        return result;
    }

}
