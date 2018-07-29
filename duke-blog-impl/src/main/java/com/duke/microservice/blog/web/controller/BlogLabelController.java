package com.duke.microservice.blog.web.controller;

import com.duke.framework.web.Response;
import com.duke.microservice.blog.api.BlogLabelRestService;
import com.duke.microservice.blog.service.BlogLabelService;
import com.duke.microservice.blog.vm.BlogLabelVM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created duke on 2018/6/29
 */
@Api(description = "博文标签接口文档")
@RestController
public class BlogLabelController implements BlogLabelRestService {

    @Autowired
    private BlogLabelService blogLabelService;

    @ApiOperation(value = "列表", notes = "列表")
    @Override
    public Response<List<BlogLabelVM>> select() {
        return Response.ok(blogLabelService.select());
    }
}
