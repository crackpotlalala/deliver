package com.deliver.demo;

import com.deliver.demo.entity.Student;
import com.deliver.demo.mapper.StudentMapper;
import com.deliver.demo.service.StudentService;
import com.deliver.demo.utils.RedisUtils;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * redis + springCache 缓存注解测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootCacheApplicationTests {

    private static Gson gson = new Gson();

    @Autowired
    StringRedisTemplate stringRedisTemplate; //操作 k-v 字符串


    @Autowired
    RedisTemplate redisTemplate;  //k- v 都是对象

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentService studentService;


    /**
     * redis 常见
     * String(字符串) List(列表) Set(集合) Hash(散列) ZSet(有序集合)
     */

    @Test
    public void testRedisString() {
        stringRedisTemplate.opsForValue().append("StringKey", "字符串数值");
        String value = stringRedisTemplate.opsForValue().get("StringKey");
        System.out.println(value);
    }

    @Test
    public void testRedisObject() {
        Student student = studentMapper.selectByPrimaryKey(1);
        redisTemplate.opsForValue().set("student", student);

    }

    @Test
    public void getSpringCache() {
        System.out.println(gson.toJson(studentService.getCache(1)));
    }

    @Test
    public void clearSpringCache() {
        studentService.clearCache(1);
    }

    @Test
    public void testRedisUtils() {
        RedisUtils.setStringValue("test", "testString");
        System.out.print("get value of :" +
                RedisUtils.getStringValue("test"));
    }


}
