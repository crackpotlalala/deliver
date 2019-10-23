package com.deliver.demo.config.dataSource;

import com.deliver.demo.enums.DataSourceNames;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定 dataBase 的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataSource {
    String value() default DataSourceNames.FIRST;
}
