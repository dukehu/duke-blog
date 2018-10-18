package com.duke.microservice.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created duke on 2018/6/23
 */
public class BlogConstants {

    public static final String SERVICE_ID = "duke-blog";

    /**
     * 博客状态枚举类
     */
    @AllArgsConstructor
    public enum BLOG_STATUS {
        /**
         * 0：删除 1：发布 2：存草稿
         */
        DELETED(0, "删除"),
        PULISHED(1, "发布"),
        DRAFTED(2, "存草稿");

        @Getter
        private Integer code;
        @Getter
        private String desc;
    }

    /**
     * 删除状态
     */
    @AllArgsConstructor
    public enum DELETE_STATUS {
        /**
         * 0：未删除 1：已删除
         */
        NOT_DELETE(0, "未删除"),
        DELETED(1, "已删除");

        @Getter
        private int code;
        @Getter
        private String desc;
    }

    /**
     * 文件状态
     */
    @AllArgsConstructor
    public enum FILE_STATUS {
        /**
         * 0：删除  1：存在
         */
        DELETED(0, "删除"),
        EXIST(1, "发布");

        @Getter
        private Integer code;
        @Getter
        private String desc;
    }
}
