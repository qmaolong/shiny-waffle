package com.covilla.service.coupon;

import com.covilla.common.CardStateEnum;
import com.covilla.common.CardTypeEnum;
import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.coupon.CouponMongoDao;
import com.covilla.model.mongo.card.Card;
import com.covilla.model.mongo.card.CardBatch;
import com.covilla.model.mongo.card.CardPublish;
import com.covilla.model.mongo.card.CardType;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.ServiceException;
import com.covilla.service.card.CardBatchService;
import com.covilla.service.card.CardService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.*;
import com.covilla.vo.card.CardImportVo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by qmaolong on 2016/11/15.
 */
@Service
public class CouponService extends CardService {
    @Autowired
    private CouponMongoDao couponMongoDao;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CouponTypeService couponTypeService;
    @Autowired
    private CardBatchService cardBatchService;

    public List<Card> findCoupons(ObjectId shopId){
        return couponMongoDao.findCoupons(shopId);
    }

    public List<Card> findByBatchId(ObjectId shopId, String batchId){
        return couponMongoDao.findByBatchId(shopId, batchId);
    }

    /*public List<CardBatch> findCouponIndex(ObjectId shopId){
        CommandResult result = couponMongoDao.getMongoTemplate().executeCommand("{distinct:'card', key:'batchId'}");
        BasicDBList list = (BasicDBList) result.get("values");
        List cardBatches = new ArrayList();
        for (Object object: list){
            CardBatch batch = new CardBatch();
            batch.setId(Convert.toString(object));
            cardBatches.add(batch);
        }
        return cardBatches;
    }*/

    public Card findCouponBy_id(ObjectId _id){
        return couponMongoDao.findBy_id(_id);
    }

    /**
     * 新建券
     * @param coupon
     */
    public void addCoupon(Card coupon, ObjectId shopId, User user){
        Card coupon1 = findByTagIdAndShop(coupon.getTagId(), shopId);
        if(ValidatorUtil.isNotNull(coupon1)){
            throw new ServiceException("券编号已存在");
        }

        CardType couponType = couponTypeService.findBy_id(new ObjectId(coupon.getCardTypeIdStr()));
        if(ValidatorUtil.isNull(couponType)){
            throw new ServiceException("不存在的券类型");
        }else {
            coupon.setName(couponType.getName());
        }

        /*if(RoleEnum.manager.getCode().equals(user.getRole()) && ValidatorUtil.isNotNull(coupon.getIsPublic())&&coupon.getIsPublic()){
            Shop shop = shopService.findBy_id(shopId);
            coupon.setShopId(shop.getSuperShop());
        }else {
            coupon.setShopId(shopId);
        }*/
        coupon.setShopId(couponType.getOwner());//设置所属门店
        coupon.setCreateShopId(shopId);//设置创建门店

        coupon.setBalance(couponType.getInitBalance());
        coupon.setMediumKey(coupon.getTagId());
        coupon.setMedium(CardTypeEnum.coupon.getCode());
        coupon.setCardTypeId(new ObjectId(coupon.getCardTypeIdStr()));
        coupon.setCreateTime(new Date());
        couponMongoDao.getMongoTemplate().insert(coupon);
    }

    /**
     * 编辑券
     * @param coupon
     */
    public void editCoupon(Card coupon){
        Query query = new Query(Criteria.where("_id").is(coupon.get_id()));
        Update update = new Update().set("cardType", new ObjectId(coupon.getCardTypeIdStr())).set("startTime", coupon.getStartTime()).set("endTime", coupon.getEndTime());
        if(ValidatorUtil.isNotNull(coupon.getCardState())){
            update.set("cardState", coupon.getCardState());
        }
        couponMongoDao.getMongoTemplate().updateFirst(query, update, Card.class);
    }

