package com.duke.microservice.blog.web.controller;

import com.duke.framework.web.Response;
import com.duke.microservice.blog.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created duke on 2018/7/2
 */
@Api(description = "文件上传接口文档")
@RestController
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "上传文件的服务id", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "md5", value = "文件md5值", dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "单文件上传", notes = "单文件上传")
    @RequestMapping(value = "/nologin/upload", method = RequestMethod.POST)
//    @PreAuthorize("hasAuthority('admin') or hasAuthority('storage_upload_fileUpload')")
    public Response<String> fileUpload(@RequestParam(value = "file", required = false) MultipartFile file,
                                       @RequestParam(value = "serviceId", required = false) String serviceId,
                                       @RequestParam(value = "md5", required = false) String md5,
                                       HttpServletRequest request) {
        String filePath = fileUploadService.fileUpload(file, serviceId, md5);
        String url = request.getScheme() +
                "://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                "/api/blog" +
                request.getContextPath() +
                filePath;
        return Response.ok(url);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "上传文件的服务id", dataType = "string", paramType = "query", required = true)
    })
    @ApiOperation(value = "多文件上传", notes = "多文件上传")
    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
    public Response<String> fileBatchUpload(@RequestParam(value = "serviceId", required = false) String serviceId,
                                            HttpServletRequest request) {

        fileUploadService.fileBatchUpload(serviceId, request);
        return Response.ok();
    }

}
