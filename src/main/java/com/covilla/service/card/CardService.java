package com.covilla.service.card;

import com.covilla.common.*;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.CardMongoDao;
import com.covilla.model.CardFlow;
import com.covilla.model.mongo.card.*;
import com.covilla.model.mongo.order.Order;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.plugin.rabbitMQ.OrderMsgProducer;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.service.order.OrderService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.*;
import com.covilla.vo.api.ChargeApiMsg;
import com.covilla.vo.card.CardFilterVo;
import com.covilla.vo.card.CardImportVo;
import com.covilla.vo.card.CouponSendVo;
import com.covilla.weixin.OrderMessage;
import jodd.typeconverter.Convert;
import org.apache.commons.collections.map.HashedMap;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qmaolong on 2016/9/13.
 */
@Service
@Transactional
public class CardService extends BaseMongoService<Card>{
    @Autowired
    private ShopService shopService;
    @Autowired
    private CardTypeService cardTypeService;
    @Autowired
    private CardBatchService cardBatchService;
    @Autowired
    private CardFlowService cardFlowService;
    @Autowired
    private CardMongoDao cardMongoDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMsgProducer orderMsgProducer;

    private AtomicInteger atomicInteger = new AtomicInteger(1);

    private final static Map<String, Integer> retainCardTag;//卡生成保留的前缀
    static {
        retainCardTag = new HashedMap();
        retainCardTag.put("13", 20);
        retainCardTag.put("28", 20);
        retainCardTag.put("17", 13);
        retainCardTag.put("18", 18);
        retainCardTag.put("19", 13);
    }
    protected BaseMongoDao<Card> getBaseMongoDao(){
        return cardMongoDao;
    }

    /**
     * 查找门店的会员卡
     * @param shopId
     * @return
     */
    public List<Card> findCards(CardFilterVo filterVo, ObjectId shopId, boolean isCoupon){
        return cardMongoDao.findByFilter(filterVo, shopId, isCoupon);
    }

    public List<Card> findWeixinMember(ObjectId shopId){
        Query query = new Query(Criteria.where("shopId").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("medium").is(CardTypeEnum.virtual.getCode()));
        return getMongoTemplate().find(query, Card.class);
    }

    /**
     * 新建卡
     * @param card
     */
    public void addCard(Card card, ObjectId shopId, User user){
        CardType cardType = cardTypeService.findBy_id(new ObjectId(card.getCardTypeIdStr()));
        if(ValidatorUtil.isNull(cardType)){
            throw new ServiceException("卡类型提取失败~");
        }
        card.setShopId(cardType.getOwner());
        card.setCreateShopId(shopId);

        if(card.getCardState()==1){
            card.setActivateTime(new Date());
        }
        card.setBalance(BigDecimal.ZERO);
        card.setCardTypeId(new ObjectId(card.getCardTypeIdStr()));
        getMongoTemplate().insert(card);
    }

    /**
     * 编辑卡
     * @param card
     */
    public void editCard(Card card){
        Query query = new Query(Criteria.where("_id").is(card.get_id()));
        Update update = new Update().set("memberName", card.getMemberName())
                .set("tel", card.getTel()).set("birthDay", card.getBirthDay())
                .set("cardType", new ObjectId(card.getCardTypeIdStr()))
                .set("startTime", card.getStartTime()).set("endTime", card.getEndTime())
                .set("tagId", card.getTagId()).set("mediumKey", card.getMediumKey());
        CardType cardType = cardTypeService.findBy_id(new ObjectId(card.getCardTypeIdStr()));
        update.set("name", cardType.getName());
        if(ValidatorUtil.isNotNull(card.getBalance())){
            update.set("balance", card.getBalance());
        }
        if (ValidatorUtil.isNotNull(card.getPoint())){
            update.set("point", card.getPoint());
        }
        if(ValidatorUtil.isNotNull(card.getCardState())){
            update.set("cardState", card.getCardState());
        }
        if (ValidatorUtil.isNotNull(card.getOrderId())){
            update.set("orderId", card.getOrderId());
        }
        if (ValidatorUtil.isNotNull(card.getUsedTime())){
            update.set("usedTime", card.getUsedTime());
        }
        getMongoTemplate().updateFirst(query, update, Card.class);
    }

