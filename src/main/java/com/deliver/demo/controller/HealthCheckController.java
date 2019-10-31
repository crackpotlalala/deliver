package com.deliver.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务启动的健康检查
 */
@RestController
@RequestMapping("/check")
public class HealthCheckController {

    @RequestMapping(value = "/healthCheck", method = RequestMethod.GET)
    public String healthCheck() {
        return "ok";
    }
}