    /**
     * 作废券
     * @param tagId
     * @param shopId
     */
    public void disableCoupon(String tagId, ObjectId shopId){
        Query query = new Query(Criteria.where("tagId").is(tagId).and("shopId").is(shopId).and("medium").is(CardTypeEnum.coupon.getCode()));

        Card coupon = findByTagIdAndShop(tagId, shopId);
        if(ValidatorUtil.isNull(coupon)){
            throw new ServiceException("不存在该券~");
        }else if(coupon.getCardState() == CardStateEnum.disabled.getCode()){
            throw new ServiceException("不可重复作废~");
        }

        Update update = new Update().set("cardState", CardStateEnum.disabled.getCode()).set("disabledTime", new Date());
        getMongoTemplate().updateFirst(query, update, Card.class);
    }

    public List<CardImportVo> previewCoupons(MultipartFile file, Boolean isPublic, String cardTypeIdStr, String startTime, String endTime, Integer cardState, ObjectId shopId) throws Exception{
        CardType cardType = couponTypeService.findBy_id(new ObjectId(cardTypeIdStr));
        if(ValidatorUtil.isNull(cardType)){
            throw new ServiceException("券类型提取失败~");
        }

        String fieldsName = "tagId";
        List<Map<String, String>> fileData = ExcelUtil.analysisImportFile(file, fieldsName);

        List<CardImportVo> result = new ArrayList<CardImportVo>();
        for (Map<String, String> data : fileData){
            String tagId = data.get("tagId");
            if(ValidatorUtil.isNull(tagId)){
                continue;
            }
            CardImportVo coupon = new CardImportVo();

            Card existMediumKeys = findByTagIdAndShop(tagId, shopId);
            if(ValidatorUtil.isNotNull(existMediumKeys)){
                coupon.setNoPassReason(tagId + "已存在");
                coupon.setPass(false);
            }
            coupon.setName(cardType.getName());
            coupon.setBalance(cardType.getInitBalance());
            coupon.setCardTypeIdStr(cardTypeIdStr);
            coupon.setMediumKey(tagId);
            coupon.setMedium(CardTypeEnum.coupon.getCode());
            coupon.setTagId(tagId);
            coupon.setIsPublic(isPublic);
            coupon.setStartTime(DateUtil.formateStrToDate(startTime, "yyyy-MM-dd"));
            coupon.setEndTime(DateUtil.formateStrToDate(endTime, "yyyy-MM-dd"));
            coupon.setCreateTime(new Date());
            coupon.setCardState(cardState);
//            card.setCreator(operator);
            result.add(coupon);
        }

        return result;
    }

    /**
     * 导入券信息
     * @param shopId
     */
    public Integer importCoupons(String dataStr, ObjectId shopId, String operator){
        List<Card> cardList = SerializationUtil.deSerializeList(dataStr, Card.class);

        String batchId = cardBatchService.generateBatchIdByDate();

        CardType couponType = couponTypeService.findBy_id(new ObjectId(cardList.get(0).getCardTypeIdStr()));

        int importCount = 0;
        for (Card coupon : cardList){
            Card existMediumKeys = findByTagIdAndShop(coupon.getTagId(), shopId);
            if(ValidatorUtil.isNotNull(existMediumKeys)){
                continue;
            }
            coupon.setShopId(couponType.getOwner());
            coupon.setCreateShopId(shopId);
            coupon.setCreator(operator);
            coupon.setCardTypeId(new ObjectId(coupon.getCardTypeIdStr()));
            coupon.setCardState(CardStateEnum.newCard.getCode());
            coupon.setBatchId(batchId);

            importCount++;
            getMongoTemplate().insert(coupon);
        }

        if(importCount > 0){
            //保存生成记录
            CardBatch cardBatch = new CardBatch();
            cardBatch.setId(batchId);
            cardBatch.setMedium(CardTypeEnum.coupon.getCode());
            cardBatch.setCount(importCount);
            cardBatch.setCardTypeName(cardList.get(0).getName());
            cardBatch.setCreateTime(new Date());
            cardBatch.setIsPublic(cardList.get(0).getIsPublic());
            cardBatchService.saveCardBatch(cardBatch, shopId);
        }
        return importCount;
    }