    /**
     * 激活卡
     * @param tagId
     * @param shopId
     */
    public void activateCard(String tagId, ObjectId shopId){
        Query query = new Query(Criteria.where("tagId").is(tagId).and("shopId").is(shopId).and("medium").ne(CardTypeEnum.coupon.getCode()));

        Card card = findByTagIdAndShop(tagId, shopId);
        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("不存在该卡号~");
        }else if(card.getCardState() > CardStateEnum.newCard.getCode()){
            throw new ServiceException("不可重复激活~");
        }

        Update update = new Update().set("cardState", CardStateEnum.activated.getCode()).set("activateTime", new Date());
        getMongoTemplate().updateFirst(query, update, Card.class);
    }

    /**
     * 注销卡
     */
    public void deleteCard(ObjectId cardId){
       cardMongoDao.deleteCard(cardId);
    }

    /**
     * 冻结或解冻卡
     */
    public void frozenOrUnfrozenCard(String tagId, ObjectId shopId){
        Query query = new Query(Criteria.where("tagId").is(tagId).and("shopId").is(shopId));

        Card card = findByTagIdAndShop(tagId, shopId);
        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("不存在该卡号~");
        }
        Update update = new Update().set("cardState", CardStateEnum.frozen.getCode().equals(card.getCardState())?CardStateEnum.activated.getCode():CardStateEnum.frozen.getCode()).set("frozenTime", new Date());
        getMongoTemplate().updateFirst(query, update, Card.class);
    }

    public ChargeApiMsg cardCharge(String orderNo, String cardId, String shopId, BigDecimal chargeAmount, String operator, String remark, Integer chargeType){
        Card  card = findByCardId(cardId);
        if(ValidatorUtil.isNull(card)){
            throw new ServiceException("卡信息提取失败~");
        }
        if(!CardStateEnum.activated.getCode().equals(card.getCardState())){
            throw new ServiceException("卡" + CardStateEnum.getNameByCode(card.getCardState()) + ",不可充值~");
        }
        CardType cardType = card.getCardTypeObject();
        if(ValidatorUtil.isNull(cardType)){
            throw new ServiceException("卡类型提取失败~");
        }
        if(!cardType.getIsSupportCharge()){
            throw new ServiceException("该卡不支持储值业务~");
        }

        //计算赠送金额
        BigDecimal giftAmount = BigDecimal.ZERO;
        if(ValidatorUtil.isNotNull(cardType)){
            List<CardChargeRule> chargeRules = cardType.getChargeRules();
            if(ValidatorUtil.isNotNull(chargeRules)){
                /*Double maxfit = 0.0;
                for(CardChargeRule cardChargeRule : chargeRules){
                    if(chargeAmount >= cardChargeRule.getMinLine() && cardChargeRule.getMinLine() > maxfit){
                        maxfit = cardChargeRule.getMinLine();
                        giftAmount = cardChargeRule.getGiftAmount();
                    }
                }*/
                for(CardChargeRule cardChargeRule : chargeRules){
                    if(chargeAmount.compareTo(cardChargeRule.getMinLine()) >= 0){
                        if(cardChargeRule.getGiftType().equals(1)){
                            giftAmount = cardChargeRule.getGiftAmount();
                        }else if(cardChargeRule.getGiftType().equals(2)){
                            giftAmount = chargeAmount.multiply(cardChargeRule.getGiftAmount()).divide(new BigDecimal(100));
                        }
                        break;
                    }
                }
            }
        }

        //保存卡流水信息
        String seriesNumber = cardFlowService.generateSeriesNumber(BusinessTypeEnum.charge.getPrefix());
        CardFlow cardFlow = new CardFlow();
        cardFlow.setCardOwnerShop(cardType.getOwner().toString());
        cardFlow.setCardUseShop(shopId);
        cardFlow.setSeriesNo(seriesNumber);
        cardFlow.setAmount(chargeAmount);
        cardFlow.setGiftAmount(giftAmount);
        cardFlow.setOption(BusinessTypeEnum.charge.getType());
        cardFlow.setCardId(cardId);
        cardFlow.setCardTagNo(card.getTagId());
        cardFlow.setCreateTime(new Date());
        cardFlow.setCreator(operator);
        cardFlow.setLastBalance(card.getBalance());
        cardFlow.setCurBalance(card.getBalance().add(chargeAmount).add(giftAmount));
        cardFlow.setChargeType(chargeType);
        cardFlow.setRemark(remark);
        cardFlow.setCardMediumKey(card.getMediumKey());
        cardFlow.setCardMedium(card.getMedium());
        cardFlow.setLastPoint(card.getPoint());
        cardFlow.setCurPoint(card.getPoint());
        cardFlow.setIsPublic(card.getIsPublic());

        //修改卡信息
        card.setBalance(cardFlow.getCurBalance());

        cardFlowService.addCardFlow(cardFlow);
        editCard(card);

        //后台充值保存order到mongodb
        if (CardChargeTypeEnum.backend.getCode().equals(chargeType)){
            orderNo = orderService.generateOrderNo();
            orderService.addChargeOrder(orderNo, chargeAmount, giftAmount, new ObjectId(shopId), card.get_id());
        }

        //推送充值消息
        OrderMessage message = new OrderMessage();
        message.setAmount(card.getBalance());
        message.setChangeAmount(chargeAmount);
        message.setShopId(shopId);
        message.setTime(new Date());
        message.setType(1);
        message.setOrderId(orderNo);
        message.setPoint(card.getPoint());
        message.setChangePoint(0);
        message.setCardId(cardId);
        orderMsgProducer.sendOrderMessage(message);

        ChargeApiMsg result = new ChargeApiMsg();
        result.setSeriesNumber(seriesNumber);
        result.setLastBalance(cardFlow.getLastBalance());
        result.setCurBalance(cardFlow.getCurBalance());
        result.setChargeAmount(cardFlow.getAmount());
        result.setGiftAmount(giftAmount);
        return  result;
    }

    /**
     * 根据卡号获取卡信息
     * @param tagId
     * @return
     */
    public Card findByTagId(String tagId){
        List<Card> cardList = findByProperty("tagId", tagId);
        if(cardList.size()<=0){
            return null;
        }
        Card card = cardList.get(0);
        if(ValidatorUtil.isNotNull(card.getCardTypeId())){
            CardType cardType = cardTypeService.findBy_id(card.getCardTypeId());
            card.setCardTypeObject(cardType);
        }
        return card;
    }

    /**
     * 根据卡id获取卡信息
     * @param id
     * @return
     */
    public Card findByCardId(String id){
        Card card = findBy_id(new ObjectId(id));
        if(ValidatorUtil.isNotNull(card) && ValidatorUtil.isNotNull(card.getCardTypeId())){
            CardType cardType = cardTypeService.findBy_id(card.getCardTypeId());
            card.setCardTypeObject(cardType);
        }
        return card;
    }

    public List<Card> findByBatchId(ObjectId shopId, String batchId){
        Query query = new Query(Criteria.where("shopId").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("medium").ne(CardTypeEnum.coupon.getCode()).and("batchId").is(batchId));
        return getMongoTemplate().find(query, Card.class);
    }

    /**
     * 根据卡号和门店获取
     * @param tagId
     * @param shopId
     * @return
     */
    public Card findByTagIdAndShop(String tagId, ObjectId shopId){
        Query query = new Query(Criteria.where("tagId").is(tagId).and("shopId").is(shopId).and("medium").ne(CardTypeEnum.coupon.getCode()));
        List<Card> cardList = getMongoTemplate().find(query, Card.class);
        if(cardList.size()<=0){
            return null;
        }
        Card card = cardList.get(0);
        if(ValidatorUtil.isNotNull(card.getCardTypeId())){
            CardType cardType = cardTypeService.findBy_id(card.getCardTypeId());
            card.setCardTypeObject(cardType);
        }
        return card;
    }

    public List<CardImportVo> previewCards(MultipartFile file, Integer medium, Boolean isPublic, String cardTypeIdStr, String startTime, String endTime, Integer cardState, ObjectId shopId) throws Exception{
        CardType cardType = cardTypeService.findBy_id(new ObjectId(cardTypeIdStr));
        if(ValidatorUtil.isNull(cardType)){
            throw new ServiceException("卡类型提取失败~");
        }

        String fieldsName = "tagId,mediumKey";
        List<Map<String, String>> fileData = ExcelUtil.analysisImportFile(file, fieldsName);

        List<CardImportVo> result = new ArrayList<CardImportVo>();
        for (Map<String, String> data : fileData){
            String mediumKey = data.get("mediumKey");
            String tagId = data.get("tagId");
            if(ValidatorUtil.isNull(tagId)){
                continue;
            }else if(!medium.equals(3)&&ValidatorUtil.isNull(medium)){
                continue;
            }
            CardImportVo card = new CardImportVo();

            List<Card> existMediumKeys = findByTagOrMediumKey(tagId, mediumKey, shopId);
            if(existMediumKeys.size() > 0){
                card.setNoPassReason((existMediumKeys.get(0).getTagId().equals(tagId)?tagId:mediumKey) + "已存在");
                card.setPass(false);
            }
            card.setName(cardType.getName());
            card.setBalance(BigDecimal.ZERO);
            card.setCardTypeIdStr(cardTypeIdStr);
            card.setMediumKey(ValidatorUtil.isNotNull(mediumKey)?mediumKey:tagId);
            card.setMedium(medium);
            card.setTagId(tagId);
            card.setIsPublic(isPublic);
            card.setStartTime(DateUtil.formateStrToDate(startTime, "yyyy-MM-dd"));
            card.setEndTime(DateUtil.formateStrToDate(endTime, "yyyy-MM-dd"));
            card.setCreateTime(new Date());
            card.setCardState(cardState);
//            card.setCreator(operator);
            result.add(card);
        }

        return result;
    }

    public List<Card> findByTagOrMediumKey(String tagId, String mediumKey, ObjectId shopId){
        Criteria criteria = Criteria.where("shopId").is(shopId).orOperator(Criteria.where("tagId").is(tagId), Criteria.where("mediumKey").is(mediumKey));
        List<Card> cardList = getMongoTemplate().find(new Query(criteria), Card.class);
        return cardList;
    }

    /**
     * 导入实体卡信息生成会员卡
     * @param shopId
     */
    public Integer importCards(String dataStr, ObjectId shopId, String operator) throws Exception{
        List<Card> cardList = SerializationUtil.deSerializeList(dataStr, Card.class);

        CardType cardType = cardTypeService.findBy_id(new ObjectId(cardList.get(0).getCardTypeIdStr()));
        if(ValidatorUtil.isNull(cardType)){
            throw new ServiceException("卡类型提取失败~");
        }

        String batchId = cardBatchService.generateBatchIdByDate();

        int importCount = 0;
        for (Card card : cardList){
            card.setShopId(cardType.getOwner());
            card.setCreateShopId(shopId);
            card.setCreator(operator);
            card.setCardTypeId(cardType.get_id());
            card.setCardState(CardStateEnum.activated.getCode());
            List<Card> existCards = findByProperty("mediumKey", card.getMediumKey());
            if(existCards.size() > 0){
                continue;
            }
            importCount++;
            getMongoTemplate().insert(card);
        }

        if(importCount > 0){
            //保存生成记录
            CardBatch cardBatch = new CardBatch();
            cardBatch.setId(batchId);
            cardBatch.setMedium(cardList.get(0).getMedium());
            cardBatch.setCount(importCount);
            cardBatch.setCardTypeName(cardList.get(0).getName());
            cardBatch.setCreateTime(new Date());
            cardBatch.setIsPublic(cardList.get(0).getIsPublic());
            cardBatchService.saveCardBatch(cardBatch, shopId);
        }
        return importCount;
    }
    /**
     * 生成会员卡
     * @param cardBatch
     * @param shopId
     * @param operator
     */
    public void generateCards(CardBatch cardBatch, ObjectId shopId, String operator){
        if(countWillBeMade(cardBatch) > 2000){
            throw new ServiceException("一次最多生成2000张卡/券");
        }
        if(cardBatch.getEndNo() - cardBatch.getStartNo() < 0){
            throw new ServiceException("起止值设置有误");
        }
        if(ifRangeContainCard(cardBatch, shopId)){
            throw new ServiceException("编号取值范围内已存在~");
        }
        if(ValidatorUtil.isNotNull(cardBatch.getPrefix())){
            for (String key : retainCardTag.keySet()){
                if (cardBatch.getPrefix().startsWith(key) || key.startsWith(cardBatch.getPrefix())){
                    throw new ServiceException("前缀不可用~");
                }
            }
        }
        CardType cardType = cardTypeService.findBy_id(new ObjectId(cardBatch.getCardTypeIdStr()));
        if(ValidatorUtil.isNull(cardType)){
            throw new ServiceException("类型提取失败~");
        }


        String batchId = cardBatchService.generateBatchIdByDate();
        cardBatch.setId(batchId);
        cardBatch.setCount(cardBatch.getEndNo()-cardBatch.getStartNo()+1);
        cardBatch.setCardTypeName(cardType.getName());
        cardBatch.setCreateTime(new Date());
        cardBatch.setCreator(operator);

        for (int s=cardBatch.getStartNo(); s<=cardBatch.getEndNo(); s++){
            String randomKey = generateCardNo(cardBatch.getNoLength(), cardBatch.getPrefix(), cardBatch.getEndNo().toString().length(), s, cardBatch.getIsPublic(), true);
            String commonKey = generateCardNo(cardBatch.getNoLength(), cardBatch.getPrefix(), cardBatch.getEndNo().toString().length(), s, cardBatch.getIsPublic(), false);

            Card card = new Card();
            if (CardTypeEnum.ming.getCode().equals(cardBatch.getMedium()) || CardTypeEnum.coupon.getCode().equals(cardBatch.getMedium())){//明卡或券时，tagId与mediumKey相同
                if(cardBatch.getNoLength()-cardBatch.getPrefix().length()-cardBatch.getEndNo().toString().length()<2){
                    throw new ServiceException("请增大编号长度或减小前缀长度");
                }
                card.setTagId(randomKey);
                card.setMediumKey(randomKey);
            }else {
                card.setTagId(commonKey);
//                card.setMediumKey(randomKey);
            }
            card.setShopId(shopId);
            card.setName(cardType.getName());
            card.setCardTypeId(cardType.get_id());
            card.setMedium(cardBatch.getMedium());
            card.setIsPublic(cardBatch.getIsPublic());
            card.setStartTime(cardBatch.getStartTime());
            card.setEndTime(cardBatch.getEndTime());
            card.setCreateTime(new Date());
            card.setBatchId(batchId);
            card.setCardState(cardBatch.getCardState());
            card.setBalance(cardType.getInitBalance());
            card.setPoint(0);
            getMongoTemplate().insert(card);
        }
        //保存生成记录
        cardBatchService.saveCardBatch(cardBatch, shopId);
    }

    /**
     * 计算将要生成的数量
     * @param cardBatch
     * @return
     */
    public Integer countWillBeMade(CardBatch cardBatch){
        if (ValidatorUtil.isNull(cardBatch.getStartNo()) || ValidatorUtil.isNull(cardBatch.getEndNo())){
            return 0;
        }
        if (ValidatorUtil.isNull(cardBatch.getSpecialSuffix())){
            return cardBatch.getEndNo() - cardBatch.getStartNo() + 1;
        }
        String[] specialSuffixes = cardBatch.getSpecialSuffix().split("\\|");
        Integer count = 0;
        for (int i=cardBatch.getStartNo(); i<=cardBatch.getEndNo(); i++){
            boolean canBeMade = true;
            for (String specialSuffix: specialSuffixes){
                if (Convert.toString(i).endsWith(specialSuffix)){
                    canBeMade = false;
                    break;
                }
            }
            if (canBeMade){
                count++;
            }
        }
        return count;
    }

    /**
     * 编号是否已存在
     * @param cardBatch
     * @param shopId
     * @return
     */
    public boolean ifRangeContainCard(CardBatch cardBatch, ObjectId shopId ){
        Query query = new Query(Criteria.where("shopId").is(shopId).and("tagId").regex("^" + cardBatch.getPrefix() + "\\w{" + (cardBatch.getNoLength()-cardBatch.getPrefix().length()) + "}$"));
        List<Card> cards = getMongoTemplate().find(query, Card.class);
        for (Card card : cards){
            if(card.getTagId().length()!=cardBatch.getNoLength()){
                continue;
            }
            Integer no = Convert.toInteger(card.getTagId().substring(card.getTagId().length()-cardBatch.getEndNo().toString().length(), card.getTagId().length()));
            if(no>=cardBatch.getStartNo()&&no<=cardBatch.getEndNo()){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否操作卡权限
     * @param card
     * @param shopId
     */
    public boolean authCheck(Card card, String shopId){
        if (ValidatorUtil.isNull(card)){
            throw new ServiceException("没有这个卡");
        }
        if(!card.getShopId().toString().equals(shopId)){
            if(ValidatorUtil.isNull(card.getIsPublic()) || card.getIsPublic()){
                return false;
            }
            Shop mainShop = shopService.findMainShop(new ObjectId(shopId));
            if(ValidatorUtil.isNull(mainShop) || !mainShop.getObjectIdStr().equals(shopId)){
                return false;
            }
        }
        return true;
    }

    public boolean authCheck(String cardId, String shopId){
        Card card = findByCardId(cardId);
        return authCheck(card, shopId);
    }

    public List<Card> findActivatedCardByCardType(ObjectId cardTypeId){
        Query query = new Query(Criteria.where("cardType").is(cardTypeId).and("cardState").is(CardStateEnum.activated.getCode()));
        return getMongoTemplate().find(query, Card.class);
    }

    public Long findActivatedCardCountByCardType(ObjectId cardTypeId){
        Query query = new Query(Criteria.where("cardType").is(cardTypeId).and("cardState").is(CardStateEnum.activated.getCode()));
        return getMongoTemplate().count(query, Card.class);
    }


    /**
     * 生成编号
     * @param length
     * @param prefix
     * @param suffixLength 编号保留位长度
     * @param curNo
     * @param isPublic
     * @param withRandom
     * @return
     */
    private String generateCardNo(Integer length, String prefix, Integer suffixLength, Integer curNo, Boolean isPublic, boolean withRandom){
        StringBuffer keyBuffer = new StringBuffer(prefix);//物理识别码
        for (int i=0; i<length - prefix.length() - suffixLength - 1; i++){
            if (withRandom)
                keyBuffer.append(RandomUtil.genRandomNumberString(1));
            else
                keyBuffer.append(0);
        }
        if(ValidatorUtil.isNotNull(isPublic)&&isPublic){//通用卡作特别标识，防止与分店卡号相重
            keyBuffer.append(1);
        }else {
            keyBuffer.append(0);
        }
        for (int i=0; i<suffixLength-Convert.toString(curNo).length(); i++){
            keyBuffer.append(0);
        }
        keyBuffer.append(curNo);
        return keyBuffer.toString();
    }


    /**
     * 发放券到微会员
     * @param filterVo
     * @param couponTypes
     * @param shopId
     */
    public Integer sendCoupon2Card(CardFilterVo filterVo, List<CouponSendVo> couponTypes, ObjectId shopId){
        filterVo.setMedium(CardTypeEnum.virtual.getCode());
        List<Card> cards = cardMongoDao.findByFilter(filterVo, shopId, false);
        if (ValidatorUtil.isNotNull(cards)){
            String batchNo = cardBatchService.generateBatchIdRandom(shopId, 6);
            int cursor = 1;
            for (Card card : cards){
                for (CouponSendVo sendVo : couponTypes){
                    Card coupon = new Card();
                    CardType couponType = cardTypeService.findBy_id(sendVo.getCouponTypeId());
                    coupon.setCardTypeId(sendVo.getCouponTypeId());
                    coupon.setIsPublic(true);
                    coupon.setMedium(CardTypeEnum.coupon.getCode());
                    coupon.setBalance(couponType.getInitBalance());
                    coupon.setName(couponType.getName());
                    coupon.setCardState(CardStateEnum.activated.getCode());
                    coupon.setShopId(shopId);
                    coupon.setCreateShopId(shopId);
                    coupon.setEndTime(sendVo.getExpired());
                    coupon.setOwner(card.get_id());
                    coupon.setBatchId(batchNo);
                    coupon.setCreateTime(new Date());
                    for (int i=0; i<sendVo.getCount(); i++){
                        String tagId = generateMediumKey(batchNo, cursor);
                        String mediumKey = tagId + RandomUtil.genRandomNumberString(2);
                        coupon.setMediumKey(mediumKey);
                        coupon.setTagId(tagId);
                        cardMongoDao.insert(coupon);
                        cursor++;
                    }
                }
            }
            //保存批次
            CardBatch batch = new CardBatch();
            batch.setCouponSendVos(couponTypes);
            batch.setFitMemberCount(cards.size());
            batch.setMedium(CardTypeEnum.coupon.getCode());
            batch.setId(batchNo);
            batch.setCount(cards.size()*couponTypes.size());
            batch.setName(couponTypes.size() + "种券");
            cardBatchService.saveCardBatch(batch, shopId);
        }else {
            throw new ServiceException("未找到匹配的微会员");
        }
        return cards.size();
    }

    public void sendCouponToCardByOrder(ObjectId shopId, Order order){
        if (ValidatorUtil.isNull(order) || ValidatorUtil.isNull(order.getCard())){
            return;
        }
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNull(shop) || ValidatorUtil.isNull(shop.getTicketPublish())){
            return;
        }
        Date today = new Date();
        for (CardPublish cardPublish : shop.getTicketPublish()){
            if (ValidatorUtil.isNotNull(cardPublish.getMinLine()) && cardPublish.getMinLine().compareTo(order.getAmount()) > 0){
                continue;
            }
            if (ValidatorUtil.isNotNull(cardPublish.getStartTime()) && today.before(cardPublish.getStartTime())){
                continue;
            }
            if (ValidatorUtil.isNotNull(cardPublish.getEndTime()) && today.after(cardPublish.getEndTime())){
                continue;
            }
            if (ValidatorUtil.isNotNull(cardPublish.getFitDays())){
                String flag = cardPublish.getFitDays().substring(today.getDay(), today.getDay() + 1);
                if (!"1".equals(flag)){
                    continue;
                }
            }
            if (ValidatorUtil.isNotNull(cardPublish.getDayOff())){
                String formatter = "yyyy-MM-dd " + cardPublish.getDayOff() + ":00";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);

                String dateStr = simpleDateFormat.format(today);
                Date date = DateUtil.formateStrToDate(dateStr, "yyyy-MM-dd HH:mm:ss");
                if (today.after(date)){
                    continue;
                }
            }
            if (ValidatorUtil.isNotNull(cardPublish.getDayOn())){
                String formatter = "yyyy-MM-dd " + cardPublish.getDayOn() + ":00";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);

                String dateStr = simpleDateFormat.format(today);
                Date date = DateUtil.formateStrToDate(dateStr, "yyyy-MM-dd HH:mm:ss");
                if (today.before(date)){
                    continue;
                }
            }
            CardType couponType = cardTypeService.findBy_id(cardPublish.getTicket());
            boolean isFit = false;
            for (int i=0; i<cardPublish.getCount(); i++){
                Card coupon = new Card();
                coupon.setCardTypeId(cardPublish.getTicket());
                coupon.setIsPublic(true);
                coupon.setMedium(CardTypeEnum.coupon.getCode());
                coupon.setBalance(couponType.getInitBalance());
                coupon.setName(couponType.getName());
                coupon.setCardState(CardStateEnum.activated.getCode());
                coupon.setShopId(shopId);
                coupon.setCreateShopId(shopId);
                coupon.setEndTime(couponType.getExpiredDate());
                coupon.setOwner(order.getCard());
                coupon.setGainFrom(order.getId());
                coupon.setCreateTime(new Date());

                String tagId = "19" + RandomUtil.genRandomNumberString(11);
                String mediumKey = tagId + RandomUtil.genRandomNumberString(2);
                coupon.setMediumKey(mediumKey);
                coupon.setTagId(tagId);
                cardMongoDao.insert(coupon);
                isFit = true;
            }
            if (isFit)
                break;
        }
    }

    /**
     * 获取根据订单送出去的券
     * @param orderNo
     * @param shopId
     * @return
     */
    public List<Card> findCouponsSendByOrderId(String orderNo, ObjectId shopId){
        Query query = new Query(Criteria.where("shopId").is(shopId).and("gainFrom").is(orderNo).and("medium").is(CardTypeEnum.coupon.getCode()));
        return getMongoTemplate().find(query, Card.class);
    }

    private String generateMediumKey(String batchNo, int cursor){
        /*String no = atomicInteger.getAndIncrement() % 10000  + 10000 + "";
        return "19" + batchNo + no;*/
        String temp = 100000 + cursor%100000 + "";
        if (cursor/100000 >= 1){
            temp += cursor/100000;
        }
        return "19" + batchNo + temp.substring(1, temp.length());
    }

}
