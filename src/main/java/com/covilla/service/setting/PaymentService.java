package com.covilla.service.setting;

import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.covilla.common.Constant;
import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.common.PaymentEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.setting.Payment;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.service.pay.AliPayService;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ContentUtil;
import com.covilla.util.DateUtil;
import com.covilla.util.SerializationUtil;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2016/11/4.
 */
@Service
public class PaymentService extends BaseMongoService<Shop> {
    private Logger logger = LoggerFactory.getLogger(PaymentService.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    public void editPayment(String dataStr, String oper, ObjectId shopId){
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNull(shop)){
            throw new ServiceException("未找到相应门店");
        }

        if(OperationEnum.add.getCode().equals(oper)){
            Payment form = SerializationUtil.deSerializeObject(dataStr, Payment.class);
            if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getPayments())){
                for(Payment form1 : shop.getPayments()){
                    if(form.getName().equals(form1.getName())){
                        throw new ServiceException("该支付方式已存在~");
                    }
                }
            }
            String abc = ContentUtil.getPinYinHeadChar(form.getDescribe());
            form.setAbc(abc.toUpperCase());
            //设置支付宝
            if(PaymentEnum.aliPay.getName().equals(form.getName())){
                if(ValidatorUtil.isNull(form.getAlAppId())&&ValidatorUtil.isNull(form.getAuthCode())){
                    throw new ServiceException("请授权支付宝支付");
                }else if(ValidatorUtil.isNotNull(form.getAuthCode())){
                    try {
                        AlipayOpenAuthTokenAppResponse response = aliPayService.getOrRefreshAuthToken(form.getAuthCode(), false);
                        if(ValidatorUtil.isNotNull((response.getAppAuthToken()))) {
                            form.setAlAppId(response.getAuthAppId());
                            form.setAlUserId(response.getUserId());
                            form.setAlAuthToken(response.getAppAuthToken());
                            form.setAlRefreshToken(response.getAppRefreshToken());
                            form.setAlTokenExpireDate(DateUtil.addDay(new Date(), 364));
                            form.setAlRefreshTokenExpireDate(DateUtil.addDay(new Date(), 371));
                            batchUpdateAliAuthInfo(response);
                        }else{
                            throw new ServiceException(response.getSubMsg());
                        }
                    }catch (Exception e){
                        logger.error(e.getMessage());
                        throw new ServiceException("支付宝信息提取失败");
                    }
                }
            }
            Criteria criteria =  Criteria.where("_id").is(shopId);
            Update update = new Update();
            update.push("payment", form);

            getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        }else if(OperationEnum.edit.getCode().equals(oper)){
            Payment form = SerializationUtil.deSerializeObject(dataStr, Payment.class);
            String abc = ContentUtil.getPinYinHeadChar(form.getDescribe());
            form.setAbc(abc.toUpperCase());

            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId).and("payment.name").is(form.getName()));
            Update update = new Update().set("payment.$.fiveTicket", form.getGiveTicket()).set("payment.$.givePoint", form.getGivePoint()).set("payment.$.unit", form.getUnit()).set("payment.$.activated", form.getActivated()).set("payment.$.wxAppId", form.getWxAppId()).set("payment.$.wxMchId", form.getWxMchId());
            //设置支付宝
            if(PaymentEnum.aliPay.getName().equals(form.getName())){
                if(ValidatorUtil.isNull(form.getAlAppId())&&ValidatorUtil.isNull(form.getAuthCode())){
                    throw new ServiceException("请授权支付宝支付");
                }else if(ValidatorUtil.isNotNull(form.getAuthCode())){
                    try {
                        AlipayOpenAuthTokenAppResponse response = aliPayService.getOrRefreshAuthToken(form.getAuthCode(), false);
                        if(ValidatorUtil.isNotNull((response.getAppAuthToken()))){
                            update.set("payment.$.alAppId", response.getAuthAppId());
                            update.set("payment.$.alUserId", response.getUserId());
                            update.set("payment.$.alAuthToken", response.getAppAuthToken());
                            update.set("payment.$.alRefreshToken", response.getAppRefreshToken());
                            update.set("payment.$.alTokenExpireDate", DateUtil.addDay(new Date(), 364));
                            update.set("payment.$.alRefreshTokenExpireDate", DateUtil.addDay(new Date(), 371));
                            batchUpdateAliAuthInfo(response);
                        }else {
                            throw new ServiceException(response.getSubMsg());
                        }
                    }catch (Exception e){
                        logger.error(e.getMessage());
                        throw new ServiceException("支付宝授权信息提取失败");
                    }
                }
            }
            getMongoTemplate().updateFirst(query, update, Shop.class);
        }else if(OperationEnum.delete.getCode().equals(oper)&&ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getPayments())){
            List<Payment> deleteItems = SerializationUtil.deSerializeList(dataStr, Payment.class);
            for (int i=0; i<shop.getPayments().size(); i++){
                for (Payment form : deleteItems){
                    if(shop.getPayments().get(i).getName().equals(form.getName())){
                        shop.getPayments().remove(i);
                        break;
                    }
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));
            getMongoTemplate().updateFirst(query, new Update().set("payment", shop.getPayments()), Shop.class);
        }else {
            throw new ServiceException("操作失败~");
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());

    }

    /**
     * 重新排序
     * @param shopId
     */
    public void sortPayment(String dataStr, ObjectId shopId){
        Shop shop = shopService.findBy_id(shopId);
        List<Payment> payments = SerializationUtil.deSerializeList(dataStr, Payment.class);

        List<Payment> olds = shop.getPayments();
        if(olds.size() != payments.size()){
            throw new ServiceException("数据有变更，请刷新重试~");
        }

        Query query = new Query(Criteria.where("_id").is(shopId));
        Update update = new Update().set("payment", payments);
        getMongoTemplate().updateFirst(query, update, Shop.class);
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    public Payment findPayment(ObjectId shopId, String paymentType){
        Shop shop = shopService.findBy_id(shopId);
        List<Payment> payments = shop.getPayments();
        for (Payment payment : payments){
            if(paymentType.equals(payment.getName())){
                return payment;
            }
        }
        return null;
    }

    /**
     * 刷新支付宝授权token
     * @param shopId
     * @return
     */
    public Payment refreshAliPayment(String shopId){
        Payment payment = findPayment(new ObjectId(shopId), PaymentEnum.aliPay.getName());
        try {
            AlipayOpenAuthTokenAppResponse response = aliPayService.getOrRefreshAuthToken(payment.getAlRefreshToken(), true);
            if (ValidatorUtil.isNotNull(response.getAppAuthToken())){
                batchUpdateAliAuthInfo(response);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ServiceException("重新获取授权信息失败~");
        }
        return payment;
    }

    public void batchUpdateAliAuthInfo(AlipayOpenAuthTokenAppResponse authInfo){
        if(ValidatorUtil.isNotNull(authInfo.getAppAuthToken())){
            Query query = new Query().addCriteria(Criteria.where("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("payment.name").is(PaymentEnum.aliPay.getName()).and("payment.alAppId").is(authInfo.getAuthAppId()));
            Update update = new Update();
            update.set("payment.$.alAppId", authInfo.getAuthAppId());
            update.set("payment.$.alUserId", authInfo.getUserId());
            update.set("payment.$.alAuthToken", authInfo.getAppAuthToken());
            update.set("payment.$.alRefreshToken", authInfo.getAppRefreshToken());
            update.set("payment.$.alTokenExpireDate", DateUtil.addDay(new Date(), 364));
            update.set("payment.$.alRefreshTokenExpireDate", DateUtil.addDay(new Date(), 371));
            getMongoTemplate().updateMulti(query, update, Shop.class);
        }
    }
}
