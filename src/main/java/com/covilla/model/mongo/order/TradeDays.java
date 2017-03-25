package com.covilla.model.mongo.order;

import com.covilla.model.mongo.BaseModel;
import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by qmaolong on 2016/10/15.
 */
@Document(collection = "tradeDay")
public class TradeDays extends BaseModel {
    @Field("id")
    private String id;
    private Integer cashierId;
    private String cashierName;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId shopId;
    private Integer userId;
    private String userName;
    private Date day;
    private Date startTime;
    private Date endTime;
    private Double amount1;
    private Double amount2;
    private Integer tradeCount;
    private Integer people;
    private Double totalAmount;
    private Double givenAmount;
    private Double itemDiscountAmount;
    private Double subAmount;
    private Double discountAmount;
    private Double specialDiscountAmount;
    private Double roundAmount;
    private Double lowAmount;
    private Double amount;
    private Double srvFee;
    private Double minPay;
    private Double actualAmount;
    private Double cashAmount;
    private Double changeAmount;
    private Double cardAmount;
    private Double bankAmount;
    private Double payCashAmount;
    private Double payChangeAmount;
    private Double payCardAmount;
    private Double payBankAmount;
    private Double loanAmount;
    private Double otherIncomeAmount;
    private Double otherOutcomeAmount;
    private Double cardChargeAmount;
    private Double cardReturnAmount;
    private Double ownedAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCashierId() {
        return cashierId;
    }

    public void setCashierId(Integer cashierId) {
        this.cashierId = cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public ObjectId getShopId() {
        return shopId;
    }

    public void setShopId(ObjectId shopId) {
        this.shopId = shopId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getAmount1() {
        return amount1;
    }

    public void setAmount1(Double amount1) {
        this.amount1 = amount1;
    }

    public Double getAmount2() {
        return amount2;
    }

    public void setAmount2(Double amount2) {
        this.amount2 = amount2;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getGivenAmount() {
        return givenAmount;
    }

    public void setGivenAmount(Double givenAmount) {
        this.givenAmount = givenAmount;
    }

    public Double getItemDiscountAmount() {
        return itemDiscountAmount;
    }

    public void setItemDiscountAmount(Double itemDiscountAmount) {
        this.itemDiscountAmount = itemDiscountAmount;
    }

    public Double getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(Double subAmount) {
        this.subAmount = subAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getSpecialDiscountAmount() {
        return specialDiscountAmount;
    }

    public void setSpecialDiscountAmount(Double specialDiscountAmount) {
        this.specialDiscountAmount = specialDiscountAmount;
    }

    public Double getRoundAmount() {
        return roundAmount;
    }

    public void setRoundAmount(Double roundAmount) {
        this.roundAmount = roundAmount;
    }

    public Double getLowAmount() {
        return lowAmount;
    }

    public void setLowAmount(Double lowAmount) {
        this.lowAmount = lowAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getSrvFee() {
        return srvFee;
    }

    public void setSrvFee(Double srvFee) {
        this.srvFee = srvFee;
    }

    public Double getMinPay() {
        return minPay;
    }

    public void setMinPay(Double minPay) {
        this.minPay = minPay;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Double getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(Double cardAmount) {
        this.cardAmount = cardAmount;
    }

    public Double getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(Double bankAmount) {
        this.bankAmount = bankAmount;
    }

    public Double getPayCashAmount() {
        return payCashAmount;
    }

    public void setPayCashAmount(Double payCashAmount) {
        this.payCashAmount = payCashAmount;
    }

    public Double getPayChangeAmount() {
        return payChangeAmount;
    }

    public void setPayChangeAmount(Double payChangeAmount) {
        this.payChangeAmount = payChangeAmount;
    }

    public Double getPayCardAmount() {
        return payCardAmount;
    }

    public void setPayCardAmount(Double payCardAmount) {
        this.payCardAmount = payCardAmount;
    }

    public Double getPayBankAmount() {
        return payBankAmount;
    }

    public void setPayBankAmount(Double payBankAmount) {
        this.payBankAmount = payBankAmount;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getOtherIncomeAmount() {
        return otherIncomeAmount;
    }

    public void setOtherIncomeAmount(Double otherIncomeAmount) {
        this.otherIncomeAmount = otherIncomeAmount;
    }

    public Double getOtherOutcomeAmount() {
        return otherOutcomeAmount;
    }

    public void setOtherOutcomeAmount(Double otherOutcomeAmount) {
        this.otherOutcomeAmount = otherOutcomeAmount;
    }

    public Double getCardChargeAmount() {
        return cardChargeAmount;
    }

    public void setCardChargeAmount(Double cardChargeAmount) {
        this.cardChargeAmount = cardChargeAmount;
    }

    public Double getCardReturnAmount() {
        return cardReturnAmount;
    }

    public void setCardReturnAmount(Double cardReturnAmount) {
        this.cardReturnAmount = cardReturnAmount;
    }

    public Double getOwnedAmount() {
        return ownedAmount;
    }

    public void setOwnedAmount(Double ownedAmount) {
        this.ownedAmount = ownedAmount;
    }
}
