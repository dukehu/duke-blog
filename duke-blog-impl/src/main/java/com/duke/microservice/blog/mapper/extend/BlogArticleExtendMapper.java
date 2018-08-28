package com.duke.microservice.blog.mapper.extend;

import com.duke.microservice.blog.domain.extend.BlogArticleList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created duke on 2018/7/8
 */
@Mapper
public interface BlogArticleExtendMapper {

    /**
     * 根据用户id查找
     *
     * @param userId 用户id
     * @return List<BlogArticleList>
     */
    List<BlogArticleList> selectByUserId(@Param("userId") String userId);

    /**
     * 访问次数加一
     *
     * @param id           主键
     * @param articleViews 访问次数
     */
    void updateArticleViewsById(@Param("id") String id, @Param("articleViews") int articleViews);

    /**
     * 最新文章推荐
     * @return List<BlogArticleList>
     */
    List<BlogArticleList> latestRecommendedArticles();
}
