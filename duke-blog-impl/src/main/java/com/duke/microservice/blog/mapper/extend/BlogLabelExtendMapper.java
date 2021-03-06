package com.duke.microservice.blog.mapper.extend;

import com.duke.microservice.blog.domain.basic.BlogLabel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据博文id集合查找
     *
     * @param articleIds 博文id集合
     * @return List
     */
    List<Map<String, String>> selectByArticleIds(@Param("articleIds") List<String> articleIds);

    /**
     * 根据id删除（假删）
     *
     * @param id 主键
     */
    void deleteById(@Param("id") String id);
}
