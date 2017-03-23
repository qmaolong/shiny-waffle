package com.covilla.repository.mongodb;

import com.covilla.model.mongo.order.TradeDays;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.filter.QueryFilter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by qmaolong on 2016/10/15.
 */
@Repository
public class TradeDaysMongoDao extends BaseMongoDao<TradeDays> {

    public List<TradeDays> findByShopId(QueryFilter filter){
        Criteria criteria = new Criteria();
        if(ValidatorUtil.isNotNull(filter.getShopId())){
            criteria.and("shopId").is(new ObjectId(filter.getShopId()));
        }if (ValidatorUtil.isNull(filter.getShopId())){
            criteria.and("shopId").in(filter.getShopIds());
        }
        if(ValidatorUtil.isNotNull(filter.getStartDate())&&ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.and("day").gte(filter.getStartDate()).lte(filter.getEndDate());
        }else if(ValidatorUtil.isNotNull(filter.getStartDate())){
            criteria.and("day").gte(filter.getStartDate());
        }else if(ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.and("day").lte(filter.getEndDate());
        }
        if(ValidatorUtil.isNotNull(filter.getOperator())){
            criteria.and("userName").is(filter.getOperator());
        }
        Query query = new Query(criteria);
        return getMongoTemplate().find(query, TradeDays.class);
    }
}
