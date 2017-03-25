package com.covilla.service.food;

import com.covilla.common.ModifyBlockEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.food.Unit;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.BaseMongoService;
import com.covilla.util.ValidatorUtil;
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
public class UnitService extends BaseMongoService<Shop> {
    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    /**
     * 新增unit
     * @param shopId
     * @param unit
     */
    public void addUnitToShop(ObjectId shopId, Unit unit){
        Integer unitId = generate2thLevelId(shopId, "units");
        unit.setId(unitId);

        Criteria criteria =  Criteria.where("_id").is(shopId);
        Update update = new Update();
        update.push("units", unit);

        shopMongoDao.getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    /**
     * 删除unit
     * @param shopId
     * @param units
     */
    public void deleteUnitFromShop(ObjectId shopId, List<Unit> units){
        Shop shop = shopMongoDao.findBy_id(shopId);
        if (ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getUnits())){
            for (int i=0; i<shop.getUnits().size(); i++){
                for (Unit form : units){
                    if(shop.getUnits().get(i).getName().equals(form.getName())){
                        shop.getUnits().remove(i);
                        break;
                    }
                }
            }
        }
        Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));

        shopMongoDao.getMongoTemplate().updateFirst(query, new Update().set("units", shop.getUnits()), Shop.class);
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    public void updateUnit(ObjectId shopId, Unit unit){
        Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where("units.id").is(unit.getId()));
        Update update = new Update().set("units.$", unit);

        shopMongoDao.getMongoTemplate().updateFirst(query, update, Shop.class);
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

}
