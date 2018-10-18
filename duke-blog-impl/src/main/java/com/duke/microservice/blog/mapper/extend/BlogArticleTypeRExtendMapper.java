package com.duke.microservice.blog.mapper.extend;

import com.duke.microservice.blog.domain.basic.BlogArticleTypeR;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created duke on 2018/9/29
 */
@Mapper
public interface BlogArticleTypeRExtendMapper {

    /**
     * 批量保存
     *
     * @param articleTypeRS 博文类别关系对象
     */
    void batchSave(@Param("articleTypeRS") List<BlogArticleTypeR> articleTypeRS);
}
