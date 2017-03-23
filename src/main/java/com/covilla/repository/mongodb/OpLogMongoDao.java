package com.covilla.repository.mongodb;

import com.covilla.model.mongo.log.OpLog;
import com.covilla.util.DateUtil;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2017/2/12.
 */
@Repository
public class OpLogMongoDao extends BaseMongoDao<OpLog> {

    public void insertOpLog(OpLog opLog){
        insert(opLog);
    }

    /**
     * 获取三个月内操作记录
     * @param shopId
     * @return
     */
    public List<OpLog> getOpLogs(ObjectId shopId){
        Date startDate = DateUtil.addMonth(new Date(), -3);
        Query query = new Query(Criteria.where("shopId").is(shopId).and("time").gte(startDate));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"time")));
        return getMongoTemplate().find(query, OpLog.class);
    }
}
