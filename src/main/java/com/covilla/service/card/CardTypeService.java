package com.covilla.service.card;

import com.covilla.common.Constant;
import com.covilla.common.RoleEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.CardTypeMongoDao;
import com.covilla.model.mongo.card.CardChargeRule;
import com.covilla.model.mongo.card.CardDiscountRule;
import com.covilla.model.mongo.card.CardType;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2016/9/13.
 */
@Service
@Transactional
public class CardTypeService extends BaseMongoService<CardType>{
    @Autowired
    private CardTypeMongoDao cardTypeMongoDao;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CardService cardService;

    protected BaseMongoDao<CardType> getBaseMongoDao(){
        return cardTypeMongoDao;
    }

    /**
     * 查找门店可用的会员方案
     * @param shopId
     * @return
     */
    public List<CardType> findAvailableCardType(ObjectId shopId){
        Query query = new Query(Criteria.where("owner").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("name").exists(true).and("isCoupon").ne(true));
        return getMongoTemplate().find(query, CardType.class);
    }

    /**
     * 获取总店公开给所有店使用的cardType
     * @param shopId
     * @return
     */
    public List<CardType> findPublicCardTypes(ObjectId shopId){
        Query query = new Query(Criteria.where("owner").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("name").exists(true).and("isCoupon").ne(true).and("isPublic").is(true));
        return getMongoTemplate().find(query, CardType.class);
    }

    /**
     * 获取公用的会员方案
     * @param shopId
     * @param user
     * @return
     */
    public List<CardType> findMainShopCardType(ObjectId shopId, User user){
        Shop shop = shopService.findBy_id(shopId);
        if(!shop.getSuperFlag()){
            Shop mainShop = shopService.findMainShop(shop.getOwner());
            return findPublicCardTypes(mainShop.get_id());
        }else{
            return findPublicCardTypes(shopId);
        }
    }

    /**
     * 查找非会员的折扣方案（name为空）
     * @param shopId
     * @return
     */
    public CardType findNoneNameCardType(ObjectId shopId){
        Query query = new Query(Criteria.where("owner").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("name").exists(false));
        List<CardType> cardTypes = getMongoTemplate().find(query, CardType.class);
        if(ValidatorUtil.isNull(cardTypes) || cardTypes.size() == 0){
            CardType cardType = new CardType();
            cardType.setDiscountRules(new ArrayList<CardDiscountRule>());
            cardType.setOwner(shopId);
            cardType.setIsSupportDiscount(true);
            getMongoTemplate().insert(cardType);
            return cardType;
        }
        return cardTypes.get(0);
    }

    /**
     * 新增
     * @param cardType
     * @param discountRules
     * @param chargeRules
     * @param shopId
     */
    public void addCardType(CardType cardType, List<CardDiscountRule> discountRules, List<CardChargeRule> chargeRules, ObjectId shopId, ObjectId userId){
        Integer id = generateDocumentId("cardType");
        cardType.setId(id);
        cardType.setCreateTime(new Date());
        cardType.setCreator(userId);
        cardType.setDataStatus(0);
        cardType.setDiscountRules(discountRules);
        cardType.setChargeRules(chargeRules);
        cardType.setOwner(shopId);

        getMongoTemplate().insert(cardType);
    }

    /**
     * 修改
     * @param cardType
     * @param discountRules
     * @param chargeRules
     * @param shopId
     */
    public void editCardType(CardType cardType, List<CardDiscountRule> discountRules, List<CardChargeRule> chargeRules, ObjectId shopId, ObjectId userId) throws Exception{
        CardType oldCardType = findById(cardType.getId());
        if(ValidatorUtil.isNull(oldCardType)){
            throw new Exception("当前数据查找失败");
        }
        oldCardType.setName(cardType.getName());
        oldCardType.setCardDesc(cardType.getCardDesc());
        oldCardType.setIsSupportDiscount(cardType.getIsSupportDiscount());
        oldCardType.setIsSupportCharge(cardType.getIsSupportCharge());
        oldCardType.setIsSupportPoint(cardType.getIsSupportPoint());
        oldCardType.setIsPublic(cardType.getIsPublic());
        oldCardType.setChargeRules(chargeRules);
        oldCardType.setDiscountRules(discountRules);
        oldCardType.setWxSupport(cardType.getWxSupport());
        oldCardType.setCoverImg(cardType.getCoverImg());
        if (ValidatorUtil.isNotNull(cardType.getWxSupport())&&cardType.getWxSupport()){
            cardTypeMongoDao.unsetWxSupport(shopId);
        }
        updateDocument(oldCardType);
    }

    /**
     * 删除
     * @param cardTypes
     * @param shopId
     */
    public void deleteCardType(List<CardType> cardTypes, ObjectId shopId, User user) throws Exception{
        List<Integer> ids = new ArrayList<Integer>();
        for(CardType cardType : cardTypes){
            //判断是否还有正在使用的卡
            Long activateCardCount = cardService.findActivatedCardCountByCardType(new ObjectId(cardType.getObjectIdStr()));
            if(activateCardCount > 0){
                throw new ServiceException(cardType.getName() + "含有" + activateCardCount + "张可使用的卡，暂不可删除");
            }
            ids.add(cardType.getId());
        }
        if(RoleEnum.manager.getCode().equals(user.getRole())){
            for(CardType cardType : cardTypes){
                if(!cardType.getCreator().toString().equals(user.get_id().toString())){
                    throw new ServiceException("【" + cardType.getName() + "】非当前管理员所创建，删除失败！");
                }
            }
        }

        Query query = new Query(Criteria.where("id").in(ids));
        Update update = new Update().set("dataStatus", 1);
        getMongoTemplate().updateMulti(query, update, CardType.class);
    }
}
