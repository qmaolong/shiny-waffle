package com.covilla.repository.mongodb;

import com.covilla.common.CardStateEnum;
import com.covilla.common.CardTypeEnum;
import com.covilla.model.mongo.card.Card;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.card.CardFilterVo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
@Repository
public class CardMongoDao extends BaseMongoDao<Card>{

    public void deleteCard(ObjectId cardId){
        Query query = new Query(Criteria.where("_id").is(cardId));
        Update update = new Update().set("cardState", CardStateEnum.disabled.getCode());
        getMongoTemplate().updateFirst(query, update, Card.class);
    }

    /**
     * 根据过滤对象查找卡
     * @param filterVo
     * @return
     */
    public List<Card> findByFilter(CardFilterVo filterVo, ObjectId shopId, boolean isCoupon){
        Criteria criteria = Criteria.where("shopId").is(shopId);

        //介质
        if (ValidatorUtil.isNotNull(filterVo) && ValidatorUtil.isNotNull(filterVo.getMedium())){
            criteria.and("medium").is(filterVo.getMedium());
        }else if (isCoupon){
            criteria.and("medium").is(CardTypeEnum.coupon.getCode());
        }else {
            Integer[] notIn = {CardTypeEnum.virtual.getCode(), CardTypeEnum.coupon.getCode()};
            criteria.and("medium").nin(notIn);
        }

        if (ValidatorUtil.isNotNull(filterVo)){
            if (ValidatorUtil.isNotNull(filterVo.getMemberName())){
                criteria.and("memberName").regex("/*" + filterVo.getMemberName() + "*");
            }
            if (ValidatorUtil.isNotNull(filterVo.getCardTypeId())){
                criteria.and("cardTypeId").is(filterVo.getCardTypeId());
            }
            if (ValidatorUtil.isNotNull(filterVo.getSex())){
                criteria.and("sex").is(filterVo.getSex());
            }
            if (ValidatorUtil.isNotNull(filterVo.getTagId())){
                criteria.and("tagId").is(filterVo.getTagId());
            }
            if (ValidatorUtil.isNotNull(filterVo.getTel())){
                criteria.and("tel").regex("/*" + filterVo.getTel() + "*");
            }
            if (ValidatorUtil.isNotNull(filterVo.getCardState())){
                criteria.and("cardState").is(filterVo.getCardState());
            }
            if (ValidatorUtil.isNotNull(filterVo.getIsPublic())){
                if (filterVo.getIsPublic())
                    criteria.and("isPublic").is(true);
                else
                    criteria.and("isPublic").ne(true);
            }
            if (ValidatorUtil.isNotNull(filterVo.getTagOrMemberName())){
                criteria.orOperator(Criteria.where("tagId").regex("/*" + filterVo.getTagOrMemberName() + "*"), Criteria.where("memberName").regex("/*" + filterVo.getTagOrMemberName() + "*"));
            }
        }

        List<Card> cards = getMongoTemplate().find(new Query(criteria), Card.class);

        if (ValidatorUtil.isNotNull(filterVo)){
            if (ValidatorUtil.isNotNull(filterVo.getBirthMonth())){
                Iterator<Card> it = cards.iterator();
                while (it.hasNext()){
                    Card card = it.next();
                    if (ValidatorUtil.isNull(card.getBirthDay()) || !filterVo.getBirthMonth().equals(card.getBirthDay().getMonth() + 1)){
                        it.remove();
                    }
                }
            }
            if (ValidatorUtil.isNotNull(filterVo.getBirthDay())){
                Iterator<Card> it = cards.iterator();
                while (it.hasNext()){
                    Card card = it.next();
                    if (ValidatorUtil.isNull(card.getBirthDay()) || !filterVo.getBirthDay().equals(card.getBirthDay().getDate())){
                        it.remove();
                    }
                }
            }
        }
        return cards;
    }
}
