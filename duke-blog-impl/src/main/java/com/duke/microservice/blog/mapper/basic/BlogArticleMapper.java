package com.duke.microservice.blog.mapper.basic;

import com.duke.microservice.blog.domain.basic.BlogArticle;

import java.util.List;

public interface BlogArticleMapper {
    int deleteByPrimaryKey(String id);

    int insert(BlogArticle record);

    BlogArticle selectByPrimaryKey(String id);

    List<BlogArticle> selectAll();

    int updateByPrimaryKey(BlogArticle record);
}