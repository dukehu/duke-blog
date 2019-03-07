package com.duke.microservice.blog.web.controller;

import com.duke.framework.CoreConstants;
import com.duke.framework.web.Response;
import com.duke.microservice.blog.api.BlogArticleRestService;
import com.duke.microservice.blog.service.BlogArticleService;
import com.duke.microservice.blog.vm.BlogArticleDetailVM;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created duke on 2018/6/23
 */
@Api(description = "博文接口文档")
@RestController
public class BlogArticleController implements BlogArticleRestService {

    @Autowired
    private BlogArticleService blogArticleService;

    @ApiOperation(value = "写文章", notes = "写文章")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_article_publish')")
    @Override
    public Response<String> publish(BlogArticleSetVM blogArticleSetVM) {
        blogArticleService.setBlogArticle(blogArticleSetVM, null, CoreConstants.SAVE);
        return Response.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "string", paramType = "path", required = true)
    })
    @ApiOperation(value = "修改文章", notes = "修改文章")
    @Override
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_article_update')")
    public Response<String> update(@PathVariable String id,
                                   BlogArticleSetVM blogArticleSetVM) {
        blogArticleService.setBlogArticle(blogArticleSetVM, id, CoreConstants.UPDATE);
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
    @ApiOperation(value = "详情（不需要登陆）", notes = "详情")
    @Override
    public Response<BlogArticleDetailVM> selectByIdNologin(@PathVariable(value = "id", required = false) String id) {
        return Response.ok(blogArticleService.selectById(id, false));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "string", paramType = "path", required = true)
    })
    @ApiOperation(value = "详情（不需要登陆）", notes = "详情")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_article_selectById')")
    @Override
    public Response<BlogArticleDetailVM> selectById(@PathVariable(value = "id", required = false) String id) {
        return Response.ok(blogArticleService.selectById(id, true));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "起始页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "tag", value = "标签", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类别", dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "列表（不需要登陆）", notes = "列表")
    @Override
    public Response<PageInfo<BlogArticleDetailVM>> selectNologin(Integer page, Integer size, String tag, String type) {
        return Response.ok(blogArticleService.select(page, size, tag, type, false));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "起始页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "列表", notes = "列表")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_article_select')")
    @Override
    public Response<PageInfo<BlogArticleDetailVM>> select(Integer page, Integer size) {
        return Response.ok(blogArticleService.select(page, size, null, null, true));
    }

    @ApiOperation(value = "最新文章推荐", notes = "最新文章推荐")
    @Override
    public Response<List<BlogArticleDetailVM>> latestRecommendedArticles() {
        return Response.ok(blogArticleService.latestRecommendedArticles());
    }

    @ApiOperation(value = "文章归档查询", notes = "文章归档查询")
    @Override
    public Response<Map<Integer, List<BlogArticleDetailVM>>> archiveQuery() {
        return Response.ok(blogArticleService.archiveQuery());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string", paramType = "path", required = true),
    })
    @ApiOperation(value = "下载pdf", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "/nologin/blog_article/download/pdf/{id}", method = RequestMethod.POST)
    public void download(@PathVariable(value = "id", required = false) String id,
                         HttpServletResponse response) {
        blogArticleService.downloadPdfByArticleId(id, response);
    }
}
