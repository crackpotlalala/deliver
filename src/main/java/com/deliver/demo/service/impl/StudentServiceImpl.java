package com.deliver.demo.service.impl;

import com.deliver.demo.config.dataSource.DataSource;
import com.deliver.demo.entity.Student;
import com.deliver.demo.enums.DataSourceNames;
import com.deliver.demo.mapper.StudentMapper;
import com.deliver.demo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 设置数据源为 db2
     *
     * @param id
     * @return
     */
    @Override
    @DataSource(DataSourceNames.SECOND)
    public Student getById2(int id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    /**
     * @param id
     * @return
     * @Cacheable：缓存
     */
    @Override
    @Cacheable(value = "students", key = "#id")
    public Student getCache(int id) {
        Student student = studentMapper.selectByPrimaryKey(id);
        log.info("第一次查询走数据库...");
        log.info("redis 为 key 为:" + id + "数据做了缓存");
        return student;
    }

    /**
     * @param id
     * @CacheEvict 清除缓存
     */
    @Override
    @CacheEvict(value = "students", key = "#id")
    public void clearCache(int id) {
        log.info("redis 为 key 为:" + id + "数据做了缓存清除");
//        studentMapper.deleteByPrimaryKey(id);
    }

    /**
     * 不加 DataBase注解，默认db1
     *
     * @param id
     * @return
     */
    @Override
    public Student getById1(int id) {
        return studentMapper.selectByPrimaryKey(id);
    }


}
