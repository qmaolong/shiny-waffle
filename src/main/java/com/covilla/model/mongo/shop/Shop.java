package com.covilla.model.mongo.shop;

import com.covilla.model.mongo.BaseModel;
import com.covilla.model.mongo.card.CardBatch;
import com.covilla.model.mongo.card.CardPublish;
import com.covilla.model.mongo.food.Category;
import com.covilla.model.mongo.food.Taste;
import com.covilla.model.mongo.food.Unit;
import com.covilla.model.mongo.organization.Section;
import com.covilla.model.mongo.printer.PrintScheme;
import com.covilla.model.mongo.printer.TicketForm;
import com.covilla.model.mongo.purchase.Area;
import com.covilla.model.mongo.setting.Payment;
import com.covilla.model.mongo.setting.SmsGateway;
import com.covilla.model.mongo.setting.WxSupport;
import com.covilla.model.mongo.user.Cashier;
import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.covilla.util.ContentUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.filter.QueryFilter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/24.
 */
@Document(collection = "shop")
public class Shop extends BaseModel{
    @Field("id")
    private Integer id;
    private String name;
    private String address;
    private Date expired;
    private String apiKey;
    private List<String> authKeys;
    private List<Cashier> cashiers;
    private List<Category> categories;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId owner;
    private Boolean superFlag;
    private Integer manager;
    private Date openDate;
    private String ownerName;
    @Field("tel")
    private String phone;
    private List<Unit> units;
    private List<DeskCat> deskCat;
    @Field("desk")
    private List<Room> rooms;
    @Field("taste")
    private List<String> tastes;
    @Field("users")
    private List<Section> sections;
    private Double longitude;
    private Double latitude;
    @Transient
    private List<Taste> tastVo;
    @Field("super")
    private ObjectId superShop;
    private Date createTime;
    private String creator;
    @Transient
    private String ownerId;
    @Field("printer")
    private List<Printer> printers;
    private List<TicketForm> ticketForms = new ArrayList<TicketForm>();
    private List<TicketForm> kitchenForms = new ArrayList<TicketForm>();
    private List<TicketForm> controlForms = new ArrayList<TicketForm>();
    @Transient
    private String authKey;
    @Field("printerPolicy")
    private List<PrintScheme> printSchemes;
    @Field("payment")
    private List<Payment> payments;
    @Field("cardBatch")
    private List<CardBatch> cardBatches;
    private List<CardPublish> ticketPublish;//卡券发放规则
    private List<QueryFilter> statisticModules;//数据统计模板
    @Transient
    private String encodeId;
    private List<String> advertise;//广告位图片
    private byte[] icKey;
    private WxSupport wxSupport;
    private SmsGateway smsGateway;

    private List<Area> areas;//经营区域

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public void setCashiers(List<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public ObjectId getOwner() {
        return owner;
    }

    public void setOwner(ObjectId owner) {
        this.owner = owner;
    }

    public Boolean getSuperFlag() {
        return superFlag;
    }

    public void setSuperFlag(Boolean superFlag) {
        this.superFlag = superFlag;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    /*public String getCode() {
        return transCodeFromId(this.id);
    }

    public void setCode(String code) {
        this.code = code;
    }*/

    public List<DeskCat> getDeskCat() {
        return deskCat;
    }

    public void setDeskCat(List<DeskCat> deskCat) {
        this.deskCat = deskCat;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<String> getTastes() {
        return tastes;
    }

    public void setTastes(List<String> tastes) {
        this.tastes = tastes;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public List<Taste> getTastVo() {
        if(ValidatorUtil.isNotNull(this.tastes)){
            List<Taste> result = new ArrayList<Taste>();
            for(int i=0; i<this.tastes.size(); i++){
                Taste taste = new Taste();
                taste.setId(i + 1);
                taste.setName(this.tastes.get(i));
                result.add(taste);
            }
            return result;
        }
        return tastVo;
    }

    public void setTastVo(List<Taste> tastVo) {
        this.tastVo = tastVo;
    }

    public ObjectId getSuperShop() {
        return superShop;
    }

    public void setSuperShop(ObjectId superShop) {
        this.superShop = superShop;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<Printer> getPrinters() {
        return printers;
    }

    public void setPrinters(List<Printer> printers) {
        this.printers = printers;
    }

    public List<TicketForm> getTicketForms() {
        return ticketForms;
    }

    public void setTicketForms(List<TicketForm> ticketForms) {
        this.ticketForms = ticketForms;
    }

    public List<TicketForm> getKitchenForms() {
        return kitchenForms;
    }

    public void setKitchenForms(List<TicketForm> kitchenForms) {
        this.kitchenForms = kitchenForms;
    }

    public List<TicketForm> getControlForms() {
        return controlForms;
    }

    public void setControlForms(List<TicketForm> controlForms) {
        this.controlForms = controlForms;
    }

    public List<String> getAuthKeys() {
        return authKeys;
    }

    public void setAuthKeys(List<String> authKeys) {
        this.authKeys = authKeys;
    }

    public String getAuthKey() {
        if(ValidatorUtil.isNotNull(authKeys)){
            authKey = "";
            for (String key : authKeys){
                authKey += key + ",";
            }
            authKey = authKey.substring(0, authKey.length()-1);
        }
        return authKey;
    }

    public List<PrintScheme> getPrintSchemes() {
        return printSchemes;
    }

    public void setPrintSchemes(List<PrintScheme> printSchemes) {
        this.printSchemes = printSchemes;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<CardBatch> getCardBatches() {
        return cardBatches;
    }

    public void setCardBatches(List<CardBatch> cardBatches) {
        this.cardBatches = cardBatches;
    }

    public List<CardPublish> getTicketPublish() {
        return ticketPublish;
    }

    public void setTicketPublish(List<CardPublish> ticketPublish) {
        this.ticketPublish = ticketPublish;
    }

    public List<QueryFilter> getStatisticModules() {
        return statisticModules;
    }

    public void setStatisticModules(List<QueryFilter> statisticModules) {
        this.statisticModules = statisticModules;
    }

    public String getEncodeId() {
        if(ValidatorUtil.isNotNull(id)){
            return ContentUtil.encodeId(id);
        }
        return encodeId;
    }

    public void setEncodeId(String encodeId) {
        this.encodeId = encodeId;
    }

    public List<String> getAdvertise() {
        return advertise;
    }

    public void setAdvertise(List<String> advertise) {
        this.advertise = advertise;
    }

    public byte[] getIcKey() {
        return icKey;
    }

    public void setIcKey(byte[] icKey) {
        this.icKey = icKey;
    }

    public WxSupport getWxSupport() {
        return wxSupport;
    }

    public void setWxSupport(WxSupport wxSupport) {
        this.wxSupport = wxSupport;
    }

    public SmsGateway getSmsGateway() {
        return smsGateway;
    }

    public void setSmsGateway(SmsGateway smsGateway) {
        this.smsGateway = smsGateway;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
}
