package com.duke.microservice.blog.vm;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created duke on 2018/7/7
 */
@Setter
@Getter
public class BlogArticleDetailVM {

    /**
     * 主键
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 导航目录
     */
    private String navigation;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 发布时间
     */
    private String publishDate;

    /**
     * md数据
     */
    private String mdContent;

    /**
     * html数据
     */
    private String htmlContent;

    /**
     * 标签
     */
    private List<BlogLabelVM> labelVMS;

    /**
     * 分类
     */
    private List<BlogTypeVM> typeVMS;

    /**
     * 浏览次数
     */
    private Integer articleViews;

    /**
     * 上一篇
     */
    private BlogArticleDetailVM previousArticle;

    /**
     * 下一篇
     */
    private BlogArticleDetailVM nextArticle;

    public BlogArticleDetailVM() {
    }

    public BlogArticleDetailVM(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public BlogArticleDetailVM(String id, String title, String summary, String publishDate, Integer articleViews, List<BlogLabelVM> labelVMS, List<BlogTypeVM> typeVMS) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.publishDate = publishDate;
        this.articleViews = articleViews;
        this.labelVMS = labelVMS;
        this.typeVMS = typeVMS;
    }

    public BlogArticleDetailVM(String id, String title, String navigation, String publishDate, String mdContent, String htmlContent, List<BlogLabelVM> labelVMS, List<BlogTypeVM> typeVMS) {
        this.id = id;
        this.title = title;
        this.navigation = navigation;
        this.publishDate = publishDate;
        this.mdContent = mdContent;
        this.htmlContent = htmlContent;
        this.labelVMS = labelVMS;
        this.typeVMS = typeVMS;
    }
}
