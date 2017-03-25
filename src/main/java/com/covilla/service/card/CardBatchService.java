package com.covilla.service.card;

import com.covilla.common.CardTypeEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.card.CardBatch;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.BaseMongoService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.DateUtil;
import com.covilla.util.RandomUtil;
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
 * Created by qmaolong on 2016/11/16.
 */
@Service
public class CardBatchService extends BaseMongoService<Shop> {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    public void saveCardBatch(CardBatch cardBatch, ObjectId shopId){
        Query query  = new Query(Criteria.where("_id").is(shopId));
        Update update = new Update().push("cardBatch", cardBatch);
        getMongoTemplate().updateFirst(query, update, Shop.class);
    }

    public CardBatch findByBatchId(String batchId, ObjectId shopId){
        Shop shop = findBy_id(shopId);
        if (ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCardBatches())){
            for (CardBatch batch : shop.getCardBatches()){
                if(batchId.equals(batch.getId())){
                    return batch;
                }
            }
        }
        return null;
    }

    public List<CardBatch> findBatches(ObjectId shopId, boolean isCoupon){
        Shop shop = findBy_id(shopId);

        List<CardBatch> result = new ArrayList<CardBatch>();
        if (ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCardBatches())){
            for (CardBatch batch : shop.getCardBatches()){
                if(isCoupon && CardTypeEnum.coupon.getCode().equals(batch.getMedium())){
                    result.add(batch);
                }else if(!isCoupon && !CardTypeEnum.coupon.getCode().equals(batch.getMedium())){
                    result.add(batch);
                }
            }
        }
        return result;
    }

    /**
     * 生成批次号
     * @return
     */
    public String generateBatchIdByDate(){
        Date now = new Date();
        String result = DateUtil.formateDateToStr(now, "yyyyMMddHHmmss");
        result += RandomUtil.genRandomNumberString(2);
        return result;
    }

    public String generateBatchIdRandom(ObjectId shopId, int length){
        Shop shop = findBy_id(shopId);
        while (true){
            String id = RandomUtil.genRandomNumberString(length);
            if (ValidatorUtil.isNotNull(shop) && ValidatorUtil.isNotNull(shop.getCardBatches())){
                for (CardBatch batch : shop.getCardBatches()){
                    if (id.equals(batch.getId())){
                        continue;
                    }
                }
            }
            return id;
        }
    }
}
