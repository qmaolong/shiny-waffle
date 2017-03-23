package com.covilla.service.shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.shop.Desk;
import com.covilla.model.mongo.shop.Room;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.BaseMongoService;
import com.covilla.util.MiscUtils;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/24.
 */
@Service
@Transactional
public class DeskService extends BaseMongoService<Shop> {
    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    /**
     * 操作桌信息
     * @param desk
     * @param shopId
     * @param oper
     * @throws Exception
     */
    public void updateDesk(Desk desk, ObjectId shopId, String oper) throws Exception{
        Shop shop = shopMongoDao.findBy_id(shopId);

        Room room = null;
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getRooms())){
            for (Room temp : shop.getRooms()){
                if(temp.getId().equals(desk.getParentId())){
                    room = temp;
                    break;
                }
            }
            if(OperationEnum.add.getCode().equals(oper)){
                desk.setId(generateDeskId(shop));
                if(ValidatorUtil.isNotNull(room.getDesks())){
                    room.getDesks().add(desk);
                }else {
                    List<Desk> desks = new ArrayList<Desk>();
                    desks.add(desk);
                    room.setDesks(desks);
                }
            }else if(ValidatorUtil.isNotNull(room.getDesks())){
                for(int i=0; i<room.getDesks().size(); i++){
                    Desk desk1 = room.getDesks().get(i);
                    if(desk1.getId().equals(desk.getId())){
                        room.getDesks().set(i, desk);
                        break;
                    }
                }
            }
        }

        /*Update update = new Update().set("desk.$.desk", room.getDesks());
        shopMongoDao.getMongoTemplate().updateFirst(new BasicQuery(dbObject), update, Shop.class);*/
        updateByMap(MiscUtils.toMap("_id", shopId), MiscUtils.toMap("desk", shop.getRooms()));
        updateModifyTime("shop", shopId, ModifyBlockEnum.desk.getKey());

    }

    public void deleteClerks(ObjectId shopId, List<Desk> desks){
        Shop shop = shopMongoDao.findBy_id(shopId);

        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getRooms())){
            OK:
            for (Room room : shop.getRooms()){
                if(ValidatorUtil.isNotNull(room.getDesks())){
                    for(int i=0; i<room.getDesks().size(); i++){
                        for(Desk desk : desks){
                            if(desk.getId().equals(room.getDesks().get(i).getId())){
                                room.getDesks().remove(i);
                                break OK;
                            }
                        }
                    }
                }
            }
        }

        /*Update update = new Update().set("desk.$.desk", room.getDesks());
        shopMongoDao.getMongoTemplate().updateFirst(new BasicQuery(dbObject), update, Shop.class);*/
        updateByMap(MiscUtils.toMap("_id", shopId), MiscUtils.toMap("desk", shop.getRooms()));
        updateModifyTime("shop", shopId, ModifyBlockEnum.desk.getKey());
    }

    public Integer generateDeskId(Shop shop){
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getRooms())){
            List<Desk> desks = new ArrayList<Desk>();
            for (Room room : shop.getRooms()){
                if(ValidatorUtil.isNotNull(room.getDesks())){
                    desks.addAll(room.getDesks());
                }
            }
            return findUntakenId(JSONArray.parseArray(JSON.toJSONString(desks)), null);
        }

        return 1;
    }

}
