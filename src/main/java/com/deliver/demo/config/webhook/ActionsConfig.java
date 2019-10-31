package com.deliver.demo.config.webhook;


import com.deliver.demo.enums.ActionEnum;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * 初始化 MethodCache
 */
@Service
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class ActionsConfig implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        this.loadAnnotation();
    }

    public void loadAnnotation() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(ControllerAnnotation.class);
        if (map == null) {
            return;
        }
        for (Object obj : map.values()) {
            for (Method method : obj.getClass().getMethods()) {
                if (method.isAnnotationPresent(MethodAnnotation.class)) {
                    ActionEnum cmd = method.getAnnotation(MethodAnnotation.class).action();
                    MethodObj ref = new MethodObj();
                    ref.setMethod(method);
                    ref.setObject(obj);
                    MethodCache.setMethod(cmd, ref);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

