package com.duke.microservice.blog.domain.extend;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created duke on 2018/7/8
 */
@Setter
@Getter
public class BlogArticleList {
    private String id;
    private String title;
    private String summary;
    private Date publishTime;
    private Integer articleViews;
}
