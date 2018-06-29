package com.duke.microservice.blog.service;

import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.domain.basic.BlogArticle;
import com.duke.microservice.blog.mapper.basic.BlogArticleMapper;
import com.duke.microservice.blog.utils.ValidationUtils;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import com.duke.microservice.blog.vm.BlogLabelVM;
import com.duke.microservice.blog.vm.BlogTypeVM;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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
     * @param status           0：删除 1：发布 2：存草稿
     */
    public void setBlogArticle(BlogArticleSetVM blogArticleSetVM, Integer status, String id) {
        // 统一处理异常 https://blog.csdn.net/hao_kkkkk/article/details/80538955
        // todo 这个ValidationUtils.validate好像是不能对对象里面的对象进行校验的，抽时间看一下
        ValidationUtils.validate(blogArticleSetVM, "blogArticleSetVM", "参数校验失败！");

        // todo 获取用户信息
        String userId = "duke";
        // 类别
        List<BlogTypeVM> blogTypeVMS = blogArticleSetVM.getBlogTypeVMS();
        // 标签
        List<BlogLabelVM> blogLabelVMS = blogArticleSetVM.getBlogLabelVMS();

        BlogArticle blogArticle = new BlogArticle();
        BeanUtils.copyProperties(blogArticleSetVM, blogArticle);
        Date date = new Date();
        blogArticle.setId(UUID.randomUUID().toString());
        blogArticle.setSummary("");
        blogArticle.setStatus(BlogConstants.BLOG_STATUS.PULISHED.getCode());
        blogArticle.setCreateTime(date);
        blogArticle.setModifyTime(date);
        blogArticle.setUserId(userId);
        blogArticleMapper.insert(blogArticle);
    }

    /**
     * 根据主键查找博文详情
     *
     * @param id 主键
     * @return
     */
    @Transactional(readOnly = true)
    public BlogArticleSetVM selectBlogById(String id) {
        // 校验主键有效性
        BlogArticle blogArticle = this.exit(id);
        return null;
    }

    /**
     * 校验id有效性
     *
     * @param id 主键
     * @return BlogArticle
     */
    @Transactional(readOnly = true)
    public BlogArticle exit(String id) {
        BlogArticle blogArticle = new BlogArticle();
        return blogArticle;
    }
}
