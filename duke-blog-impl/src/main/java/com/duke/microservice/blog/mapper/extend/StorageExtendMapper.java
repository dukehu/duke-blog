package com.duke.microservice.blog.mapper.extend;

import com.duke.microservice.blog.domain.basic.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created duke on 2018/7/3
 */
@Mapper
public interface StorageExtendMapper {

    /**
     * 根据文件md5值查询
     *
     * @param md5 文件md5值
     * @return List<Storage>
     */
    List<Storage> selectByMD5(@Param("md5") String md5);
}
