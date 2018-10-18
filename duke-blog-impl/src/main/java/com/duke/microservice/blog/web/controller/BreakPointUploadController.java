package com.duke.microservice.blog.web.controller;

import com.duke.framework.web.Response;
import com.duke.microservice.blog.api.BreakPointUploadRestService;
import com.duke.microservice.blog.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created duke on 2018/7/17
 */
@Api(description = "断点上传接口文档")
@RestController
public class BreakPointUploadController implements BreakPointUploadRestService {

    @Autowired
    private FileUploadService fileUploadService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "服务id", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "name", value = "文件名称", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "md5", value = "文件md5值", dataType = "string", paramType = "query", required = true)
    })
    @ApiOperation(value = "根据md5值判断资源文件是否存在", notes = "根据md5值判断资源文件是否存在")
    @Override
    public Response<Boolean> checkMD5(String serviceId, String name, String md5) {
        return Response.ok(fileUploadService.checkMD5(serviceId, name, md5));
    }


    

}
