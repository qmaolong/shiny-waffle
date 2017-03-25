package com.covilla.repository.mongodb;

import com.covilla.model.mongo.order.Order;
import com.covilla.util.DateUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.filter.QueryFilter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by qmaolong on 2016/10/13.
 */
@Repository
public class OrderMongoDao extends BaseMongoDao<Order> {

    /**
     * 获取订单
     * @param shopId
     * @return
     */
    public List<Order> findAllOrders(ObjectId shopId){
        Criteria criteria = new Criteria().where("shopId").is(shopId);
        return getMongoTemplate().find(new Query(criteria), Order.class);
    }

    /**
     * 获取特定类型和状态的订单
     * @param filter
     * @param types
     * @param states
     * @return
     */
    public List<Order> findByFilter(QueryFilter filter, List<Integer> types, List<Integer> states){
        Criteria criteria = new Criteria();
        if(ValidatorUtil.isNotNull(filter.getShopId())){
            criteria.and("shopId").is(new ObjectId(filter.getShopId()));
        }if (ValidatorUtil.isNull(filter.getShopId())){
            criteria.and("shopId").in(filter.getShopIds());
        }
        if(ValidatorUtil.isNotNull(types)){
            criteria.and("orderType").in(types);
        }
        if(ValidatorUtil.isNotNull(states)){
            criteria.and("state").in(states);
        }
        if(ValidatorUtil.isNotNull(filter.getStartDate())&&ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.and("payTime").gte(filter.getStartDate()).lt(DateUtil.addDay(filter.getEndDate(), 1));
        }else if(ValidatorUtil.isNotNull(filter.getStartDate())){
            criteria.and("payTime").gte(filter.getStartDate());
        }else if(ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.and("payTime").lt(DateUtil.addDay(filter.getEndDate(), 1));
        }
        if(ValidatorUtil.isNotNull(filter.getOperator())){
            criteria.and("operatorName").is(filter.getOperator());
        }
        return getMongoTemplate().find(new Query(criteria), Order.class);
    }

}
