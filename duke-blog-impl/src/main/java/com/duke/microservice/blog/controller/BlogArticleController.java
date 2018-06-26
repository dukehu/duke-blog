package com.duke.microservice.blog.controller;

import com.duke.microservice.blog.api.BlogArticleRestService;
import com.duke.microservice.blog.common.Response;
import com.duke.microservice.blog.service.BlogArticleService;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import io.swagger.annotations.Api;
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

    @Override
    public Response<String> save(
            @RequestParam(value = "blogArticleSetVM", required = false) BlogArticleSetVM blogArticleSetVM) {
        blogArticleService.saveOrUpdate(blogArticleSetVM);
        return Response.ok();
    }

    @RequestMapping(value = "/blog_article/{name}", method = RequestMethod.GET)
    public Response<String> test(@PathVariable(value = "name", required = false) String name) {
        return Response.ok(blogArticleService.selectBlogById(name));
    }
}
