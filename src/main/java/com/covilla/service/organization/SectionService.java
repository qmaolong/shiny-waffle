package com.covilla.service.organization;

import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.organization.Section;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.BaseMongoService;
import com.covilla.util.SerializationUtil;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/24.
 */
@Service
@Transactional
public class SectionService extends BaseMongoService<Shop> {
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
    public void updateSection(String dataStr, ObjectId shopId, String oper) throws Exception{
        if(OperationEnum.add.getCode().equals(oper)){
            Section section = SerializationUtil.deSerializeObject(dataStr, Section.class);
            Integer categoryId = generate2thLevelId(shopId, "sections");
            section.setId(categoryId);
            section.setAuthority(new ArrayList<String>());
            if(ValidatorUtil.isNotNull(section.getAuthorityStr())){
                String[] authorities =section.getAuthorityStr().split(",");
                for(String authority : authorities){
                    section.getAuthority().add(authority);
                }
            }

            Criteria criteria =  Criteria.where("_id").is(shopId);
            Update update = new Update();
            update.push("users", section);

            shopMongoDao.getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        }else if(OperationEnum.edit.getCode().equals(oper)){
            Section section = SerializationUtil.deSerializeObject(dataStr, Section.class);
            section.setAuthority(new ArrayList<String>());
            if(ValidatorUtil.isNotNull(section.getAuthorityStr())){
                String[] authorities =section.getAuthorityStr().split(",");
                for(String authority : authorities){
                    section.getAuthority().add(authority);
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where("users.id").is(section.getId()));
            Update update = new Update().set("users.$.name", section.getName()).set("users.$.authority", section.getAuthority()).set("users.$.minDiscount", section.getMinDiscount()).set("users.$.specialDiscount", section.getSpecialDiscount());
            shopMongoDao.getMongoTemplate().updateFirst(query, update, Shop.class);
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<Section> sections = SerializationUtil.deSerializeList(dataStr, Section.class);
            Shop shop = shopMongoDao.findBy_id(shopId);
            for (int i=0; i<shop.getSections().size(); i++){
                for (Section form : sections){
                    if(shop.getSections().get(i).getId().equals(form.getId())){
                        shop.getSections().remove(i);
                        break;
                    }
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));
            shopMongoDao.getMongoTemplate().updateFirst(query, new Update().set("users", shop.getSections()), Shop.class);
        }else {
            throw new Exception("操作失败~");
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.user.getKey());
    }
}
