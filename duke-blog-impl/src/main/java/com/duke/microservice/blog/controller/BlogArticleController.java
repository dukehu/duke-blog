package com.duke.microservice.blog.controller;

import com.duke.microservice.blog.api.BlogArticleRestService;
import com.duke.microservice.blog.common.Response;
import com.duke.microservice.blog.service.BlogArticleService;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created duke on 2018/6/23
 */
@Api(description = "博文接口文档")
@RestController
public class BlogArticleController implements BlogArticleRestService {

    @Autowired
    private BlogArticleService blogArticleService;

    @ApiOperation(value = "新增博文", notes = "新增博文")
    @Override
    public Response<String> save(BlogArticleSetVM blogArticleSetVM) {
        blogArticleService.saveOrUpdate(blogArticleSetVM);
        return Response.ok();
    }
}
