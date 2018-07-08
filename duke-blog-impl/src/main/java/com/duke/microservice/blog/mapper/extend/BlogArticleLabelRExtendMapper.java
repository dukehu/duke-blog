package com.duke.microservice.blog.mapper.extend;

import com.duke.microservice.blog.domain.basic.BlogArticleLabelR;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created duke on 2018/7/8
 */
@Mapper
public interface BlogArticleLabelRExtendMapper {

    /**
     * 批量保存
     *
     * @param articleLabelRS 博文标签关系对象
     */
    void batchSave(@Param("articleLabelRS") List<BlogArticleLabelR> articleLabelRS);
}
