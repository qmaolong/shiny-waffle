package com.covilla.service.shop;

import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.Cashier;
import com.covilla.service.BaseMongoService;
import com.covilla.util.SerializationUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/24.
 */
@Service
@Transactional
public class CashierService extends BaseMongoService<Shop> {
    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    /**
     * 操作桌型信息
     * @param shopId
     * @param oper
     * @throws Exception
     */
    public void updateCashier(String dataStr, ObjectId shopId, String oper) throws Exception{
        if(OperationEnum.add.getCode().equals(oper)){
            Cashier cashier = SerializationUtil.deSerializeObject(dataStr, Cashier.class);
            Integer cashiersId = generate2thLevelId(shopId, "cashiers");
            cashier.setId(cashiersId);

            Criteria criteria =  Criteria.where("_id").is(shopId);
            Update update = new Update();
            update.push("cashiers", cashier);
            shopMongoDao.getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        }else if(OperationEnum.edit.getCode().equals(oper)){
            Cashier cashier = SerializationUtil.deSerializeObject(dataStr, Cashier.class);
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where("cashiers.id").is(cashier.getId()));
            Update update = new Update().set("cashiers.$", cashier);
            shopMongoDao.getMongoTemplate().updateFirst(query, update, Shop.class);
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<Cashier> cashiers = SerializationUtil.deSerializeList(dataStr, Cashier.class);
            Shop shop = shopMongoDao.findBy_id(shopId);
            for (int i=0; i<shop.getCashiers().size(); i++){
                for (Cashier form : cashiers){
                    if(shop.getCashiers().get(i).getId().equals(form.getId())){
                        shop.getCashiers().remove(i);
                        break;
                    }
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));
            shopMongoDao.getMongoTemplate().updateFirst(query, new Update().set("cashiers", shop.getCashiers()), Shop.class);
        }else {
            throw new Exception("操作失败~");
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    public void unBindCashier(String dataStr, ObjectId shopId){
        Cashier cashier = SerializationUtil.deSerializeObject(dataStr, Cashier.class);
        Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where("cashiers.id").is(cashier.getId()));
        Update update = new Update().set("cashiers.$.name", "*").set("cashiers.$.mid", "*");
        shopMongoDao.getMongoTemplate().updateFirst(query, update, Shop.class);
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }


}
