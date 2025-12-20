package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@Author: 潜月
 *@Date: 2025/12/19 16:51
 *@Param:
 *@Return:
 *@Description:
 **/
@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
