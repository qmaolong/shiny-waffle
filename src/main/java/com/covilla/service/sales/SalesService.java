package com.covilla.service.sales;

import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.CardTypeMongoDao;
import com.covilla.model.mongo.card.CardDiscountRule;
import com.covilla.model.mongo.card.CardType;
import com.covilla.model.mongo.user.User;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.service.card.CardTypeService;
import com.covilla.service.shop.ShopService;
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
 * Created by qmaolong on 2016/9/28.
 */
@Service
@Transactional
public class SalesService extends BaseMongoService<CardType> {
    @Autowired
    private CardTypeService cardTypeService;
    @Autowired
    private CardTypeMongoDao cardTypeMongoDao;
    @Autowired
    private ShopService shopService;
    protected BaseMongoDao<CardType> getBaseMongoDao(){
        return cardTypeMongoDao;
    }

    public List<CardDiscountRule> getDiscountRules(ObjectId shopId){
        return cardTypeService.findNoneNameCardType(shopId).getDiscountRules();
    }

    /**
     * 编辑优惠
     * @param dataStr
     * @param shopId
     * @param user
     * @param oper
     */
    public void editDiscountRules(String dataStr, ObjectId shopId, User user, String oper){
        CardType cardType = cardTypeService.findNoneNameCardType(shopId);
        if(OperationEnum.add.getCode().equals(oper)){
            CardDiscountRule discountRule = SerializationUtil.deSerializeObject(dataStr, CardDiscountRule.class);
            discountRule.setId(generate2thLevelId(cardType.get_id(), "discountRules"));
            Query query = new Query(Criteria.where("_id").is(cardType.get_id()));
            Update update = new Update().push("discountRules", discountRule);
            getMongoTemplate().updateFirst(query, update, CardType.class);

        }else if(OperationEnum.edit.getCode().equals(oper)){
            CardDiscountRule discountRule = SerializationUtil.deSerializeObject(dataStr, CardDiscountRule.class);
            Query query = new Query(Criteria.where("_id").is(cardType.get_id()).and("discountRules.id").is(discountRule.getId()));
            Update update = new Update().set("discountRules.$", discountRule).set("isSupportDiscount", true);
            getMongoTemplate().updateFirst(query, update, CardType.class);
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<CardDiscountRule> discountRules = SerializationUtil.deSerializeList(dataStr, CardDiscountRule.class);
            for (int i=0; i<cardType.getDiscountRules().size(); i++){
                for (CardDiscountRule form : discountRules){
                    if(cardType.getDiscountRules().get(i).getId().equals(form.getId())){
                        cardType.getDiscountRules().remove(i);
                        break;
                    }
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(cardType.get_id()));
            getMongoTemplate().updateFirst(query, new Update().set("discountRules", cardType.getDiscountRules()), CardType.class);
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    /**
     * 重新排序
     * @param shopId
     * @param user
     */
    public void sortDiscountRules(String dataStr, ObjectId shopId, User user){
        CardType cardType = cardTypeService.findNoneNameCardType(shopId);
        List<CardDiscountRule> discountRule = SerializationUtil.deSerializeList(dataStr, CardDiscountRule.class);

        List<CardDiscountRule> olds = getDiscountRules(shopId);
        if(olds.size() != discountRule.size()){
            throw new ServiceException("排序错误，请刷新重试~");
        }

        Query query = new Query(Criteria.where("_id").is(cardType.get_id()));
        Update update = new Update().set("discountRules", discountRule);
        getMongoTemplate().updateFirst(query, update, CardType.class);
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }
}
