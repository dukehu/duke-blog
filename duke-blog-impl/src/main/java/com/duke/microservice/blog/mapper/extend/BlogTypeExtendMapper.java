package com.duke.microservice.blog.mapper.extend;

import com.duke.microservice.blog.domain.basic.BlogType;
import com.duke.microservice.blog.domain.extend.ArticleCount;
import com.duke.microservice.blog.vm.BlogTypeVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created duke on 2018/9/29
 */
@Mapper
public interface BlogTypeExtendMapper {

    /**
     * 根据博文id集合查找
     *
     * @param articleIds 博文id集合
     * @return List
     */
    List<Map<String, String>> selectByArticleIds(@Param("articleIds") List<String> articleIds);

    /**
     * 批量保存
     *
     * @param types 标签集合对象
     */
    void batchSave(@Param("types") List<BlogTypeVM> types);

    /**
     * 根据用户id查找可用类别
     *
     * @param userId 用户id
     * @return List<BlogType>
     */
    List<BlogType> selectByUserId(@Param("userId") String userId);

    /**
     * 根据id删除（假删除）
     *
     * @param id 主键
     */
    void deleteById(@Param("id") String id);

    /**
     * 根据类别id查询每个类别对应的文章数
     *
     * @param typeIds 类别id集合
     * @return List<ArticleCount>
     */
    List<ArticleCount> selectArticleCountsByTypeIds(@Param("typeIds") List<String> typeIds);
}
