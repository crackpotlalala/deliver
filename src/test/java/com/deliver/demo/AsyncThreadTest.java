package com.deliver.demo;

import com.deliver.demo.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 异步线程池测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncThreadTest {

    @Autowired
    private TestService testServiceImpl;

    @Test
    public void AsyncThreadTest(){
        try {
            testServiceImpl.one();
            testServiceImpl.two();
            testServiceImpl.three();

            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
