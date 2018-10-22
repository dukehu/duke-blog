package com.duke.microservice.blog.service;

import com.duke.framework.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created duke on 2018/10/19
 */
@Service
public class FileShowService {

    /**
     * 写文件
     *
     * @param response 相应
     * @param path     路径
     * @param file     文件
     */
    private void readStreamAsFile(HttpServletResponse response,
                                  String path, File file) {
        response.reset();
        response.setCharacterEncoding("utf-8");
        try (InputStream is = new FileInputStream(file);
             ServletOutputStream out = response.getOutputStream()) {
            response.setHeader("Content-Type",
                    Files.probeContentType(Paths.get(path)) + ";utf-8");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                out.write(bytes, 0, len);
                out.flush();
            }
        } catch (IOException e) {
            throw new BusinessException("文件传输异常");
        }
    }

    /**
     * 展示图片
     *
     * @param path 图片路径
     */
    public void showImage(String path, HttpServletResponse response) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        this.readStreamAsFile(response, path, file);
    }
}
