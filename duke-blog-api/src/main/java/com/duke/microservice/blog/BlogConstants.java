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
}
