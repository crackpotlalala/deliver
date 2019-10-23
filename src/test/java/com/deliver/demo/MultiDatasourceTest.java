package com.deliver.demo;

import com.deliver.demo.service.StudentService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 多数据源注解 测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiDatasourceTest {

    private static Gson gson = new Gson();

    @Autowired
    private StudentService studentService;

    @Test
    public void testDataSource() {
        System.out.println("data from dataSource1:");
        System.out.println(gson.toJson(studentService.getById1(1)));
        System.out.println("data from dataSource2:");
        System.out.println(gson.toJson(studentService.getById2(1)));

    }


}
