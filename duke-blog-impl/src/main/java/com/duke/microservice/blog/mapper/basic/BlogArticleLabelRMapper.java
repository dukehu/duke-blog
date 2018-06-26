package com.duke.microservice.blog.mapper.basic;

import com.duke.microservice.blog.domain.basic.BlogArticleLabelR;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogArticleLabelRMapper {
    int deleteByPrimaryKey(String id);

    int insert(BlogArticleLabelR record);

    BlogArticleLabelR selectByPrimaryKey(String id);

    List<BlogArticleLabelR> selectAll();

    int updateByPrimaryKey(BlogArticleLabelR record);
}