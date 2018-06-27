package com.duke.microservice.blog.service;

import org.springframework.stereotype.Service;

/**
 * Created pc on 2018/6/27
 */
@Service
public class TestService {

    public void testCustomExceptionHandler(String test) {
        if (test == null) {
            throw new RuntimeException("值不能为空！");
        }
    }
}
