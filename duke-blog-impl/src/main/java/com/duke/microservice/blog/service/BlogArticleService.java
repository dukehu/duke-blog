package com.duke.microservice.blog.service;

import com.duke.framework.CoreConstants;
import com.duke.framework.exception.BusinessException;
import com.duke.framework.security.AuthUserDetails;
import com.duke.framework.utils.FileUtils;
import com.duke.framework.utils.SecurityUtils;
import com.duke.framework.utils.ValidationUtils;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.config.Md2PdfConfig;
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

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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
    public Md2PdfService md2PdfService;
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
        // OperationCodeUtils.createOperationCodeSqlFile(new DBProperties("localhost:3306", "duke_auth", "root", "duke"), "duke-blog", BlogArticleController.class);
    }

    /**
     * 新增博文
     *
     * @param blogArticleSetVM 博文设置对象
     */
    public void setBlogArticle(BlogArticleSetVM blogArticleSetVM, String id, String saveOrUpdate) {
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

        BlogArticle blogArticle;
        Date date = new Date();
        if (CoreConstants.UPDATE.equalsIgnoreCase(saveOrUpdate)) {
            // 修改
            blogArticle = this.exit(id);
            blogArticleLabelRService.deleteByArticleId(id);
            blogArticleTypeRService.deleteByArticleId(id);
        } else {
            // 新增
            blogArticle = new BlogArticle();
            blogArticle.setId(UUID.randomUUID().toString());
            blogArticle.setUserId(userId);
            blogArticle.setCreateTime(date);
            blogArticle.setArticleViews(1);
            blogArticle.setStatus(blogArticleSetVM.getIsDraft());
        }
        BeanUtils.copyProperties(blogArticleSetVM, blogArticle);
        // 处理摘要
        String stripHtml = stripHtml(blogArticleSetVM.getHtmlContent());
        blogArticle.setNavigation(blogArticleSetVM.getNavigation());
        blogArticle.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        blogArticle.setStatus(BlogConstants.BLOG_STATUS.PULISHED.getCode());
        blogArticle.setModifyTime(date);

        if (!CollectionUtils.isEmpty(labelIds)) {
            this.setLabels(labelIds, blogArticle.getId());
        }
        if (!CollectionUtils.isEmpty(typeIds)) {
            this.setTypes(typeIds, blogArticle.getId());
        }

        if (CoreConstants.UPDATE.equalsIgnoreCase(saveOrUpdate)) {
            blogArticleMapper.updateByPrimaryKey(blogArticle);
        } else {
            blogArticleMapper.insert(blogArticle);
        }
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
     * @param id        主键
     * @param needLogin 是否需要登陆
     * @return BlogArticleDetailVM
     */
    public BlogArticleDetailVM selectById(String id, boolean needLogin) {
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

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createTimeStr = simpleDateFormat.format(blogArticle.getPublishDate());

            BlogArticleDetailVM detailVM = new BlogArticleDetailVM(blogArticle.getId(), blogArticle.getTitle(), blogArticle.getNavigation(),
                    createTimeStr, blogArticle.getMdContent(), blogArticle.getHtmlContent(),
                    blogLabelVMS, blogTypeVMS);
            if (!needLogin) {
                // 访问次数加一
                blogArticleExtendMapper.updateArticleViewsById(id, blogArticle.getArticleViews() + 1);
                this.setPreviousNextArticle(createTimeStr, detailVM);
            }
            return detailVM;
        } else {
            throw new BusinessException("id为" + id + "的文章不存在!");
        }

    }

    private void setPreviousNextArticle(String createTimeStr, BlogArticleDetailVM detailVM) {
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
        detailVM.setPreviousArticle(previousArticleVM);
        detailVM.setNextArticle(nextArticleVM);
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
     * @param page      起始页
     * @param size      每页条数
     * @param tag       标签
     * @param type      类别
     * @param needLogin 是否需要登陆
     * @return PageInfo<BlogArticleDetailVM>
     */
    @Transactional(readOnly = true)
    public PageInfo<BlogArticleDetailVM> select(Integer page, Integer size, String tag, String type, boolean needLogin) {
        String userId = this.getCurrentUserId(needLogin);

        if (ObjectUtils.isEmpty(page) || ObjectUtils.isEmpty(size)) {
            page = 0;
            size = 10;
        }
        PageHelper.startPage(page, size);
        List<BlogArticleList> blogArticleLists = blogArticleExtendMapper.selectByUserId(userId, tag, type);
        Map<String, List<BlogLabelVM>> blogLabelMap = new HashMap<>();
        Map<String, List<BlogTypeVM>> blogTypeMap = new HashMap<>();
        if (!needLogin) {
            List<String> articleIds = blogArticleLists.stream().map(BlogArticleList::getId).collect(Collectors.toList());
            blogLabelMap = blogLabelService.selectByArticleIds(articleIds);
            blogTypeMap = blogTypeService.selectByArticleIds(articleIds);
        }

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
        String userId = this.getCurrentUserId(false);
        List<BlogArticleDetailVM> blogArticleDetailVMS = new ArrayList<>();
        List<BlogArticleList> blogArticleLists = blogArticleExtendMapper.latestRecommendedArticles(userId);
        if (!CollectionUtils.isEmpty(blogArticleLists)) {
            blogArticleLists.forEach(tmp -> {
                BlogArticleDetailVM blogArticleDetailVM = new BlogArticleDetailVM(tmp.getId(), tmp.getTitle());
                blogArticleDetailVMS.add(blogArticleDetailVM);
            });
        }
        return blogArticleDetailVMS;
    }

    /**
     * 获取当前登陆用户id，如果没有登陆返回duke的id
     *
     * @param b b
     * @return String
     */
    private String getCurrentUserId(boolean b) {
        String userId = "b66a3fe7-8fdd-11e8-bcd8-18dbf21f6c28";
        try {
            // 获取用户信息
            AuthUserDetails authUserDetails = SecurityUtils.getCurrentUserInfo();
            userId = authUserDetails.getUserId();
        } catch (Exception e) {
            if (b) {
                throw new BusinessException("无登录用户！");
            } else {
                log.info("无登录用户，查询所有");
            }
        }
        return userId;
    }

    /**
     * 文章归档查询
     *
     * @return List<BlogArticleDetailVM>
     */
    @Transactional(readOnly = true)
    public List<BlogArticleDetailVM> archiveQuery() {
        String userId = this.getCurrentUserId(false);
        List<BlogArticleDetailVM> blogArticleDetailVMS = new ArrayList<>();
        List<BlogArticleList> blogArticleLists = blogArticleExtendMapper.archiveQuery(userId);
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

    /**
     * 根据文章id将文章下载成pdf文件
     *
     * @param id 文章id
     */
    public void downloadPdfByArticleId(String id, HttpServletResponse response) {
        BlogArticle blogArticle = this.exit(id);
        // 转换
        String pdfPath = md2PdfService.convertOfContent(blogArticle.getHtmlContent(), blogArticle.getTitle());
        // 下载
        File file = new File(pdfPath);
        try {
            FileUtils.readDownloadFile(response, blogArticle.getTitle() + ".pdf", file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 删除
            FileUtils.delete(Md2PdfConfig.getPdfFolderPath() + blogArticle.getTitle() + ".pdf");
        }

    }
}
