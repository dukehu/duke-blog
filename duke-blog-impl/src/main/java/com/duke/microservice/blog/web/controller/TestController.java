package com.duke.microservice.blog.web.controller;

import com.duke.microservice.blog.common.Response;
import com.duke.microservice.blog.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created pc on 2018/6/27
 */
@RestController
@Api(description = "测试接口文档")
public class TestController {

    @Autowired
    private TestService testService;

    @ApiOperation(value = "测试自定义异常处理", notes = "测试自定义异常处理")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Response<String> testCustomExceptionHandler(@RequestParam(value = "test", required = false) String test) {
        testService.testCustomExceptionHandler(test);
        return Response.ok();
    }

}
