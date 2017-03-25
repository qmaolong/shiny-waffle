package com.covilla.service.organization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.organization.Clerk;
import com.covilla.model.mongo.organization.Section;
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
public class ClerkService extends BaseMongoService<Shop> {
    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    public void updateClerk(Clerk clerk, ObjectId shopId, String oper) throws Exception{
        Shop shop = shopMongoDao.findBy_id(shopId);

        clerk.setAuthority(new ArrayList<String>());
        if(ValidatorUtil.isNotNull(clerk.getAuthorityStr())){
            String[] authorities =clerk.getAuthorityStr().split(",");
            for(String authority : authorities){
                clerk.getAuthority().add(authority);
            }
        }

        Section section = null;
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getSections())){
            for (Section temp : shop.getSections()){
                if(temp.getId().equals(clerk.getParentId())){
                    section = temp;
                    break;
                }
            }
            if(OperationEnum.add.getCode().equals(oper)){
                clerk.setId(generateClerkId(shop));
                if(ValidatorUtil.isNotNull(section.getClerks())){
                    section.getClerks().add(clerk);
                }else {
                    List<Clerk> clerkList = new ArrayList<Clerk>();
                    clerkList.add(clerk);
                    section.setClerks(clerkList);
                }
            }else if(ValidatorUtil.isNotNull(section.getClerks())){
                for(int i=0; i<section.getClerks().size(); i++){
                    Clerk clerk1 = section.getClerks().get(i);
                    if(clerk1.getId().equals(clerk.getId())){
                        section.getClerks().set(i, clerk);
                        break;
                    }
                }
            }
        }

        updateByMap(MiscUtils.toMap("_id", shopId), MiscUtils.toMap("users", shop.getSections()));

        /*Update update = new Update().set("users.$.members", section.getClerks());
        shopMongoDao.getMongoTemplate().updateFirst(new BasicQuery(dbObject), update, Shop.class);*/
        updateModifyTime("shop", shopId, ModifyBlockEnum.user.getKey());
    }

    public void deleteClerks(ObjectId shopId, List<Clerk> clerks){
        Shop shop = shopMongoDao.findBy_id(shopId);

        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getSections())){
            OK:
            for(Section temp : shop.getSections()){
                if(ValidatorUtil.isNotNull(temp.getClerks())){
                    for(Clerk clerk : temp.getClerks()){
                        for (Clerk deleteItem : clerks){
                            if(clerk.getId().equals(deleteItem.getId())){
                                temp.getClerks().remove(clerk);
                                break OK;
                            }
                        }
                    }
                }
            }
        }

        /*Update update = new Update().set("users.$.members", section.getClerks());
        shopMongoDao.getMongoTemplate().updateFirst(new BasicQuery(dbObject), update, Shop.class);*/
        updateByMap(MiscUtils.toMap("_id", shopId), MiscUtils.toMap("users", shop.getSections()));
        updateModifyTime("shop", shopId, ModifyBlockEnum.user.getKey());
    }

    /**
     * 生成职员id
     * @return
     */
    public Integer generateClerkId(Shop shop){
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getSections())){
            List<Clerk> clerks = new ArrayList<Clerk>();
            for (Section section : shop.getSections()){
                if(ValidatorUtil.isNotNull(section.getClerks())){
                    clerks.addAll(section.getClerks());
                }
            }
            return findUntakenId(JSONArray.parseArray(JSON.toJSONString(clerks)), null);
        }

        return 1;
    }
}
