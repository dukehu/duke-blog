package com.duke.microservice.blog.mapper.basic;

import com.duke.microservice.blog.domain.basic.BlogType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogTypeMapper {
    int deleteByPrimaryKey(String id);

    int insert(BlogType record);

    BlogType selectByPrimaryKey(String id);

    List<BlogType> selectAll();

    int updateByPrimaryKey(BlogType record);
}