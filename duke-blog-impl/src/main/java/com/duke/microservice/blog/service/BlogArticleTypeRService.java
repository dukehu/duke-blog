package com.duke.microservice.blog.service;

import com.duke.microservice.blog.domain.basic.BlogArticleTypeR;
import com.duke.microservice.blog.mapper.extend.BlogArticleTypeRExtendMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created duke on 2018/9/29
 */
@Service
@Transactional
@Slf4j
public class BlogArticleTypeRService {

    @Autowired
    private BlogArticleTypeRExtendMapper blogArticleTypeRExtendMapper;

    /**
     * 批量保存
     *
     * @param articleTypeRS 博文类别关系对象
     */
    public void batchSave(List<BlogArticleTypeR> articleTypeRS) {
        if (!CollectionUtils.isEmpty(articleTypeRS)) {
            blogArticleTypeRExtendMapper.batchSave(articleTypeRS);
        }
    }

    /**
     * 根据文章id删除
     *
     * @param articleId 文章id
     */
    public void deleteByArticleId(String articleId) {
        if (!ObjectUtils.isEmpty(articleId)) {
            blogArticleTypeRExtendMapper.deleteByArticleId(articleId);
        }
    }
}
