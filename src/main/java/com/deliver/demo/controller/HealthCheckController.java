package com.deliver.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 *
 * @author zyy
 */
@Slf4j
@RestController
@RequestMapping("/check")
public class HealthCheckController {

    @RequestMapping(value = "/healthCheck", method = RequestMethod.GET)
    public String healthCheck() {
        return "ok";
    }
}