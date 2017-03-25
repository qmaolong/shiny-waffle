package com.covilla.annotation;

import com.covilla.common.LogOptionEnum;

import java.lang.annotation.*;

/**
 * Created by qmaolong on 2017/2/10.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AOPLogAnnotation {
    String description()  default "";
    String remark() default "";
    LogOptionEnum option() default LogOptionEnum.CRUD;
    String displayTitle() default "名称";
    String displayItemFromDataStr() default "name";
    String displayItemFromParameter() default "";
}
