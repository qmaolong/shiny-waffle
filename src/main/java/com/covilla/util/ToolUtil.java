package com.covilla.util;

import com.covilla.common.BusinessTypeEnum;
import com.covilla.common.Config;
import com.covilla.common.RoleEnum;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qmaolong on 2016/8/30.
 */
public class ToolUtil {
    /**
     * 生成版本时间戳
     */
    public static final String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    public final static String getVersionTimestamp(){
        return TIMESTAMP;
    }
    //截取单个字符
    public static String subString(String string, int index){
        if(ValidatorUtil.isNull(string) || index < 1 || index > string.length()){
            return "";
        }
        return string.substring(index - 1, index);
    }

    public static String getEnvironment(){
        return Config.SYS_DEV_MODE;
    }

    public static void main(String[] args){
        System.out.print(subString("abc", 4));
    }

    public String getCardOptionName(Integer code){
        return BusinessTypeEnum.findNameByType(code);
    }

    public String getRoleNameByCode(String code){
        return RoleEnum.findNameByCode(code);
    }
}
