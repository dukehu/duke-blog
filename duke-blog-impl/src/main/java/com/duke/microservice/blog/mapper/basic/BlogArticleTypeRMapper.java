package com.duke.microservice.blog.mapper.basic;

import com.duke.microservice.blog.domain.basic.BlogArticleTypeR;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogArticleTypeRMapper {
    int deleteByPrimaryKey(String id);

    int insert(BlogArticleTypeR record);

    BlogArticleTypeR selectByPrimaryKey(String id);

    List<BlogArticleTypeR> selectAll();

    int updateByPrimaryKey(BlogArticleTypeR record);
}