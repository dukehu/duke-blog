package com.duke.microservice.blog.service;

import com.duke.microservice.blog.mapper.basic.BlogArticleMapper;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created duke on 2018/6/23
 */
@Service
public class BlogArticleService {

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    public void saveOrUpdate(BlogArticleSetVM blogArticleSetVM) {
    }

    @Transactional(readOnly = true)
    public String selectBlogById(String id) {
        return blogArticleMapper.selectByPrimaryKey(id).getContent();
    }
}