    public void couponUseAble(ObjectId couponId){
        couponMongoDao.updateState(couponId, CardStateEnum.used.getCode());
    }

    /**
     * 根据卡号和门店获取
     * @param tagId
     * @param shopId
     * @return
     */
    public Card findByTagIdAndShop(String tagId, ObjectId shopId){
        Query query = new Query(Criteria.where("tagId").is(tagId).and("shopId").is(shopId).and("medium").is(CardTypeEnum.coupon.getCode()));
        List<Card> cardList = getMongoTemplate().find(query, Card.class);
        if(cardList.size()<=0){
            return null;
        }
        Card coupon = cardList.get(0);
        if(ValidatorUtil.isNotNull(coupon.getCardTypeId())){
            CardType cardType = couponTypeService.findBy_id(coupon.getCardTypeId());
            coupon.setCardTypeObject(cardType);
        }
        return coupon;
    }

    /**
     * 编辑发放规则
     * @param dataStr
     * @param oper
     * @param shopId
     */
    public void editCouponPublishRule(String dataStr, String oper, ObjectId shopId){
        if(ValidatorUtil.isNull(shopId)){
            throw new ServiceException("登录失效！");
        }

        Shop shop = shopService.findBy_id(shopId);
        List<CardPublish> cardPublishes = shop.getTicketPublish();
        if(ValidatorUtil.isNull(cardPublishes)){
            cardPublishes = new ArrayList<CardPublish>();
        }

        if(OperationEnum.add.getCode().equals(oper)){
            CardPublish cardPublish = SerializationUtil.deSerializeObject(dataStr, CardPublish.class);
            cardPublish.setId(shopService.generate2thLevelId(shopId, "ticketPublish"));
//            couponPublish.setTicket(new ObjectId(couponPublish.getCardTypeIdStr()));
            cardPublishes.add(cardPublish);
        }else if(OperationEnum.edit.getCode().equals(oper)){
            CardPublish cardPublish = SerializationUtil.deSerializeObject(dataStr, CardPublish.class);
            if(ValidatorUtil.isNull(cardPublish.getId())){
                cardPublish.setId(shopService.generate2thLevelId(shopId, "ticketPublish"));
//                couponPublish.setTicket(new ObjectId(couponPublish.getCardTypeIdStr()));
                for (int i = 0; i< cardPublishes.size(); i++){
                    if (cardPublish.getName().equals(cardPublishes.get(i).getName())){
                        cardPublishes.set(i, cardPublish);
                        break;
                    }
                }
            }else {
                for (int i = 0; i< cardPublishes.size(); i++){
                    if (cardPublish.getId().equals(cardPublishes.get(i).getId())){
//                        couponPublish.setTicket(new ObjectId(couponPublish.getCardTypeIdStr()));
                        cardPublishes.set(i, cardPublish);
                        break;
                    }
                }
            }
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<CardPublish> deleteItems = SerializationUtil.deSerializeList(dataStr, CardPublish.class);
            for (int i = 0; i< cardPublishes.size(); i++){
                for (CardPublish deleteItem : deleteItems){
                    if (deleteItem.getId().equals(cardPublishes.get(i).getId())){
                        cardPublishes.remove(i);
                        break;
                    }
                }
            }
        }else if(OperationEnum.sort.getCode().equals(oper)){
            cardPublishes = SerializationUtil.deSerializeList(dataStr, CardPublish.class);
        }else {
            throw new ServiceException("未知操作类型");
        }
        shopService.updateByMap(MiscUtils.toMap("_id", shopId), MiscUtils.toMap("ticketPublish", cardPublishes));
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    public void activateCoupon(String tagId, ObjectId shopId){
        Card coupon = findByTagIdAndShop(tagId, shopId);
        if(!CardStateEnum.newCard.getCode().equals(coupon.getCardState())){
            throw new ServiceException("当前状态不可激活~");
        }
        updateByMap(MiscUtils.toMap("_id", coupon.get_id()), MiscUtils.toMap("cardState", CardStateEnum.activated.getCode()));
    }
}
