package com.covilla.service.pay;

import com.covilla.model.PayFlow;
import com.covilla.repository.jpa.PayFlowDao;
import com.covilla.service.config.ConfigService;
import com.covilla.util.DateUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.filter.QueryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2016/11/22.
 */
@Service
@Transactional
public class PayFlowService {
    /*@Autowired
    private PayFlowDao payFlowDao;*/
    @Autowired
    private ConfigService configService;

    public PayFlow findByOrderNo(String orderNo){
        /*PayFlow example = new PayFlow();
        example.setOrderNo(orderNo);
        List<PayFlow> payFlows = payFlowDao.findAll(Example.of(example));
        if(ValidatorUtil.isNull(payFlows)){
            return null;
        }
        return payFlows.get(0);*/
        return null;
    }

    public PayFlow findBySeriesNo(String seriesNo){
        /*PayFlow example = new PayFlow();
        example.setSeriesNo(seriesNo);
        List<PayFlow> payFlows = payFlowDao.findAll(Example.of(example));
        if(ValidatorUtil.isNull(payFlows)){
            return null;
        }
        return payFlows.get(0);*/
        return null;
    }

    public PayFlow updateByOrderNo(PayFlow payFlow){
        /*return payFlowDao.save(payFlow);*/
        return null;
    }

    /**
     * 获取筛选支付成功数据
     * @param filter
     * @return
     */
    public List<PayFlow> getPayFlow(QueryFilter filter, String shopId){
        /*PayFlowExample example = new PayFlowExample();
        PayFlowExample.Criteria criteria = example.createCriteria().andShopIdEqualTo(shopId);

        if(ValidatorUtil.isNotNull(filter.getStartDate())&&ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.andCreateTimeBetween(filter.getStartDate(), DateUtil.addDay(filter.getEndDate(), 1));
        }else if(ValidatorUtil.isNotNull(filter.getStartDate())){
            criteria.andCreateTimeGreaterThan(filter.getStartDate());
        }else if(ValidatorUtil.isNotNull(filter.getEndDate())){
            criteria.andCreateTimeLessThan(DateUtil.addDay(filter.getEndDate(), 1));
        }

        return payFlowDao.selectByExample(example);*/
        return null;
    }

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

    public PayFlow save(PayFlow payFlow){
        /*return payFlowDao.save(payFlow);*/
        return null;
    }

}
