package com.covilla.service.card;

import com.covilla.common.BusinessTypeEnum;
import com.covilla.model.CardFlow;
import com.covilla.repository.jpa.CardFlowDao;
import com.covilla.service.config.ConfigService;
import com.covilla.util.DateUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.filter.QueryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2016/11/21.
 */
@Service
@Transactional
public class CardFlowService {
    /*@Autowired
    private CardFlowDao cardFlowDao;*/
    @Autowired
    private ConfigService configService;

    public List<CardFlow> getByOwnerShop(String shopId){
        /*CardFlow example = new CardFlow();
        example.setCardOwnerShop(shopId);
        return cardFlowDao.findAll(Example.of(example));*/
        return null;
    }

    public List<CardFlow> getByOperateShop(String shopId){
        /*CardFlow example = new CardFlow();
        example.setCardUseShop(shopId);
        return cardFlowDao.findAll(Example.of(example));*/
        return null;
    }

    public CardFlow getBySeriesNo(String seriesNo, Integer option){
        /*CardFlow example = new CardFlow();
        example.setSeriesNo(seriesNo);
        if (ValidatorUtil.isNotNull(option)){
            example.setOption(option);
        }
        List<CardFlow> list =  cardFlowDao.findAll(Example.of(example));
        if(ValidatorUtil.isNotNull(list)){
            return list.get(0);
        }*/
        return null;
    }

    /**
     * 根据消费门店获取筛选支付成功数据
     * @param filter
     * @return
     */
    public List<CardFlow> getCardFlowByUseShop(QueryFilter filter){
        /*CardFlowExample example = new CardFlowExample();
        CardFlowExample.Criteria criteria = example.createCriteria();

        //门店
        if(ValidatorUtil.isNotNull(filter.getShopId())){
            criteria.andCardUseShopEqualTo(filter.getShopId());
        }else {
            List<String> shopIds = new ArrayList<String>();
            for (ObjectId objectId : filter.getShopIds()){
                shopIds.add(objectId.toString());
            }
            criteria.andCardUseShopIn(shopIds);
        }

        //日期
        if(ValidatorUtil.isNotNull(filter.getStartDate())&&ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.andCreateTimeBetween(filter.getStartDate(), DateUtil.addDay(filter.getEndDate(), 1));
        }else if(ValidatorUtil.isNotNull(filter.getStartDate())){
            criteria.andCreateTimeGreaterThan(filter.getStartDate());
        }else if(ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.andCreateTimeLessThan(DateUtil.addDay(filter.getEndDate(), 1));
        }

        //操作者
        if (ValidatorUtil.isNotNull(filter.getOperator())){
            criteria.andCreatorLike(filter.getOperator());
        }

        //卡or券
        if(ValidatorUtil.isNotNull(filter.getIsCoupon())){
            if(filter.getIsCoupon()){
                criteria.andCardMediumEqualTo(CardTypeEnum.coupon.getCode());
            }else {
                criteria.andCardMediumNotEqualTo(CardTypeEnum.coupon.getCode());
            }
        }

        //是否通用
        if(ValidatorUtil.isNotNull(filter.getIsPublic())){
            if(filter.getIsPublic()){
                criteria.andIsPublicEqualTo(true);
            }else {
                criteria.andIsPublicNotEqualTo(true);
            }
        }

        return cardFlowDao.selectByExample(example);*/
        return null;
    }

