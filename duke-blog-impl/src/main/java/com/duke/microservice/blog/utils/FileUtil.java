package com.duke.microservice.blog.utils;

import java.util.Calendar;

/**
 * Created duke on 2018/7/2
 * <p>
 * 文件工具类
 */
public class FileUtil {


    /**
     * 得到文件后缀名
     *
     * @param originalFileName 文件原始名称，如：time.png
     * @return 文件后缀
     */
    public static String getFileSuffix(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length());
    }


    /**
     * 拼装上传文件的文件夹路径
     *
     * @param serviceId 服务id
     * @return 文件夹路径
     */
    public static String getRelativeFilePath(String serviceId) {
        Calendar calendar = Calendar.getInstance();
        // 如："/blog/2018/07/02"
        return "/" + serviceId +
                "/" + calendar.get(Calendar.YEAR) +
                "/" + (calendar.get(Calendar.MONTH) + 1) +
                "/" + calendar.get(Calendar.DATE);
    }

    /**
     * 截取文件名称，去掉后缀
     *
     * @param originalFileName 文件原始名称
     * @return 文件名称
     */
    public static String getFileName(String originalFileName) {
        return originalFileName.substring(0, originalFileName.lastIndexOf("."));
    }
}
