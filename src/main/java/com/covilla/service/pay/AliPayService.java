package com.covilla.service.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.covilla.common.*;
import com.covilla.model.PayFlow;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 * Created by qmaolong on 2016/11/24.
 */
@Service
public class AliPayService {
    private static AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", Config.ALI_APPID, Config.ALI_PRIVATE_KEY,"json","UTF-8",Config.ALI_PUBLIC_KEY);

    /**
     * 条码实付
     * @param orderNo
     * @param cardAmount
     * @param cardId
     * @param body
     * @param shopId
     * @return
     * @throws Exception
     */
    public AlipayTradePayResponse scanPay(String appAuthToken, String orderNo, BigDecimal cardAmount, String cardId, String body, String shopId) throws Exception{
        JSONObject params = new JSONObject();
        params.put("out_trade_no",orderNo);
        params.put("scene", "bar_code");
        params.put("auth_code", cardId);
        params.put("subject", body);
        params.put("total_amount", cardAmount);
        params.put("body", "门店" + shopId);

        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setBizContent(params.toJSONString());
        AlipayTradePayResponse response = alipayClient.execute(request, null, appAuthToken);

        return response;
    }

    public AlipayTradeCancelResponse cancelPay(PayFlow payFlow) throws Exception{
        JSONObject params = new JSONObject();
        params.put("out_trade_no",payFlow.getOrderNo());
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizContent(params.toJSONString());
        AlipayTradeCancelResponse response = alipayClient.execute(request, null, payFlow.getAliToken());
        return response;
    }

    /**
     * 生成授权回调地址
     * @return
     */
    public String generateAuthURL(){
        String url = APIConstants.ALI_PAY_AUTH_URL.replace("APPID", Config.ALI_APPID).replace("CALLBACK", URLEncoder.encode(Constant.ALI_PAY_AUTH_CALLBACK));
        return url;
    }

    public AlipayTradeQueryResponse checkPayState(String alAuthToken, String orderNo) throws Exception{
        JSONObject params = new JSONObject();
        params.put("out_trade_no",orderNo);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent(params.toJSONString());
        AlipayTradeQueryResponse response = alipayClient.execute(request, null, alAuthToken);
        return response;
    }

    /**
     * 用获取或刷新token
     * @param code
     * @return
     * @throws Exception
     */
    public AlipayOpenAuthTokenAppResponse getOrRefreshAuthToken(String code, boolean isFresh) throws Exception{
        AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();

        JSONObject params = new JSONObject();
        params.put("grant_type", isFresh?"refresh_token":"authorization_code");
        params.put("code", code);

        request.setBizContent(params.toJSONString());
        AlipayOpenAuthTokenAppResponse response = alipayClient.execute(request);
        return response;
    }

}
