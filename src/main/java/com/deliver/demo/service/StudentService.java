package com.deliver.demo.service;

import com.deliver.demo.entity.Student;

public interface StudentService {

    Student getById1(int id);

    Student getById2(int id);

    Student getCache(int id);

    void clearCache(int id);

}
