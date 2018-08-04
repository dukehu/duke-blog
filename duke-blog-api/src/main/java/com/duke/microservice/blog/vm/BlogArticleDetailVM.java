package com.duke.microservice.blog.vm;

import java.util.List;

/**
 * Created duke on 2018/7/7
 */
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

    public BlogArticleDetailVM() {
    }

    public BlogArticleDetailVM(String id, String title, String summary, String publishDate, List<BlogLabelVM> labelVMS, List<BlogTypeVM> typeVMS) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.publishDate = publishDate;
        this.labelVMS = labelVMS;
        this.typeVMS = typeVMS;
    }

    public BlogArticleDetailVM(String id, String title, String navigation, String publishDate, String mdContent, String htmlContent) {
        this.id = id;
        this.title = title;
        this.navigation = navigation;
        this.publishDate = publishDate;
        this.mdContent = mdContent;
        this.htmlContent = htmlContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getMdContent() {
        return mdContent;
    }

    public void setMdContent(String mdContent) {
        this.mdContent = mdContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public List<BlogLabelVM> getLabelVMS() {
        return labelVMS;
    }

    public void setLabelVMS(List<BlogLabelVM> labelVMS) {
        this.labelVMS = labelVMS;
    }

    public List<BlogTypeVM> getTypeVMS() {
        return typeVMS;
    }

    public void setTypeVMS(List<BlogTypeVM> typeVMS) {
        this.typeVMS = typeVMS;
    }
}
