package com.duke.microservice.blog.domain.extend;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created duke on 2018/9/30
 */
@Getter
@Setter
public class BlogArticleDetail {
    /**
     * 主键
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 导航目录
     */
    private String navigation;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 发布时间
     */
    private Date publishDate;

    /**
     * md数据
     */
    private String mdContent;

    /**
     * html数据
     */
    private String htmlContent;

    /**
     * 标签id
     */
    private String labelId;

    /**
     * 标签name
     */
    private String labelName;

    /**
     * 类别id
     */
    private String typeId;

    /**
     * 类别name
     */
    private String typeName;

    private int articleViews;
}
