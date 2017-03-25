package com.covilla.service.order;

import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.TradeDaysMongoDao;
import com.covilla.model.mongo.order.TradeDays;
import com.covilla.service.BaseMongoService;
import com.covilla.vo.filter.QueryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by qmaolong on 2016/10/15.
 */
@Service
@Transactional
public class TradeDaysService extends BaseMongoService<TradeDays>{
    @Autowired
    private TradeDaysMongoDao tradeDao;
    protected BaseMongoDao<TradeDays> getBaseMongoDao(){
        return tradeDao;
    }

    public List<TradeDays> findByShopId(QueryFilter filter){
        return tradeDao.findByShopId(filter);
    }
}
