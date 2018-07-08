package com.duke.microservice.blog.mapper.extend;

import com.duke.microservice.blog.domain.extend.BlogArticleList;
import org.apache.ibatis.annotations.Mapper;

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
    List<BlogArticleList> selectByUserId(String userId);
}
