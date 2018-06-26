package com.duke.microservice.blog.mapper.basic;

import com.duke.microservice.blog.domain.basic.BlogArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogArticleMapper {
    int deleteByPrimaryKey(String id);

    int insert(BlogArticle record);

    BlogArticle selectByPrimaryKey(String id);

    List<BlogArticle> selectAll();

    int updateByPrimaryKey(BlogArticle record);
}