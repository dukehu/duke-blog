package com.duke.microservice.blog.web.controller;

import com.duke.framework.CoreConstants;
import com.duke.framework.web.Response;
import com.duke.microservice.blog.api.BlogLabelRestService;
import com.duke.microservice.blog.service.BlogLabelService;
import com.duke.microservice.blog.vm.BlogLabelVM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
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

    @ApiOperation(value = "列表（不需要登陆）", notes = "列表")
    @Override
    public Response<List<BlogLabelVM>> noLoginSelect() {
        return Response.ok(blogLabelService.select());
    }

    @ApiOperation(value = "列表（需要登陆）", notes = "列表")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_label_select')")
    @Override
    public Response<List<BlogLabelVM>> select() {
        return Response.ok(blogLabelService.select());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", dataType = "string", paramType = "query", required = true)
    })
    @ApiOperation(value = "新增", notes = "新增")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_label_save')")
    @Override
    public Response<String> save(String name) {
        blogLabelService.saveOrUpdate(null, name, CoreConstants.SAVE);
        return Response.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "string", paramType = "query", required = true)
    })
    @ApiOperation(value = "修改", notes = "修改")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_label_update')")
    @Override
    public Response<String> update(String id, String name) {
        blogLabelService.saveOrUpdate(id, name, CoreConstants.UPDATE);
        return Response.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string", paramType = "path", required = true)
    })
    @ApiOperation(value = "删除", notes = "删除")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('blog_blog_label_delete')")
    @Override
    public Response<String> delete(@PathVariable String id) {
        blogLabelService.delete(id);
        return Response.ok();
    }
}
