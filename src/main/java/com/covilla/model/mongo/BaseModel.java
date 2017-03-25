package com.covilla.model.mongo;

import com.alibaba.fastjson.annotation.JSONField;
import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.covilla.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jodd.typeconverter.Convert;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * Created by qmaolong on 2016/9/4.
 */
public class BaseModel implements Serializable {
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Field("_id")
    private ObjectId _id;
    @Transient
    private String objectIdStr;
    private Integer dataStatus = 0;

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String transCodeFromId(Integer id){
        Long id1 = (Long.valueOf(id) << 20) + id;
        Long id2 = id1^0x69c78963;
        Long id3 = id2&0xffffffff;
        return id3.toString();
    }

    public static Integer decodeId(String id){
        Long id1 = Convert.toLong(id);
        Long id2 = id1^0x69c78963;
        Long id3 = id2&0xfffff;
        return Convert.toInteger(id3);
    }

    @JSONField(serialize=false,deserialize=false)
    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getObjectIdStr() {
        if(ValidatorUtil.isNotNull(this._id)){
            return this._id.toString();
        }
        return objectIdStr;
    }

    public void setObjectIdStr(String objectIdStr) {
        this.objectIdStr = objectIdStr;
    }
}
