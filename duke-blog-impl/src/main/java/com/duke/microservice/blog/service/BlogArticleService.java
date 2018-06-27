package com.duke.microservice.blog.service;

import com.duke.microservice.blog.domain.basic.BlogArticle;
import com.duke.microservice.blog.mapper.basic.BlogArticleMapper;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * Created duke on 2018/6/23
 */
@Service
public class BlogArticleService {

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    /**
     * 新增博文
     *
     * @param blogArticleSetVM 博文设置对象
     */
    public void saveOrUpdate(BlogArticleSetVM blogArticleSetVM) {
        // todo 统一处理异常 https://blog.csdn.net/hao_kkkkk/article/details/80538955
        // todo 统一处理mybatis抛出的异常
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setId(UUID.randomUUID().toString());
        blogArticle.setTitle(blogArticleSetVM.getTitle());
        blogArticle.setContent("");
        blogArticle.setHtmlContent(blogArticleSetVM.getHtmlContent());
        blogArticle.setMdContent(blogArticleSetVM.getMdContent());
        blogArticle.setStatus(1);
        blogArticle.setCreateTime(new Date());
        blogArticle.setModifyTime(new Date());
        blogArticle.setUserId("");
        blogArticleMapper.insert(blogArticle);
    }

    @Transactional(readOnly = true)
    public String selectBlogById(String id) {
        return blogArticleMapper.selectByPrimaryKey(id).getContent();
    }
}
