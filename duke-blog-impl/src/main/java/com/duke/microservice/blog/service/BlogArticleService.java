package com.duke.microservice.blog.service;

import com.duke.framework.domain.DBProperties;
import com.duke.framework.exception.BusinessException;
import com.duke.framework.security.AuthUserDetails;
import com.duke.framework.utils.OperationCodeUtils;
import com.duke.framework.utils.SecurityUtils;
import com.duke.framework.utils.ValidationUtils;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.domain.basic.BlogArticle;
import com.duke.microservice.blog.domain.basic.BlogArticleLabelR;
import com.duke.microservice.blog.domain.basic.BlogArticleTypeR;
import com.duke.microservice.blog.domain.extend.BlogArticleDetail;
import com.duke.microservice.blog.domain.extend.BlogArticleList;
import com.duke.microservice.blog.mapper.basic.BlogArticleMapper;
import com.duke.microservice.blog.mapper.extend.BlogArticleExtendMapper;
import com.duke.microservice.blog.vm.BlogArticleDetailVM;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import com.duke.microservice.blog.vm.BlogLabelVM;
import com.duke.microservice.blog.vm.BlogTypeVM;
import com.duke.microservice.blog.web.controller.BlogArticleController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created duke on 2018/6/23
 */
@Service
@Transactional
@Slf4j
public class BlogArticleService {

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Autowired
    private BlogArticleExtendMapper blogArticleExtendMapper;

    @Autowired
    private BlogLabelService blogLabelService;

    @Autowired
    private BlogTypeService blogTypeService;

    @Autowired
    private BlogArticleLabelRService blogArticleLabelRService;

    @Autowired
    private BlogArticleTypeRService blogArticleTypeRService;

