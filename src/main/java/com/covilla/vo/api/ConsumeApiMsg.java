package com.covilla.vo.api;

import com.covilla.common.PayStateEnum;
import com.covilla.vo.BaseApiResultMsg;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/9/23.
 */
public class ConsumeApiMsg extends BaseApiResultMsg {
    private String seriesNumber;
    private BigDecimal lastBalance;
    private BigDecimal curBalance;
    private Integer lastPoint;
    private Integer curPoint;
    private String payState = PayStateEnum.success.getName();
    private String stateDesc;
    private String adviceOption;
    private String payType;
    private String orderNo;
    private String payAmount;
    private String endTime;

    /**
     * 返回业务成功
     * @return
     */
    public static ConsumeApiMsg buildSuccessMsg(String payType, String orderNo, String seriesNo, String payAmount, String endTime, BigDecimal lastBalance, BigDecimal curBalance, Integer lastPoint, Integer curPoint){
        ConsumeApiMsg result = new ConsumeApiMsg();
        result.setPayType(payType);
        result.setSeriesNumber(seriesNo);
        result.setOrderNo(orderNo);
        result.setPayAmount(payAmount);
        result.setEndTime(endTime);
        result.setPayState(PayStateEnum.success.getName());
        result.setLastBalance(lastBalance);
        result.setCurBalance(curBalance);
        result.setLastPoint(lastPoint);
        result.setCurPoint(curPoint);
        return result;
    }

    /**
     * 返回业务失败
     * @param msg
     * @return
     */
    public static ConsumeApiMsg buildFailMsg(String msg){
        ConsumeApiMsg baseResultMsg = new ConsumeApiMsg();
        baseResultMsg.setPayState(PayStateEnum.fail.getName());
        baseResultMsg.setMsg(msg);

        return baseResultMsg;
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
    public static ConsumeApiMsg buildStateMsg(String payType, String payState, String stateDesc, String adviceOption, String orderNo, String seriesNo, String payAmount, String endTime){
        ConsumeApiMsg result = new ConsumeApiMsg();
        result.setPayType(payType);
        result.setStateDesc(stateDesc);
        result.setAdviceOption(adviceOption);
        result.setSeriesNumber(seriesNo);
        result.setOrderNo(orderNo);
        result.setPayAmount(payAmount);
        result.setEndTime(endTime);
        result.setPayState(payState);
        return result;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public BigDecimal getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(BigDecimal lastBalance) {
        this.lastBalance = lastBalance;
    }

    public BigDecimal getCurBalance() {
        return curBalance;
    }

    public void setCurBalance(BigDecimal curBalance) {
        this.curBalance = curBalance;
    }

    public Integer getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(Integer lastPoint) {
        this.lastPoint = lastPoint;
    }

    public Integer getCurPoint() {
        return curPoint;
    }

    public void setCurPoint(Integer curPoint) {
        this.curPoint = curPoint;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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
