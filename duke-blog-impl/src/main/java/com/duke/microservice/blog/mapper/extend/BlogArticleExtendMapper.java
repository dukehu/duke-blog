package com.duke.microservice.blog.mapper.extend;

import com.duke.microservice.blog.domain.extend.BlogArticleDetail;
import com.duke.microservice.blog.domain.extend.BlogArticleList;
import com.duke.microservice.blog.vm.BlogArticleDetailVM;
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
     * @param tag    标签
     * @param type   类别
     * @return List<BlogArticleList>
     */
    List<BlogArticleList> selectByUserId(@Param("userId") String userId, @Param("tag") String tag, @Param("type") String type);

    /**
     * 访问次数加一
     *
     * @param id           主键
     * @param articleViews 访问次数
     */
    void updateArticleViewsById(@Param("id") String id, @Param("articleViews") int articleViews);

    /**
     * 最新文章推荐
     *
     * @param userId 当前用户id
     * @return List<BlogArticleList>
     */
    List<BlogArticleList> latestRecommendedArticles(@Param("userId") String userId);

    /**
     * 文章归档查询
     *
     * @param userId 当前用户id
     * @return List<BlogArticleList>
     */
    List<BlogArticleList> archiveQuery(@Param("userId") String userId);

    /**
     * 查询当前文章创建时间之前的文章
     *
     * @param createTime 当前时间
     * @return List<BlogArticleDetail>
     */
    List<BlogArticleDetail> selectPreviousArticles(@Param("createTime") String createTime);

    /**
     * 查询当前文章创建时间之后的文章
     *
     * @param createTime 当前时间
     * @return List<BlogArticleDetail>
     */
    List<BlogArticleDetail> selectNextArticles(@Param("createTime") String createTime);

    /**
     * 根据id查找
     *
     * @param id 主键
     * @return BlogArticleDetail
     */
    List<BlogArticleDetail> selectById(@Param("id") String id);
}
