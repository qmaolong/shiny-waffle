package com.covilla.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 验证码实体
 * Created by qmaolong on 2017/3/10.
 */
public class VerifyCode implements Serializable {
    private final static Long serialVersionUID = -1L;
    private String tel;
    private String code;
    private TypeEnum type;
    private Date expireTime;

    public VerifyCode(String tel, String code, TypeEnum type) {
        this.tel = tel;
        this.code = code;
        this.type = type;
    }

    public enum TypeEnum {
        REGIST_MERCHANT("regist_merchant", "商家注册");
        private String name;
        private String desc;

        TypeEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
