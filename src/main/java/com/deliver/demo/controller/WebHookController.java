package com.deliver.demo.controller;

import com.deliver.demo.entity.WebhookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * webhook接收接口测试
 * <p>
 * webhook requestBody 格式
 * {
 * "context": {
 * "location": "北京"
 * },
 * "action": "get_weather",
 * "dialog_node_name": "get_weather",
 * "session_id": "xxxxxxx",
 * "query_id": "xxxxxxxxx"
 * }
 */
@RestController
@RequestMapping("/webHook")
public class WebHookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @ResponseBody
    @RequestMapping(value = "/action1", method = RequestMethod.POST)
    public Object action1(@RequestBody WebhookRequest request) {
        LOGGER.info("this is action controller1--" + request);
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/action2", method = RequestMethod.POST)
    public Object action2(@RequestBody WebhookRequest request) {
        LOGGER.info("this is action controller2--" + request);
        return null;
    }

}
