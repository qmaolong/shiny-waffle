package com.covilla.service.pay;

import com.covilla.common.Config;
import com.covilla.model.PayFlow;
import com.covilla.util.wechat.util.Util;
import com.tencent.WXPay;
import com.tencent.common.Configure;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.tencent.protocol.pay_protocol.ScanPayResData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tencent.protocol.reverse_protocol.ReverseReqData;
import com.tencent.protocol.reverse_protocol.ReverseResData;
import com.tencent.service.ReverseService;
import com.tencent.service.ScanPayQueryService;
import com.tencent.service.ScanPayService;
import jodd.typeconverter.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/10/16.
 */
@Service
@Transactional
public class WXPayService {
    private Logger logger = LoggerFactory.getLogger(WXPayService.class);
    public WXPayService() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
    }

    //初始化支付参数
    static {
        WXPay.initSDKConfiguration(
                //签名算法需要用到的秘钥
                Config.WEIXIN_PAY_SIGN_KEY,
                //公众账号ID，成功申请公众账号后获得
                Config.WEIXIN_APPID,
                //商户ID，成功申请微信支付功能之后通过官方发出的邮件获得
                Config.WEIXIN_MERCHANT_ID,
                //子商户ID，受理模式下必填；
                null,
                //HTTP证书在服务器中的路径，用来加载证书用
                Config.WEIXIN_CERT_LOCATION,
                //HTTP证书的密码，默认等于MCHID
                Config.WEIXIN_MERCHANT_ID
        );
    }

    /**
     * 服务商代为支付
     * @param subAppId
     * @param subMchId
     * @param deviceInfo
     * @param body
     * @param orderNo
     * @param amount
     * @param ip
     * @param authCode
     * @return
     * @throws Exception
     */
    public ScanPayResData scanPayByBusiness(String subAppId, String subMchId, String deviceInfo, String body, String orderNo, BigDecimal amount, String ip, String authCode) throws Exception{
        Configure.setSubMchID(subMchId);
        Integer fee = Convert.toInteger(amount.multiply(new BigDecimal(100)));

        ScanPayReqData myScanPayReqData = new ScanPayReqData(authCode, body, null, orderNo, fee, deviceInfo, "", null, null, null, subAppId, subMchId);
        try {
            String payServiceResponseString = new ScanPayService().request(myScanPayReqData);
            ScanPayResData scanPayResData = (ScanPayResData) Util.getObjectFromXML(payServiceResponseString, ScanPayResData.class);
            return scanPayResData;
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }

    }

    /**
     * 查询支付状态
     * @param subMchId
     * @param orderNo
     * @return
     */
    public ScanPayQueryResData checkPayState(String subMchId, String orderNo) throws Exception{
        ScanPayQueryReqData reqData = new ScanPayQueryReqData(null, orderNo, subMchId);
        String result = new ScanPayQueryService().request(reqData);
        //将从API返回的XML数据映射到Java对象
        ScanPayQueryResData scanPayResData = (ScanPayQueryResData) Util.getObjectFromXML(result, ScanPayQueryResData.class);

        return scanPayResData;
    }

    /**
     * 请求撤销服务
     * @param payFlow
     * @return API返回的XML数据
     * @throws Exception
     */
    public ReverseResData rollBackPay(PayFlow payFlow) throws Exception {
        Configure.setSubMchID(payFlow.getMchId());

        ReverseReqData requestData = new ReverseReqData(payFlow.getSeriesNo(), payFlow.getOrderNo(), payFlow.getMchId(), payFlow.getAppId());
        String result = new ReverseService().request(requestData);

        //将从API返回的XML数据映射到Java对象
        ReverseResData reverseResData = (ReverseResData) Util.getObjectFromXML(result, ReverseResData.class);
        return reverseResData;
    }

}
