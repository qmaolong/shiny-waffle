package com.covilla.repository.mongodb;

import com.covilla.model.mongo.card.CardType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by qmaolong on 2016/11/15.
 */
@Repository
public class CardTypeMongoDao extends BaseMongoDao<CardType> {
    /**
     * 设置所有卡类型为非默认微会员类型
     * @param shopId
     */
    public void unsetWxSupport(ObjectId shopId){
        Query query = new Query(Criteria.where("owner").is(shopId));
        Update update = new Update().set("wxSupport", false);
        getMongoTemplate().updateMulti(query, update, CardType.class);
    }
}
