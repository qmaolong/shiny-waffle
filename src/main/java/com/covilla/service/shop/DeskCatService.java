package com.covilla.service.shop;

import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.shop.DeskCat;
import com.covilla.model.mongo.shop.Shop;
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
public class DeskCatService extends BaseMongoService<Shop>{
    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    /**
     * 操作桌型信息
     * @param dataStr
     * @param shopId
     * @param oper
     * @throws Exception
     */
    public void updateDeskCat(String dataStr, ObjectId shopId, String oper) throws Exception{
        if(OperationEnum.add.getCode().equals(oper)){
            DeskCat deskCat = SerializationUtil.deSerializeObject(dataStr, DeskCat.class);
            Integer categoryId = generate2thLevelId(shopId, "deskCat");
            deskCat.setId(categoryId);

            Criteria criteria =  Criteria.where("_id").is(shopId);
            Update update = new Update();
            update.push("deskCat", deskCat);

            shopMongoDao.getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        }else if(OperationEnum.edit.getCode().equals(oper)){
            DeskCat deskCat = SerializationUtil.deSerializeObject(dataStr, DeskCat.class);
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where("deskCat.id").is(deskCat.getId()));
            Update update = new Update().set("deskCat.$", deskCat);
            shopMongoDao.getMongoTemplate().updateFirst(query, update, Shop.class);
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<DeskCat> deskCats = SerializationUtil.deSerializeList(dataStr, DeskCat.class);
            Shop shop = shopMongoDao.findBy_id(shopId);
            for (int i=0; i<shop.getDeskCat().size(); i++){
                for (DeskCat form : deskCats){
                    if(shop.getDeskCat().get(i).getId().equals(form.getId())){
                        shop.getDeskCat().remove(i);
                        break;
                    }
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));

            shopMongoDao.getMongoTemplate().updateMulti(query, new Update().set("deskCat", shop.getDeskCat()), Shop.class);
        }else {
            throw new Exception("操作失败~");
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.desk.getKey());
    }
}
