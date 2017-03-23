package com.covilla.service.coupon;

import com.covilla.repository.mongodb.coupon.CouponTypeMongoDao;
import com.covilla.model.mongo.card.CardDiscountRule;
import com.covilla.model.mongo.card.CardType;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.card.CardTypeService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2016/11/15.
 */
@Service
public class CouponTypeService extends CardTypeService {
    @Autowired
    private CouponTypeMongoDao couponTypeMongoDao;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CouponService couponService;

    /**
     * 获取所有可用优惠券类型
     * @param shopId
     * @return
     */
    public List<CardType> findAvailableCouponType(ObjectId shopId){
        return couponTypeMongoDao.findAvailableCouponType(shopId);
    }

    public List<CardType> findMainShopCouponType(ObjectId shopId){
        Shop shop = shopService.findBy_id(shopId);
        if(!shop.getSuperFlag()){
            Shop mainShop = shopService.findMainShop(shop.getOwner());
            return findPublicCouponType(mainShop.get_id());
        }else{
            return findPublicCouponType(shopId);
        }
    }

    /**
     * 新增
     * @param cardType
     * @param discountRules
     * @param shopId
     */
    public void addCouponType(CardType cardType, List<CardDiscountRule> discountRules, ObjectId shopId, ObjectId userId){
        Integer id = generateDocumentId("cardType");
        cardType.setId(id);
        cardType.setCreateTime(new Date());
        cardType.setCreator(userId);
        cardType.setDataStatus(0);
        cardType.setDiscountRules(discountRules);
        cardType.setOwner(shopId);
        cardType.setIsCoupon(true);
        if(ValidatorUtil.isNotNull(discountRules)){
            cardType.setIsSupportDiscount(true);
        }

        couponTypeMongoDao.getMongoTemplate().insert(cardType);
    }

    public List<CardType> findPublicCouponType(ObjectId shopId){
        return couponTypeMongoDao.findPublicCouponType(shopId);
    }

    /**
     * 修改
     * @param cardType
     * @param discountRules
     * @param shopId
     */
    public void editCouponType(CardType cardType, List<CardDiscountRule> discountRules, ObjectId shopId, ObjectId userId) throws Exception{
        CardType oldCardType = findById(cardType.getId());
        if(ValidatorUtil.isNull(oldCardType)){
            throw new Exception("当前数据查找失败");
        }
        if(ValidatorUtil.isNotNull(discountRules)){
            cardType.setIsSupportDiscount(true);
        }
        Update update = new Update().set("name", cardType.getName())
                .set("cardDesc", cardType.getCardDesc())
                .set("discountRules", discountRules)
                .set("isPublic", cardType.getIsPublic())
                .set("isSupportDiscount", cardType.getIsSupportDiscount());
        Query query = new Query(Criteria.where("_id").is(oldCardType.get_id()));
        couponTypeMongoDao.getMongoTemplate().updateFirst(query, update, CardType.class);
    }

    /**
     * 删除
     * @param cardTypes
     * @param shopId
     */
    public void deleteCouponType(List<CardType> cardTypes, ObjectId shopId, User user) throws Exception{
        List<Integer> ids = new ArrayList<Integer>();
        for(CardType cardType : cardTypes){
            //判断是否还有正在使用的卡
            Long activateCardCount = couponService.findActivatedCardCountByCardType(new ObjectId(cardType.getObjectIdStr()));
            if(activateCardCount > 0){
                throw new ServiceException(cardType.getName() + "含有" + activateCardCount + "张可使用的券，暂不可删除");
            }
            ids.add(cardType.getId());
        }
        Query query = new Query(Criteria.where("id").in(ids));
        Update update = new Update().set("dataStatus", 1);
        couponTypeMongoDao.getMongoTemplate().updateMulti(query, update, CardType.class);
    }

}