    /**
     * 根据卡所属门店门店获取筛选支付成功数据
     * @param filter
     * @return
     */
    public List<CardFlow> getCardFlowByOwnerShop(QueryFilter filter){
        /*CardFlowExample example = new CardFlowExample();
        CardFlowExample.Criteria criteria = example.createCriteria();

        //门店
        if(ValidatorUtil.isNotNull(filter.getShopId())){
            criteria.andCardOwnerShopEqualTo(filter.getShopId());
        }else {
            List<String> shopIds = new ArrayList<String>();
            for (ObjectId objectId : filter.getShopIds()){
                shopIds.add(objectId.toString());
            }
            criteria.andCardOwnerShopIn(shopIds);
        }

        //日期
        if(ValidatorUtil.isNotNull(filter.getStartDate())&&ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.andCreateTimeBetween(filter.getStartDate(), DateUtil.addDay(filter.getEndDate(), 1));
        }else if(ValidatorUtil.isNotNull(filter.getStartDate())){
            criteria.andCreateTimeGreaterThan(filter.getStartDate());
        }else if(ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.andCreateTimeLessThan(DateUtil.addDay(filter.getEndDate(), 1));
        }

        //操作者
        if (ValidatorUtil.isNotNull(filter.getOperator())){
            criteria.andCreatorLike(filter.getOperator());
        }

        //卡or券
        if(ValidatorUtil.isNotNull(filter.getIsCoupon())){
            if(filter.getIsCoupon()){
                criteria.andCardMediumEqualTo(CardTypeEnum.coupon.getCode());
            }else {
                criteria.andCardMediumNotEqualTo(CardTypeEnum.coupon.getCode());
            }
        }

        //是否通用
        if(ValidatorUtil.isNotNull(filter.getIsPublic())){
            if(filter.getIsPublic()){
                criteria.andIsPublicEqualTo(true);
            }else {
                criteria.andIsPublicNotEqualTo(true);
            }
        }

        return cardFlowDao.selectByExample(example);*/
        return null;
    }

    public CardFlow addCardFlow(CardFlow cardFlow){
        /*return cardFlowDao.save(cardFlow);*/
        return null;
    }

    public CardFlow updateFlow(CardFlow cardFlow){
        /*return cardFlowDao.save(cardFlow);*/
        return null;
    }

    public int updateByExample(CardFlow cardFlow){
//        return cardFlowDao.updateByExampleSelective(cardFlow, example);
        return 1;
    }

    /**
     * 生成流水号
     * @param prefix
     * @return
     */
    public String generateSeriesNumber(String prefix){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(prefix);
        String dayStr = DateUtil.formateDateToStr(new Date(), "yyMMdd");
        stringBuffer.append(dayStr);
        Integer lastId = configService.getDayBusinessNo();
        for(int j=0;j<6-lastId.toString().length();j++){
            stringBuffer.append("0");
        }
        stringBuffer.append(lastId);
        return stringBuffer.toString();
    }

    public List<CardFlow> findByCardIdSortDesc(String cardId){
        /*CardFlow example = new CardFlow();
        example.setCardId(cardId);
        example.setOption(BusinessTypeEnum.charge.getType());
        List<CardFlow> cardFlows = cardFlowDao.findAll(Example.of(example), new Sort("desc"));
        return cardFlows;*/
        return null;
    }

    public List<CardFlow> findAll(){
        /*return cardFlowDao.findAll();*/
        return null;
    }

    public CardFlow findAddPointFlow(String orderNo, String shopId){
        /*CardFlow example = new CardFlow();
        example.setOrderNo(orderNo);
        example.setCardUseShop(shopId);
        List<CardFlow> cardFlows = cardFlowDao.findAll(Example.of(example));
        if (ValidatorUtil.isNotNull(cardFlows))
            return cardFlows.get(0);
        else
            return null;*/
        return null;
    }

    /**
     * 获取订单折扣记录
     * @param orderNo
     * @param shopId
     * @return
     */
    public List<CardFlow> findDiscountFlows(String orderNo, String shopId){
        /*CardFlow example = new CardFlow();
        example.setOrderNo(orderNo);
        example.setCardUseShop(shopId);
//        example.createCriteria().andOrderNoLike(orderNo + "%").andOptionEqualTo(BusinessTypeEnum.makeDiscount.getType()).andCardUseShopEqualTo(shopId);
        return cardFlowDao.findAll(Example.of(example));*/
        return null;
    }
}
