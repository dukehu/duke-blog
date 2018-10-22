package com.duke.microservice.blog.web.controller;

import com.duke.microservice.blog.service.FileShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created duke on 2018/10/19
 */
@Api(description = "文件预览接口文档")
@RestController
public class FileShowController {

    @Autowired
    private FileShowService fileShowService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文件路径", dataType = "string", paramType = "query", required = true),
    })
    @ApiOperation(value = "文件预览", notes = "文件预览")
    @RequestMapping(value = "/show/img", method = RequestMethod.GET)
    public void showImage(@RequestParam(value = "path", required = false) String path, HttpServletResponse response) {
        fileShowService.showImage(path, response);
    }
}
