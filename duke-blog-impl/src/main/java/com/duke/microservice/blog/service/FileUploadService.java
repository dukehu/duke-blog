package com.duke.microservice.blog.service;

import com.duke.framework.security.AuthUserDetails;
import com.duke.framework.utils.FileUtils;
import com.duke.framework.utils.SecurityUtils;
import com.duke.framework.utils.ValidationUtils;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.BlogProperties;
import com.duke.microservice.blog.domain.basic.Storage;
import com.duke.microservice.blog.mapper.basic.StorageMapper;
import com.duke.microservice.blog.mapper.extend.StorageExtendMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created duke on 2018/7/2
 */
@Service
@Transactional
public class FileUploadService {

    private Logger log = LoggerFactory.getLogger(FileUploadService.class);

    @Autowired
    private BlogProperties blogProperties;

    @Autowired
    private StorageMapper storageMapper;

    @Autowired
    private StorageExtendMapper storageExtendMapper;

    /**
     * 文件上传
     *
     * @param multipartFile 文件对象
     * @param serviceId     服务id
     */
    public String fileUpload(MultipartFile multipartFile, String serviceId, String md5) {
        // todo 获得用户信息
        AuthUserDetails authUserDetails = SecurityUtils.getCurrentUserInfo();
        String userId = authUserDetails.getUserId();
        ValidationUtils.notEmpty(multipartFile, "multipartFile", "文件不可为空！");
        ValidationUtils.notEmpty(serviceId, "serviceId", "服务id不可为空！");

        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件名称，如time.png
        String originalFileName = multipartFile.getOriginalFilename();
        // 街截取文件后缀
        String fileSuffix = FileUtils.getFileSuffix(originalFileName);
        // 文件名称，去后缀
        String fileName = FileUtils.getFileName(originalFileName);
        // todo 校验文件名称长度

        String relativeFilePath = FileUtils.getRelativeFilePath(serviceId);
        File file = new File(blogProperties.getStorage().getPath() + relativeFilePath);

        if (!file.exists()) {
            boolean mkdirsed = file.mkdirs();
            if (!mkdirsed) {
                // todo 抛出异常，创建文件夹失败
            }
        }
        String id = UUID.randomUUID().toString();
        String path = blogProperties.getStorage().getPath() + relativeFilePath + "/" + id + "." + fileSuffix;

        // 上传文件并将文件基本信息保存到数据库中
        try {
            log.info("文件开始开始，上传路径：" + path);
            multipartFile.transferTo(new File(path));
            log.info("文件上传结束");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Date date = new Date();

        // 保存文件基本信息
        Storage storage = new Storage();
        storage.setId(id);
        storage.setName(fileName);
        storage.setSuffix(fileSuffix);
        storage.setServiceId(serviceId);
        storage.setPath(path);
        storage.setSize(new Long(fileSize).intValue());
        storage.setStatus(BlogConstants.FILE_STATUS.EXIST.getCode());
        storage.setUserId(userId);
        md5 = null == md5 ? "" : md5;
        storage.setMd5(md5);
        // todo 处理文件类型
        storage.setType(1);
        storage.setUploadTime(date);
        storage.setDeleteTime(date);
        storageMapper.insert(storage);
        return relativeFilePath + "/" + id + "." + fileSuffix;
    }

    /**
     * 多文件上传
     *
     * @param serviceId 服务id
     * @param request   请求
     */
    public void fileBatchUpload(String serviceId, HttpServletRequest request) {
        // todo 获得用户信息
        String userId = "duke";
        ValidationUtils.notEmpty(serviceId, "serviceId", "服务id不可为空！");
        // 创建一个多分解的容器
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        // 设置编码
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        // 设置文件的最大上传大小
        commonsMultipartResolver.setMaxUploadSize(blogProperties.getStorage().getMaxUploadSize());

        try {
            commonsMultipartResolver.resolveMultipart(request);
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(e.getMaxUploadSize(), e);
        }

        //判断是否有文件上传
        if (commonsMultipartResolver.isMultipart(request)) {
            // 将request转换成多分解请求
            MultipartHttpServletRequest multipartHttpServletRequest
                    = commonsMultipartResolver.resolveMultipart(request);
        }

        // todo 多文件上传先不做，需要用的时候再去处理
    }

    /**
     * 根据md5值判断资源文件是否存在，如果存在则直接复制一条数据，返回true，直接上传
     *
     * @param serviceId 服务id
     * @param name      文件名称
     * @param md5       文件md5值
     * @return Boolean
     */
    @Transactional(readOnly = true)
    public Boolean checkMD5(String serviceId, String name, String md5) {
        ValidationUtils.notEmpty(md5, "md5", "文件MD5值不能为空！");
        List<Storage> storageList = storageExtendMapper.selectByMD5(md5);
        if (!CollectionUtils.isEmpty(storageList)) {
            // 复制一条数据
            Date date = new Date();
            Storage storage = storageList.get(0);
            storage.setId(UUID.randomUUID().toString());
            storage.setName(name);
            storage.setUploadTime(date);
            storage.setDeleteTime(date);
            storage.setStatus(BlogConstants.FILE_STATUS.EXIST.getCode());
            storageMapper.insert(storage);
            return true;
        } else {
            return false;
        }
    }
}
