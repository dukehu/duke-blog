package com.duke.microservice.blog.domain.extend;

import lombok.Data;

import java.util.Date;

/**
 * Created duke on 2018/7/8
 */
@Data
public class BlogArticleList {
    private String id;
    private String title;
    private String summary;
    private Date publishTime;
    private String labelId;
    private String labelName;
    private String typeId;
    private String typeName;
}
