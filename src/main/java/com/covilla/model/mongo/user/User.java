package com.covilla.model.mongo.user;

import com.covilla.common.RoleEnum;
import com.covilla.model.mongo.BaseModel;
import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.covilla.util.ContentUtil;
import com.covilla.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
@Document
public class User extends BaseModel implements Serializable {
    @Field("id")
    private Integer id;
    private String name;
    private String password;
    private String openId;
    private String salt;
    private String nickName;
    private String headImgUrl;
    private String province;
    private String city;
    private Integer sex;
    private Integer subscribe;
    private Date subscribe_time;
    private String tel;
    private String type;
    private String role;
    private String bindKey;
    private List<ObjectId> manageShops;
    @Transient
    private String roleName;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId creator;
    private Date createTime;
    private String activeCode;
    private Date codeExpireTime;
    @Transient
    private String manageShopStr;
    private String email;
    private String address;
    @Transient
    private String encodeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBindKey() {
        return bindKey;
    }

    public void setBindKey(String bindKey) {
        this.bindKey = bindKey;
    }

    public String getRoleName() {
        return RoleEnum.findNameByCode(this.getRole());
    }

    public List<ObjectId> getManageShops() {
        return manageShops;
    }

    public void setManageShops(List<ObjectId> manageShops) {
        this.manageShops = manageShops;
    }

    public ObjectId getCreator() {
        return creator;
    }

    public void setCreator(ObjectId creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public Date getCodeExpireTime() {
        return codeExpireTime;
    }

    public void setCodeExpireTime(Date codeExpireTime) {
        this.codeExpireTime = codeExpireTime;
    }

    public String getManageShopStr() {
        if(ValidatorUtil.isNotNull(this.manageShops)&&ValidatorUtil.isNull(this.manageShopStr)){
            manageShopStr = "";
            for (ObjectId shopId : manageShops){
                manageShopStr += shopId.toString() + ",";
            }
            manageShopStr = manageShopStr.substring(0, manageShopStr.length() - 1);
        }
        return manageShopStr;
    }

    public void setManageShopStr(String manageShopStr) {
        this.manageShopStr = manageShopStr;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public Date getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(Date subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
