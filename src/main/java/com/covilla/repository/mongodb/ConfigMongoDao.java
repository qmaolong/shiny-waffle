package com.covilla.repository.mongodb;

import com.covilla.model.mongo.config.ConfigModule;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.TicketFormConfigVo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by qmaolong on 2016/10/13.
 */
@Repository
public class ConfigMongoDao extends BaseMongoDao<ConfigModule> {

    public TicketFormConfigVo getTicketFormConfig(){
        Query query = new Query(Criteria.where("kitchenForm").exists(true));
        List<TicketFormConfigVo> list = getMongoTemplate().find(query, TicketFormConfigVo.class, "config");
        if(ValidatorUtil.isNotNull(list)){
            return list.get(0);
        }
        return null;
    }
}
