package com.covilla.service.shop;

import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.shop.Room;
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
public class RoomService extends BaseMongoService<Shop> {
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
    public void updateRoom(String dataStr, ObjectId shopId, String oper) throws Exception{
        if(OperationEnum.add.getCode().equals(oper)){
            Room room = SerializationUtil.deSerializeObject(dataStr, Room.class);
            Integer categoryId = generate2thLevelId(shopId, "rooms");
            room.setId(categoryId);

            Criteria criteria =  Criteria.where("_id").is(shopId);
            Update update = new Update();
            update.push("desk", room);

            shopMongoDao.getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        }else if(OperationEnum.edit.getCode().equals(oper)){
            Room room = SerializationUtil.deSerializeObject(dataStr, Room.class);
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where("desk.id").is(room.getId()));
            Update update = new Update().set("desk.$", room);
            shopMongoDao.getMongoTemplate().updateFirst(query, update, Shop.class);
        }else if(OperationEnum.delete.getCode().equals(oper)){
            Shop shop = shopMongoDao.findBy_id(shopId);
            List<Room> rooms = SerializationUtil.deSerializeList(dataStr, Room.class);
            for (int i=0; i<shop.getRooms().size(); i++){
                for (Room form : rooms){
                    if(shop.getRooms().get(i).getId().equals(form.getId())){
                        shop.getRooms().remove(i);
                        break;
                    }
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));

            shopMongoDao.getMongoTemplate().updateFirst(query, new Update().set("desk", shop.getRooms()), Shop.class);
        }else {
            throw new Exception("操作失败~");
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.desk.getKey());
    }
}
