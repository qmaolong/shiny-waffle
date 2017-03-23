package com.covilla.service.food;

import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.food.Taste;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.BaseMongoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by qmaolong on 2016/8/30.
 */
@Service
@Transactional
public class TastService extends BaseMongoService<Shop> {
    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }
    /**
     * 操作桌型信息
     * @param taste
     * @param shopId
     * @param oper
     * @throws Exception
     */
    public void updateTaste(Taste taste, ObjectId shopId, String oper) throws Exception{
        if(OperationEnum.add.getCode().equals(oper)){
            Integer categoryId = generate2thLevelId(shopId, "taste");
            taste.setId(categoryId);

            Criteria criteria =  Criteria.where("_id").is(shopId);
            Update update = new Update();
            update.push("taste", taste.getName());

            shopMongoDao.getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        }else {
            throw new Exception("操作失败~");
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    public void delete(List<Taste> tastes, ObjectId shopId){
        Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));
        Update update = new Update();
        for (Taste taste : tastes){
            update.pull("taste", taste.getName());
        }
        shopMongoDao.getMongoTemplate().updateFirst(query, update, Shop.class);
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

}
