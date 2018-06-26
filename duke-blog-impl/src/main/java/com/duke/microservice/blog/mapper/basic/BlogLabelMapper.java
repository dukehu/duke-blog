package com.duke.microservice.blog.mapper.basic;

import com.duke.microservice.blog.domain.basic.BlogLabel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogLabelMapper {
    int deleteByPrimaryKey(String id);

    int insert(BlogLabel record);

    BlogLabel selectByPrimaryKey(String id);

    List<BlogLabel> selectAll();

    int updateByPrimaryKey(BlogLabel record);
}