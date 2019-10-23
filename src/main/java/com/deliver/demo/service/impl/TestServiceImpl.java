package com.deliver.demo.service.impl;

import com.deliver.demo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    public static Random random = new Random();

    /**
     * @Async注解中指定线程池名
     * 异步调用的执行任务使用这个线程池中的资源
     * @throws Exception
     */
    @Async("testExecutor")
    public void one() throws Exception {
        log.info("第一个");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        log.info("完成一，耗时：" + (end - start) + "毫秒");
    }

    @Async("testExecutor")
    public void two() throws Exception {
        log.info("第二个");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        log.info("完成二，耗时：" + (end - start) + "毫秒");
    }

    @Async("testExecutor")
    public void three() throws Exception {
        log.info("第三个");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        log.info("完成三，耗时：" + (end - start) + "毫秒");
    }
}
