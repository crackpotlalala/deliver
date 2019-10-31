package com.deliver.demo.config.webhook;

import com.deliver.demo.enums.ActionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解需要被DistributeController 分发请求的方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MethodAnnotation {

    ActionEnum action();

    String value();
}
