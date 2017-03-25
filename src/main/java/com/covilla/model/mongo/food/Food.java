package com.covilla.model.mongo.food;

import com.covilla.model.mongo.BaseModel;
import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.covilla.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
@Document
public class Food extends BaseModel {
    public static final Integer TYPE_FOOD = 0;
    public static final Integer TYPE_SUITE = 1;
    private String name;
    private Double price1 = 0.0;
    private Double price2 = 0.0;
    private Double price3 = 0.0;
    @Field("id")
    private String id;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId owner;
    private Double pricem = 0.0;
    private String abc;
    private String cat;
    private Integer type;//类型，0：单品、1：套餐
    @Field("combo")
    private List<OptionContainer> combo;
    @Field("options")
    private List<OptionContainer> options;
    private List<String> printers;
    @Transient
    private String printersStr;
    private String printer1;
    private String printer2;
    private String printer3;
    private String printerControl;
    private String printerPolicy;
    private Date createTime;
    private String unit;
    private String[] property;
    @Field("state")
    private Integer foodState;//0可用，1不可用
    private ObjectId refId;//引用总店的菜品id

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice1() {
        return price1;
    }

    public void setPrice1(Double price1) {
        this.price1 = price1;
    }

    public Double getPrice2() {
        return price2;
    }

    public void setPrice2(Double price2) {
        this.price2 = price2;
    }

    public Double getPrice3() {
        return price3;
    }

    public void setPrice3(Double price3) {
        this.price3 = price3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getOwner() {
        return owner;
    }

    public void setOwner(ObjectId owner) {
        this.owner = owner;
    }

    public Double getPricem() {
        return pricem;
    }

    public void setPricem(Double pricem) {
        this.pricem = pricem;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<OptionContainer> getCombo() {
        return combo;
    }

    public void setCombo(List<OptionContainer> combo) {
        this.combo = combo;
    }

    public List<OptionContainer> getOptions() {
        return options;
    }

    public void setOptions(List<OptionContainer> options) {
        this.options = options;
    }

    public List<String> getPrinters() {
        return printers;
    }

    public void setPrinters(List<String> printers) {
        this.printers = printers;
    }

    public String getPrintersStr() {
        if(ValidatorUtil.isNotNull(printers)){
            printersStr = "";
            for (String printer : printers){
                printersStr += printer + ",";
            }
            printersStr = printersStr.substring(0, printersStr.length() - 1);
        }
        return printersStr;
    }

    public void setPrintersStr(String printersStr) {
        this.printersStr = printersStr;
    }

    public String getPrinter1() {
        return printer1;
    }

    public void setPrinter1(String printer1) {
        this.printer1 = printer1;
    }

    public String getPrinter2() {
        return printer2;
    }

    public void setPrinter2(String printer2) {
        this.printer2 = printer2;
    }

    public String getPrinter3() {
        return printer3;
    }

    public void setPrinter3(String printer3) {
        this.printer3 = printer3;
    }

    public String getPrinterControl() {
        return printerControl;
    }

    public void setPrinterControl(String printerControl) {
        this.printerControl = printerControl;
    }

    public String getPrinterPolicy() {
        return printerPolicy;
    }

    public void setPrinterPolicy(String printerPolicy) {
        this.printerPolicy = printerPolicy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String[] getProperty() {
        return property;
    }

    public void setProperty(String[] property) {
        this.property = property;
    }

    public Integer getFoodState() {
        return foodState;
    }

    public void setFoodState(Integer foodState) {
        this.foodState = foodState;
    }

    public ObjectId getRefId() {
        return refId;
    }

    public void setRefId(ObjectId refId) {
        this.refId = refId;
    }
}
