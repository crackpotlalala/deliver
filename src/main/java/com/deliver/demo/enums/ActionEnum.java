package com.deliver.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定义request 分发接口枚举
 */
@Getter
@AllArgsConstructor
public enum ActionEnum {
    ACTION1("action1"),
    ACTION2("action2");

    //handler 名称
    private String name;

    public static ActionEnum getEnumByName(String name) {
        for (ActionEnum action : ActionEnum.values()) {
            if (action.getName().equals(name)) {
                return action;
            }
        }
        return null;
    }
}
