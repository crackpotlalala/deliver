package com.deliver.demo.config.webhookFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置何种请求 url 会经过WebHookFilter过滤
 * 现配置的过滤目标：http://127.0.0.1:30333/webHook
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean registerFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WebHookFilter());
        registration.addUrlPatterns("/webHook");
        registration.setName("WebHookFilter");
        registration.setOrder(1);
        return registration;
    }

}