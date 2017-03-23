package com.covilla.model.mongo.order;

import com.covilla.common.OrderTypeEnum;
import com.covilla.model.mongo.BaseModel;
import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.covilla.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2016/10/13.
 */
public class Order extends BaseModel {
    @Field("id")
    private String id;
    private Integer deskId;
    private String deskName;
    private Integer people;
    private Integer waiterId;
    private Date tradeDay;
    private String tradeDayId;
    private BigDecimal totalAmount;
    private BigDecimal givenAmount;
    private BigDecimal itemDiscountAmount;
    private BigDecimal subAmount;
    private BigDecimal discountAmount;
    private BigDecimal specialDiscountAmount;
    private BigDecimal roundAmount;
    private BigDecimal lowAmount;
    private BigDecimal amount;
    private BigDecimal srvFee;
    private BigDecimal minPay;
    private BigDecimal actualAmount;
    private BigDecimal cashAmount;
    private BigDecimal cardAmount;
    private BigDecimal bankAmount;
    private BigDecimal changeAmount;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId shopId;
    private List<PayItem> payItems;
    private List<OrderItem> orderItems;
    private Date orderTime;
    private String waiterName;
    private String operatorName;
    private String operatorId;
    private String cashierName;
    private Integer cashierId;
    private Date payTime;
    private Integer state;
    private Integer orderType;
    private String remark;
    @Transient
    private String orderTypeName;
    @Transient
    private BigDecimal payedAmount;//实收金额，payItems总和
    private DiscountRule discountRule;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId card;
    private String refId;
    //后台私有参数
    private Date dealFlag;

    public BigDecimal getPayedAmount() {
        if(ValidatorUtil.isNotNull(payItems)){
            payedAmount = BigDecimal.ZERO;
            for (PayItem item : payItems){
                payedAmount = payedAmount.add(item.getAmount());
            }
        }
        return payedAmount;
    }

    public ObjectId getCard() {
        return card;
    }

    public void setCard(ObjectId card) {
        this.card = card;
    }

    public void setPayedAmount(BigDecimal payedAmount) {
        this.payedAmount = payedAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDeskId() {
        return deskId;
    }

    public void setDeskId(Integer deskId) {
        this.deskId = deskId;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public Integer getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Integer waiterId) {
        this.waiterId = waiterId;
    }

    public Date getTradeDay() {
        return tradeDay;
    }

    public void setTradeDay(Date tradeDay) {
        this.tradeDay = tradeDay;
    }

    public String getTradeDayId() {
        return tradeDayId;
    }

    public void setTradeDayId(String tradeDayId) {
        this.tradeDayId = tradeDayId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getGivenAmount() {
        return givenAmount;
    }

    public void setGivenAmount(BigDecimal givenAmount) {
        this.givenAmount = givenAmount;
    }

    public BigDecimal getItemDiscountAmount() {
        return itemDiscountAmount;
    }

    public void setItemDiscountAmount(BigDecimal itemDiscountAmount) {
        this.itemDiscountAmount = itemDiscountAmount;
    }

    public BigDecimal getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(BigDecimal subAmount) {
        this.subAmount = subAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getSpecialDiscountAmount() {
        return specialDiscountAmount;
    }

    public void setSpecialDiscountAmount(BigDecimal specialDiscountAmount) {
        this.specialDiscountAmount = specialDiscountAmount;
    }

    public BigDecimal getRoundAmount() {
        return roundAmount;
    }

    public void setRoundAmount(BigDecimal roundAmount) {
        this.roundAmount = roundAmount;
    }

    public BigDecimal getLowAmount() {
        return lowAmount;
    }

    public void setLowAmount(BigDecimal lowAmount) {
        this.lowAmount = lowAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getSrvFee() {
        return srvFee;
    }

    public void setSrvFee(BigDecimal srvFee) {
        this.srvFee = srvFee;
    }

    public BigDecimal getMinPay() {
        return minPay;
    }

    public void setMinPay(BigDecimal minPay) {
        this.minPay = minPay;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }

    public BigDecimal getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(BigDecimal bankAmount) {
        this.bankAmount = bankAmount;
    }

    public ObjectId getShopId() {
        return shopId;
    }

    public void setShopId(ObjectId shopId) {
        this.shopId = shopId;
    }

    public List<PayItem> getPayItems() {
        return payItems;
    }

    public void setPayItems(List<PayItem> payItems) {
        this.payItems = payItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public Integer getCashierId() {
        return cashierId;
    }

    public void setCashierId(Integer cashierId) {
        this.cashierId = cashierId;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getOrderTypeName() {
        if(null != orderType){
            return OrderTypeEnum.getNameByCode(orderType);
        }
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public Date getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(Date dealFlag) {
        this.dealFlag = dealFlag;
    }

    public DiscountRule getDiscountRule() {
        return discountRule;
    }

    public void setDiscountRule(DiscountRule discountRule) {
        this.discountRule = discountRule;
    }

    private class DiscountRule {
        private String name;

        private Integer cardId;

        private String startTime;

        private String endTime;

        private Integer discountType;

        private Double minLine;

        private Integer percent;

        private Double reduction;

        private String fitDays;

        private Date createTime;

        private Integer creator;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCardId() {
            return cardId;
        }

        public void setCardId(Integer cardId) {
            this.cardId = cardId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Integer getDiscountType() {
            return discountType;
        }

        public void setDiscountType(Integer discountType) {
            this.discountType = discountType;
        }

        public Double getMinLine() {
            return minLine;
        }

        public void setMinLine(Double minLine) {
            this.minLine = minLine;
        }

        public Integer getPercent() {
            return percent;
        }

        public void setPercent(Integer percent) {
            this.percent = percent;
        }

        public Double getReduction() {
            return reduction;
        }

        public void setReduction(Double reduction) {
            this.reduction = reduction;
        }

        public String getFitDays() {
            return fitDays;
        }

        public void setFitDays(String fitDays) {
            this.fitDays = fitDays;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Integer getCreator() {
            return creator;
        }

        public void setCreator(Integer creator) {
            this.creator = creator;
        }
    }
}
