package com.covilla.repository.mongodb.coupon;

import com.covilla.common.Constant;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.model.mongo.card.CardType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by qmaolong on 2016/11/15.
 */
@Repository
public class CouponTypeMongoDao extends BaseMongoDao<CardType> {

    public List<CardType> findAvailableCouponType(ObjectId shopId){
        Query query = new Query(Criteria.where("owner").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("name").exists(true).and("isCoupon").is(true));
        return getMongoTemplate().find(query, CardType.class);
    }

    public List<CardType> findPublicCouponType(ObjectId shopId){
        Query query = new Query(Criteria.where("owner").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("name").exists(true).and("isCoupon").is(true).and("isPublic").is(true));
        return getMongoTemplate().find(query, CardType.class);
    }
}
