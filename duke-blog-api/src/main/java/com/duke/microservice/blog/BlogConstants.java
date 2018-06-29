package com.duke.microservice.blog;

/**
 * Created duke on 2018/6/23
 */
public class BlogConstants {

    public static final String SERVICE_ID = "duke-blog";

    /**
     * 博客状态枚举类
     */
    public static enum BLOG_STATUS {
        /**
         * 0：删除 1：发布 2：存草稿
         */
        DELETED(0, "删除"),
        PULISHED(1, "发布"),
        DRAFTED(2, "存草稿");

        private Integer code;
        private String desc;

        BLOG_STATUS(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
