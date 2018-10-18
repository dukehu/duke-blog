package com.duke.microservice.blog.mapper.basic;

import com.duke.microservice.blog.domain.basic.Storage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StorageMapper {
    int deleteByPrimaryKey(String id);

    int insert(Storage record);

    Storage selectByPrimaryKey(String id);

    List<Storage> selectAll();

    int updateByPrimaryKey(Storage record);
}