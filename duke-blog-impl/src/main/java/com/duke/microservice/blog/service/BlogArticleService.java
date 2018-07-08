package com.duke.microservice.blog.service;

import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.domain.basic.BlogArticle;
import com.duke.microservice.blog.domain.basic.BlogArticleLabelR;
import com.duke.microservice.blog.domain.basic.BlogLabel;
import com.duke.microservice.blog.domain.extend.BlogArticleList;
import com.duke.microservice.blog.exception.BusinessException;
import com.duke.microservice.blog.mapper.basic.BlogArticleMapper;
import com.duke.microservice.blog.mapper.extend.BlogArticleExtendMapper;
import com.duke.microservice.blog.utils.ValidationUtils;
import com.duke.microservice.blog.vm.BlogArticleDetailVM;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import com.duke.microservice.blog.vm.BlogLabelVM;
import com.duke.microservice.blog.vm.BlogTypeVM;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created duke on 2018/6/23
 */
@Service
public class BlogArticleService {

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Autowired
    private BlogArticleExtendMapper blogArticleExtendMapper;

    @Autowired
    private BlogLabelService blogLabelService;

    @Autowired
    private BlogArticleLabelRService blogArticleLabelRService;

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
        // 处理摘要
        String stripHtml = stripHtml(blogArticleSetVM.getHtmlContent());
        blogArticle.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        blogArticle.setStatus(BlogConstants.BLOG_STATUS.PULISHED.getCode());
        blogArticle.setCreateTime(date);
        blogArticle.setModifyTime(date);
        blogArticle.setUserId(userId);

        if (!CollectionUtils.isEmpty(blogLabelVMS)) {
            this.setLabels(blogLabelVMS, blogArticle.getId(), userId, date);
        }
        blogArticleMapper.insert(blogArticle);
    }

    private String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

    /**
     * 设置标签保存标签
     *
     * @param blogLabelVMS blogLabelVMS
     */
    private void setLabels(List<BlogLabelVM> blogLabelVMS, String blogId, String userId, Date date) {
        // 1、保存没有的标签（id为空的）
        // 2、设置博文与标签的关系
        List<BlogLabel> labels = new ArrayList<>();
        List<BlogArticleLabelR> articleLabelRS = new ArrayList<>();

        for (BlogLabelVM labelVM : blogLabelVMS) {
            String labelId = labelVM.getId();
            if (ObjectUtils.isEmpty(labelId)) {
                labelId = UUID.randomUUID().toString();
                BlogLabel label = new BlogLabel();
                label.setId(labelId);
                label.setName(labelVM.getName());
                label.setUserId(userId);
                label.setCreateTime(date);
                label.setModifyTime(date);
                labels.add(label);
            }
            BlogArticleLabelR blogArticleLabelR = new BlogArticleLabelR();
            blogArticleLabelR.setId(UUID.randomUUID().toString());
            blogArticleLabelR.setLabelId(labelId);
            blogArticleLabelR.setArticleId(blogId);
            articleLabelRS.add(blogArticleLabelR);
        }
        if (!CollectionUtils.isEmpty(labels)) {
            blogLabelService.batchSave(labels);
        }
        if (!CollectionUtils.isEmpty(articleLabelRS)) {
            blogArticleLabelRService.batchSave(articleLabelRS);
        }
    }

    /**
     * 根据主键查找博文详情
     *
     * @param id 主键
     * @return BlogArticleDetailVM
     */
    @Transactional(readOnly = true)
    public BlogArticleDetailVM selectById(String id) {
        // 校验主键有效性
        BlogArticle blogArticle = this.exit(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return new BlogArticleDetailVM(blogArticle.getId(), blogArticle.getTitle()
                , simpleDateFormat.format(blogArticle.getCreateTime()), blogArticle.getMdContent(), blogArticle.getHtmlContent());
    }

    /**
     * 校验id有效性
     *
     * @param id 主键
     * @return BlogArticle
     */
    @Transactional(readOnly = true)
    public BlogArticle exit(String id) {
        ValidationUtils.notEmpty(id, "id", "标题主键不能为空！");
        BlogArticle blogArticle = blogArticleMapper.selectByPrimaryKey(id);

        if (ObjectUtils.isEmpty(blogArticle)) {
            throw new BusinessException("id为" + id + "的博文不存在！");
        }

        return blogArticle;
    }

    /**
     * 博客列表
     *
     * @return PageInfo<BlogArticleDetailVM>
     */
    public PageInfo<BlogArticleDetailVM> select(Integer page, Integer size) {
        // todo 获取用户信息
        String userId = "duke";

        if (ObjectUtils.isEmpty(page) || ObjectUtils.isEmpty(size)) {
            page = 0;
            size = 10;
        }
        PageHelper.offsetPage(page, size);
        List<BlogArticleList> blogArticleLists = blogArticleExtendMapper.selectByUserId(userId);
        List<BlogArticleDetailVM> blogArticleDetailVMS = new ArrayList<>();
        Map<String, BlogArticleDetailVM> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(blogArticleLists)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (BlogArticleList articleList : blogArticleLists) {
                String articleId = articleList.getId();
                BlogLabelVM labelVM = new BlogLabelVM(articleList.getLabelId(), articleList.getLabelName());
                BlogTypeVM typeVM = new BlogTypeVM(articleList.getTypeId(), articleList.getTypeName());
                if (!map.containsKey(articleId)) {
                    List<BlogLabelVM> labelVMS = new ArrayList<>();
                    List<BlogTypeVM> typeVMS = new ArrayList<>();
                    labelVMS.add(labelVM);
                    typeVMS.add(typeVM);
                    BlogArticleDetailVM blogArticleDetailVM = new BlogArticleDetailVM(articleId,
                            articleList.getTitle(), articleList.getSummary(), simpleDateFormat.format(articleList.getPublishTime()),
                            labelVMS, typeVMS);
                    blogArticleDetailVMS.add(blogArticleDetailVM);
                    map.put(articleId, blogArticleDetailVM);
                } else {
                    map.get(articleId).getLabelVMS().add(labelVM);
                    map.get(articleId).getTypeVMS().add(typeVM);
                }
            }
        }

        return new PageInfo<>(blogArticleDetailVMS);
    }
}
