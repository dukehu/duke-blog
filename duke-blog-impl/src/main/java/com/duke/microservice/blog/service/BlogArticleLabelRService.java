package com.duke.microservice.blog.service;

import com.duke.microservice.blog.domain.basic.BlogArticleLabelR;
import com.duke.microservice.blog.mapper.extend.BlogArticleLabelRExtendMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created duke on 2018/7/8
 */
@Service
@Transactional
@Slf4j
public class BlogArticleLabelRService {

    @Autowired
    private BlogArticleLabelRExtendMapper blogArticleLabelRExtendMapper;

    /**
     * 批量保存
     *
     * @param articleLabelRS 博文标签关系对象
     */
    public void batchSave(List<BlogArticleLabelR> articleLabelRS) {
        if (!CollectionUtils.isEmpty(articleLabelRS)) {
            blogArticleLabelRExtendMapper.batchSave(articleLabelRS);
        }
    }
}
