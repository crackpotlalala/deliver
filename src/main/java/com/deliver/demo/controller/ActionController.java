package com.deliver.demo.controller;

import com.deliver.demo.config.webhook.ControllerAnnotation;
import com.deliver.demo.config.webhook.MethodAnnotation;
import com.deliver.demo.entity.WebhookRequest;
import com.deliver.demo.enums.ActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ControllerAnnotation
@Component
public class ActionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionController.class);


    @MethodAnnotation(action = ActionEnum.ACTION1, value = "action1的测试 method")
    public Object action1(WebhookRequest request) {
        LOGGER.info("这是 action controller1--" + request);
        return null;
    }


    @MethodAnnotation(action = ActionEnum.ACTION2, value = "action2的测试 method")
    public Object action2(WebhookRequest request) {
        LOGGER.info("这是 action controller2--" + request);
        return null;
    }
}
