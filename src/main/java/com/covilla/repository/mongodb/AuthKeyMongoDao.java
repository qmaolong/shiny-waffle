package com.covilla.repository.mongodb;

import com.covilla.common.Constant;
import com.covilla.model.mongo.shop.AuthKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
@Repository
public class AuthKeyMongoDao{
    @Autowired
    private MongoOperations mongoTemplate;

    public List<AuthKey> findAvailableKeys(){
        Query query = new Query(Criteria.where("state").ne(Constant.DATA_STATUS_INVALID));
        return mongoTemplate.find(query, AuthKey.class);
    }

    public void updateState(String id, Integer state){
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("state", state);
        mongoTemplate.updateFirst(query, update, AuthKey.class);
    }

    public void insert(AuthKey authKey){
        mongoTemplate.insert(authKey);
    }

    public AuthKey findById(String id){
        return mongoTemplate.findById(id, AuthKey.class);
    }
}
