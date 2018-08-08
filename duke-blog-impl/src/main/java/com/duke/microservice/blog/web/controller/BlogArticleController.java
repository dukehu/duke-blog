package com.duke.microservice.blog.web.controller;

import com.duke.framework.security.AuthUserDetails;
import com.duke.framework.utils.SecurityUtils;
import com.duke.framework.web.Response;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.api.BlogArticleRestService;
import com.duke.microservice.blog.service.BlogArticleService;
import com.duke.microservice.blog.vm.BlogArticleDetailVM;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created duke on 2018/6/23
 */
@Api(description = "博文接口文档")
@RestController
public class BlogArticleController implements BlogArticleRestService {

    @Autowired
    private BlogArticleService blogArticleService;

    @ApiOperation(value = "发表", notes = "发表")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_article_publish')")
    @Override
    public Response<String> publish(BlogArticleSetVM blogArticleSetVM) {
        blogArticleService.setBlogArticle(blogArticleSetVM, BlogConstants.BLOG_STATUS.PULISHED.getCode(), null);
        return Response.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "string", paramType = "path", required = true)
    })
    @ApiOperation(value = "修改", notes = "修改")
    @Override
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_article_update')")
    public Response<String> update(@PathVariable(value = "id", required = false) String id,
                                   BlogArticleSetVM blogArticleSetVM) {
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "string", paramType = "path")
    })
    @ApiOperation(value = "存草稿", notes = "存草稿")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_article_draft')")
    @Override
    public Response<String> draft(@PathVariable(value = "id", required = false) String id,
                                  BlogArticleSetVM blogArticleSetVM) {
        return Response.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "string", paramType = "path", required = true)
    })
    @ApiOperation(value = "删除", notes = "删除")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_article_delete')")
    @Override
    public Response<String> delete(@PathVariable(value = "id", required = false) String id) {
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "string", paramType = "path", required = true)
    })
    @ApiOperation(value = "详情", notes = "详情")
    @PreAuthorize("hasAuthority('admin')")
    @Override
    public Response<BlogArticleDetailVM> select(@PathVariable(value = "id", required = false) String id) {
        return Response.ok(blogArticleService.selectById(id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "起始页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "列表", notes = "列表")
    @Override
    public Response<PageInfo<BlogArticleDetailVM>> select(Integer page, Integer size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Response.ok(blogArticleService.select(page, size));
    }

    @RequestMapping(value = "/blog_article/test", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('admin')")
    public Response<AuthUserDetails> test() {
        AuthUserDetails authUserDetails = SecurityUtils.getCurrentUserInfo();

        return Response.ok(authUserDetails);
    }
}
