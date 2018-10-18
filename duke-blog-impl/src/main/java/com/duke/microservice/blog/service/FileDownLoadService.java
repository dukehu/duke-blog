package com.duke.microservice.blog.service;

import com.duke.framework.exception.BusinessException;
import com.duke.framework.utils.ValidationUtils;
import com.duke.microservice.blog.domain.basic.Storage;
import com.duke.microservice.blog.mapper.basic.StorageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created duke on 2018/7/3
 */
@Service
@Transactional
public class FileDownLoadService {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileDownLoadService.class);

    @Autowired
    private StorageMapper storageMapper;

    /**
     * 文件下载
     *
     * @param fileId   附件id
     * @param request  请求
     * @param response 响应
     */
    public void fileDownLoad(String fileId, HttpServletRequest request, HttpServletResponse response) {
        try {
            Storage storage = this.exist(fileId);
            File file = new File(storage.getPath());
            if (!file.exists()) {
                throw new BusinessException("数据不存在，无法下载");
            }
            response.reset();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            FileInputStream is = new FileInputStream(file);
            ServletOutputStream out = response.getOutputStream();

            String filename = new String((storage.getName() + "." + storage.getSuffix()).getBytes("utf-8"), "utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            byte[] bytes = new byte[1024];
            int len;
            LOGGER.info("文件下载开始：" + filename + "（" + fileId + "）");
            while ((len = is.read(bytes)) != -1) {
                out.write(bytes, 0, len);
                // out.flush();
            }
            LOGGER.info("文件下载结束：" + filename + "（" + fileId + "）");
            is.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 校验附件id有效性
     *
     * @param fileId 附件id
     * @return Storage
     */
    public Storage exist(String fileId) {
        ValidationUtils.notEmpty(fileId, "fileId", "附件id不能为空！");
        Storage storage = storageMapper.selectByPrimaryKey(fileId);
        if (ObjectUtils.isEmpty(storage)) {
            throw new BusinessException("id为：" + fileId + "的附件不存在！");
        }
        return storage;
    }
}
