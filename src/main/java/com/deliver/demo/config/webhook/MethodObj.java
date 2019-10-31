package com.deliver.demo.config.webhook;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class MethodObj {
    private Method method;
    private Object object;
}
