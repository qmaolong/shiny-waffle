package com.covilla.vo.card;

import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;

/**
 * 会员卡过滤模型
 * Created by qmaolong on 2017/2/19.
 */
public class CardFilterVo {
    private Integer medium;
    private String memberName;
    private String sex;
    private String tel;
    private Integer birthMonth;
    private Integer birthDay;
    private String tagId;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId cardTypeId;
    private Integer cardState;
    private Boolean isPublic;
    private String tagOrMemberName;//tagId或者微会员名

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(Integer birthMonth) {
        this.birthMonth = birthMonth;
    }

    public Integer getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Integer birthDay) {
        this.birthDay = birthDay;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public ObjectId getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(ObjectId cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public Integer getMedium() {
        return medium;
    }

    public void setMedium(Integer medium) {
        this.medium = medium;
    }

    public Integer getCardState() {
        return cardState;
    }

    public void setCardState(Integer cardState) {
        this.cardState = cardState;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getTagOrMemberName() {
        return tagOrMemberName;
    }

    public void setTagOrMemberName(String tagOrMemberName) {
        this.tagOrMemberName = tagOrMemberName;
    }
}
