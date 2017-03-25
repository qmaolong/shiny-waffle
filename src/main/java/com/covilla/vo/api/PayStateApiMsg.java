package com.covilla.vo.api;

import com.covilla.common.PayStateEnum;
import com.covilla.vo.BaseApiResultMsg;

/**
 * Created by qmaolong on 2016/11/22.
 */
public class PayStateApiMsg extends BaseApiResultMsg {
    private String payState;
    private String stateDesc = "";
    private String adviceOption = "";
    private String payType;
    private String orderNo;
    private String seriesNumber;
    private String payAmount;
    private String endTime;

    /**
     * 返回系统错误
     * @param errcode
     * @param msg
     * @return
     */
    public static PayStateApiMsg buildErrorMsg(String errcode, String msg){
        PayStateApiMsg baseResultMsg = new PayStateApiMsg();
        baseResultMsg.setErrcode(errcode);
        baseResultMsg.setMsg(msg);

        return baseResultMsg;
    }

    /**
     * 返回业务失败
     * @param msg
     * @return
     */
    public static PayStateApiMsg buildFailMsg(String payType, String msg, String orderNo, String seriesNo, String payAmount, String endTime){
        PayStateApiMsg baseResultMsg = new PayStateApiMsg();
        baseResultMsg.setPayType(payType);
        baseResultMsg.setSeriesNumber(seriesNo);
        baseResultMsg.setOrderNo(orderNo);
        baseResultMsg.setPayAmount(payAmount);
        baseResultMsg.setEndTime(endTime);
        baseResultMsg.setPayState(PayStateEnum.fail.getName());
        baseResultMsg.setMsg(msg);

        return baseResultMsg;
    }

    /**
     * 返回业务成功
     * @param payType
     * @param orderNo
     * @param seriesNo
     * @param payAmount
     * @param endTime
     * @return
     */
    public static PayStateApiMsg buildSuccessMsg(String payType, String orderNo, String seriesNo, String payAmount, String endTime){
        PayStateApiMsg result = new PayStateApiMsg();
        result.setPayType(payType);
        result.setSeriesNumber(seriesNo);
        result.setOrderNo(orderNo);
        result.setPayAmount(payAmount);
        result.setEndTime(endTime);
        result.setPayState(PayStateEnum.success.getName());
        return result;
    }

    /**
     * 返回业务状态
     * @param payType
     * @param payState
     * @param stateDesc
     * @param adviceOption
     * @param orderNo
     * @param seriesNo
     * @param payAmount
     * @param endTime
     * @return
     */
    public static PayStateApiMsg buildStateMsg(String payType, String payState, String stateDesc, String adviceOption, String orderNo, String seriesNo, String payAmount, String endTime){
        PayStateApiMsg result = new PayStateApiMsg();
        result.setPayType(payType);
        result.setSeriesNumber(seriesNo);
        result.setOrderNo(orderNo);
        result.setPayAmount(payAmount);
        result.setEndTime(endTime);
        result.setPayState(payState);
        result.setStateDesc(stateDesc);
        result.setAdviceOption(adviceOption);
        return result;
    }

    /*public static PayStateMsg buildErrorMsg(String payType, String errcode, String msg){
        PayStateMsg result = new PayStateMsg();
        result.setPayType(payType);
        result.setErrcode(errcode);
        result.setMsg(msg);
        result.setPayState("USERPAYING");

        return result;
    }*/

    /*public static PayStateMsg buildWaitMsg(String payType){
        PayStateMsg result = new PayStateMsg();
        result.setPayState("USERPAYING");
        result.setMsg("等待支付完成");
        result.setPayType(payType);
        return result;
    }*/

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getAdviceOption() {
        return adviceOption;
    }

    public void setAdviceOption(String adviceOption) {
        this.adviceOption = adviceOption;
    }
}