    public static void main(String[] args) {
        try {
            OperationCodeUtils.createOperationCodeSqlFile(new DBProperties("localhost:3306", "duke_auth", "root", "duke"), "duke-blog", BlogArticleController.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增博文
     *
     * @param blogArticleSetVM 博文设置对象
     */
    public void setBlogArticle(BlogArticleSetVM blogArticleSetVM, String id) {
        // 统一处理异常 https://blog.csdn.net/hao_kkkkk/article/details/80538955
        // todo 这个ValidationUtils.validate好像是不能对对象里面的对象进行校验的，抽时间看一下
        ValidationUtils.validate(blogArticleSetVM, "blogArticleSetVM", "参数校验失败！");
        // 获取用户信息
        AuthUserDetails authUserDetails = SecurityUtils.getCurrentUserInfo();
        String userId = authUserDetails.getUserId();
        // 类别
        List<String> typeIds = blogArticleSetVM.getTypeIds();
        // 标签
        List<String> labelIds = blogArticleSetVM.getLabelIds();

        BlogArticle blogArticle = new BlogArticle();
        BeanUtils.copyProperties(blogArticleSetVM, blogArticle);
        Date date = new Date();
        blogArticle.setId(UUID.randomUUID().toString());
        // 处理摘要
        String stripHtml = stripHtml(blogArticleSetVM.getHtmlContent());
        blogArticle.setNavigation(blogArticleSetVM.getNavigation());
        blogArticle.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        blogArticle.setStatus(BlogConstants.BLOG_STATUS.PULISHED.getCode());
        blogArticle.setCreateTime(date);
        blogArticle.setModifyTime(date);
        blogArticle.setUserId(userId);
        blogArticle.setArticleViews(1);
        blogArticle.setStatus(blogArticleSetVM.getIsDraft());

        if (!CollectionUtils.isEmpty(labelIds)) {
            this.setLabels(labelIds, blogArticle.getId());
        }
        if (!CollectionUtils.isEmpty(typeIds)) {
            this.setTypes(typeIds, blogArticle.getId());
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
     * 设置类别
     *
     * @param typeIds typeIds
     */
    private void setTypes(List<String> typeIds, String blogId) {
        // 1、设置博文与标签的关系
        List<BlogArticleTypeR> articleTypeRS = new ArrayList<>();

        typeIds.forEach(typeId -> {
            BlogArticleTypeR blogArticleTypeR = new BlogArticleTypeR();
            blogArticleTypeR.setId(UUID.randomUUID().toString());
            blogArticleTypeR.setTypeId(typeId);
            blogArticleTypeR.setArticleId(blogId);
            articleTypeRS.add(blogArticleTypeR);
        });

        if (!CollectionUtils.isEmpty(articleTypeRS)) {
            blogArticleTypeRService.batchSave(articleTypeRS);
        }
    }

    /**
     * 设置标签
     *
     * @param labelIds labelIds
     */
    private void setLabels(List<String> labelIds, String blogId) {
        // 1、设置博文与标签的关系
        List<BlogArticleLabelR> articleLabelRS = new ArrayList<>();

        labelIds.forEach(labelId -> {
            BlogArticleLabelR blogArticleLabelR = new BlogArticleLabelR();
            blogArticleLabelR.setId(UUID.randomUUID().toString());
            blogArticleLabelR.setLabelId(labelId);
            blogArticleLabelR.setArticleId(blogId);
            articleLabelRS.add(blogArticleLabelR);
        });

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
    public BlogArticleDetailVM selectById(String id) {
        // 校验主键有效性
        ValidationUtils.notEmpty(id, "id", "标题主键不能为空！");

        List<BlogArticleDetail> blogArticleDetails = blogArticleExtendMapper.selectById(id);

        if (!CollectionUtils.isEmpty(blogArticleDetails)) {
            BlogArticleDetail blogArticle = blogArticleDetails.get(0);
            List<BlogLabelVM> blogLabelVMS = null;
            List<BlogTypeVM> blogTypeVMS = null;
            List<String> labelIds = new ArrayList<>();
            List<String> typeIds = new ArrayList<>();
            for (BlogArticleDetail blogArticleDetail : blogArticleDetails) {
                String labelId = blogArticleDetail.getLabelId();
                String labelName = blogArticleDetail.getLabelName();
                String typeId = blogArticleDetail.getTypeId();
                String typeName = blogArticleDetail.getTypeName();

                if (!ObjectUtils.isEmpty(labelId)) {
                    if (!labelIds.contains(labelId)) {
                        labelIds.add(labelId);
                        BlogLabelVM blogLabelVM = new BlogLabelVM(labelId, labelName);
                        if (blogLabelVMS == null) {
                            blogLabelVMS = new ArrayList<>();
                        }
                        blogLabelVMS.add(blogLabelVM);
                    }
                }

                if (!ObjectUtils.isEmpty(typeId)) {
                    if (!typeIds.contains(typeId)) {
                        typeIds.add(typeId);
                        BlogTypeVM blogTypeVM = new BlogTypeVM(typeId, typeName, 0);
                        if (blogTypeVMS == null) {
                            blogTypeVMS = new ArrayList<>();
                        }
                        blogTypeVMS.add(blogTypeVM);
                    }
                }
            }

            // 访问次数加一
            blogArticleExtendMapper.updateArticleViewsById(id, blogArticle.getArticleViews() + 1);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createTimeStr = simpleDateFormat.format(blogArticle.getPublishDate());

            List<BlogArticleDetail> previousArticles = blogArticleExtendMapper.selectPreviousArticles(createTimeStr);

            List<BlogArticleDetail> nextArticles = blogArticleExtendMapper.selectNextArticles(createTimeStr);
            BlogArticleDetailVM previousArticleVM = null;
            BlogArticleDetailVM nextArticleVM = null;
            if (!CollectionUtils.isEmpty(previousArticles)) {
                BlogArticleDetail previousArticle = previousArticles.get(0);
                String tmpId = previousArticle.getId();
                List<String> articleIds = new ArrayList<>();
                articleIds.add(tmpId);
                Map<String, List<BlogLabelVM>> tmpMap = blogLabelService.selectByArticleIds(articleIds);
                previousArticleVM = new BlogArticleDetailVM(tmpId, previousArticle.getTitle());
                previousArticleVM.setLabelVMS(tmpMap.get(tmpId));
            }
            if (!CollectionUtils.isEmpty(nextArticles)) {
                BlogArticleDetail nextArticle = nextArticles.get(0);
                String tmpId = nextArticle.getId();
                List<String> articleIds = new ArrayList<>();
                articleIds.add(tmpId);
                Map<String, List<BlogLabelVM>> tmpMap = blogLabelService.selectByArticleIds(articleIds);
                nextArticleVM = new BlogArticleDetailVM(nextArticle.getId(), nextArticle.getTitle());
                nextArticleVM.setLabelVMS(tmpMap.get(tmpId));
            }
            return new BlogArticleDetailVM(blogArticle.getId(), blogArticle.getTitle(), blogArticle.getNavigation(),
                    createTimeStr, blogArticle.getMdContent(), blogArticle.getHtmlContent(),
                    blogLabelVMS, blogTypeVMS, previousArticleVM, nextArticleVM);
        } else {
            throw new BusinessException("id为" + id + "的文章不存在!");
        }

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
    @Transactional(readOnly = true)
    public PageInfo<BlogArticleDetailVM> select(Integer page, Integer size, String tag, String type) {
        String userId = "b66a3fe7-8fdd-11e8-bcd8-18dbf21f6c28";
        try {
            // 获取用户信息
            AuthUserDetails authUserDetails = SecurityUtils.getCurrentUserInfo();
            userId = authUserDetails.getUserId();
        } catch (Exception e) {
            log.info("无登录用户，查询所有");
        }

        if (ObjectUtils.isEmpty(page) || ObjectUtils.isEmpty(size)) {
            page = 0;
            size = 10;
        }
        PageHelper.startPage(page, size);
        List<BlogArticleList> blogArticleLists = blogArticleExtendMapper.selectByUserId(userId, tag, type);
        List<String> articleIds = blogArticleLists.stream().map(BlogArticleList::getId).collect(Collectors.toList());
        Map<String, List<BlogLabelVM>> blogLabelMap = blogLabelService.selectByArticleIds(articleIds);
        Map<String, List<BlogTypeVM>> blogTypeMap = blogTypeService.selectByArticleIds(articleIds);

        List<BlogArticleDetailVM> blogArticleDetailVMS = new Page<>();
        if (!CollectionUtils.isEmpty(blogArticleLists)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (BlogArticleList articleList : blogArticleLists) {
                String articleId = articleList.getId();
                BlogArticleDetailVM blogArticleDetailVM = new BlogArticleDetailVM(articleId,
                        articleList.getTitle(), articleList.getSummary(), simpleDateFormat.format(articleList.getPublishTime()),
                        articleList.getArticleViews(),
                        blogLabelMap.get(articleId), blogTypeMap.get(articleId));
                blogArticleDetailVMS.add(blogArticleDetailVM);
            }
        }
        BeanUtils.copyProperties(blogArticleLists, blogArticleDetailVMS);
        return new PageInfo<>(blogArticleDetailVMS);
    }

    /**
     * 最新文章推荐
     *
     * @return List<BlogArticleDetailVM>
     */
    @Transactional(readOnly = true)
    public List<BlogArticleDetailVM> latestRecommendedArticles() {
        List<BlogArticleDetailVM> blogArticleDetailVMS = new ArrayList<>();
        List<BlogArticleList> blogArticleLists = blogArticleExtendMapper.latestRecommendedArticles();
        if (!CollectionUtils.isEmpty(blogArticleLists)) {
            blogArticleLists.forEach(tmp -> {
                BlogArticleDetailVM blogArticleDetailVM = new BlogArticleDetailVM(tmp.getId(), tmp.getTitle());
                blogArticleDetailVMS.add(blogArticleDetailVM);
            });
        }
        return blogArticleDetailVMS;
    }

    /**
     * 文章归档查询
     *
     * @return List<BlogArticleDetailVM>
     */
    @Transactional(readOnly = true)
    public List<BlogArticleDetailVM> archiveQuery() {
        List<BlogArticleDetailVM> blogArticleDetailVMS = new ArrayList<>();
        List<BlogArticleList> blogArticleLists = blogArticleExtendMapper.archiveQuery();
        if (!CollectionUtils.isEmpty(blogArticleLists)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
            blogArticleLists.forEach(article -> {
                String title = simpleDateFormat.format(article.getPublishTime()) + " " + article.getTitle();
                BlogArticleDetailVM blogArticleDetailVM = new BlogArticleDetailVM(article.getId(), title);
                blogArticleDetailVMS.add(blogArticleDetailVM);
            });
        }
        return blogArticleDetailVMS;
    }
}
