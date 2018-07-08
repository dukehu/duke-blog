package com.duke.microservice.blog.mapper.extend;

import com.duke.microservice.blog.domain.basic.BlogLabel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created duke on 2018/6/29
 */
@Mapper
public interface BlogLabelExtendMapper {

    /**
     * 根据用户id查找标签
     *
     * @param userId 用户id
     * @return List<BlogLabel>
     */
    List<BlogLabel> selectByUserId(@Param("userId") String userId);

    /**
     * 批量保存
     *
     * @param labels 标签集合对象
     */
    void batchSave(@Param("labels") List<BlogLabel> labels);
}
