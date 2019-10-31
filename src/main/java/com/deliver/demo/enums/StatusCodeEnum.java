package com.deliver.demo.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum StatusCodeEnum {
    UNDEFINED_ACTION(00001, "未定义的action");

    private static final Map<Integer, StatusCodeEnum> codeMapIndex = new HashMap<>();

    static {
        for (StatusCodeEnum item : EnumSet.allOf(StatusCodeEnum.class)) {
            codeMapIndex.put(item.getCode(), item);
        }
    }

    private int code;
    private String desc;

    StatusCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public static StatusCodeEnum lookup(int code) {
        return codeMapIndex.get(code);
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
