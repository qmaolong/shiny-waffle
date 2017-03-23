package com.covilla.repository.mongodb.coupon;

import com.covilla.common.CardTypeEnum;
import com.covilla.common.Constant;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.model.mongo.card.Card;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by qmaolong on 2016/11/15.
 */
@Repository
public class CouponMongoDao extends BaseMongoDao<Card> {
    /**
     * 获取所有券
     * @param shopId
     * @return
     */
    public List<Card> findCoupons(ObjectId shopId){
        Query query = new Query(Criteria.where("shopId").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("medium").is(CardTypeEnum.coupon.getCode()));
        return getMongoTemplate().find(query, Card.class);
    }

    public List<Card> findByBatchId(ObjectId shopId, String batchId){
        Query query = new Query(Criteria.where("shopId").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("medium").is(CardTypeEnum.coupon.getCode()).and("batchId").is(batchId));
        return getMongoTemplate().find(query, Card.class);
    }

    public void updateState(ObjectId couponId, Integer state){
        Query query = new Query(Criteria.where("_id").is(couponId));
        Update update = new Update().set("cardState", state);
        getMongoTemplate().updateFirst(query, update, Card.class);
    }
}
