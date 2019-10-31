package com.deliver.demo.config.webhook;

import com.deliver.demo.enums.ActionEnum;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * action-method 映射关系存放
 */
public class MethodCache {
    private static final Map<ActionEnum, MethodObj> map = new LinkedHashMap<>();

    public static void setMethod(ActionEnum value, MethodObj obj) {
        map.put(value, obj);
    }

    public static MethodObj getMethod(ActionEnum value) {
        return map.get(value);
    }
}
