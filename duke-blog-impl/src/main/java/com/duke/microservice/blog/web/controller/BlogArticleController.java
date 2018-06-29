package com.duke.microservice.blog.web.controller;

import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.api.BlogArticleRestService;
import com.duke.microservice.blog.common.Response;
import com.duke.microservice.blog.service.BlogArticleService;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import com.github.pagehelper.PageInfo;
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

    @ApiOperation(value = "发表", notes = "发表")
    @Override
    public Response<String> publish(BlogArticleSetVM blogArticleSetVM) {
        blogArticleService.setBlogArticle(blogArticleSetVM, BlogConstants.BLOG_STATUS.PULISHED.getCode(), null);
        return Response.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "string", paramType = "path")
    })
    @ApiOperation(value = "存草稿", notes = "存草稿")
    @Override
    public Response<String> draft(@PathVariable(value = "id", required = false) String id,
                                  BlogArticleSetVM blogArticleSetVM) {
        return Response.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "string", paramType = "path", required = true)
    })
    @ApiOperation(value = "存草稿", notes = "存草稿")
    @Override
    public Response<String> delete(@PathVariable(value = "id", required = false) String id) {
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "string", paramType = "path", required = true)
    })
    @ApiOperation(value = "详情", notes = "详情")
    @Override
    public Response<BlogArticleSetVM> select(@PathVariable(value = "id", required = false) String id) {
        return null;
    }

    @ApiOperation(value = "列表", notes = "列表")
    @Override
    public Response<PageInfo<BlogArticleSetVM>> select() {
        return null;
    }
}
