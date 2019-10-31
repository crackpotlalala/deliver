package com.deliver.demo.config.webhook;

import com.deliver.demo.entity.WebhookRequest;
import com.deliver.demo.enums.ActionEnum;
import com.deliver.demo.enums.StatusCodeEnum;
import com.deliver.demo.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 * 接收 webhook 请求并根据参数 action 进行转发
 */
@Slf4j
@RestController
@RequestMapping("/webHook")
public class WebHookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebHookController.class);

    @RequestMapping(value = "/handler", method = RequestMethod.POST)
    public String handler(@RequestBody WebhookRequest request) {
        LOGGER.info("webhook handler receive request");
        //业务分发
        try {
            MethodObj ref = MethodCache.getMethod(ActionEnum.getEnumByName(request.getAction()));
            if (ActionEnum.getEnumByName(request.getAction()) == null || ref == null) {
                throw new ServiceException(StatusCodeEnum.UNDEFINED_ACTION.getDesc(), StatusCodeEnum.UNDEFINED_ACTION.getCode());
            }
            ref.getMethod().invoke(ref.getObject(), request);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return "send success";
    }

}
