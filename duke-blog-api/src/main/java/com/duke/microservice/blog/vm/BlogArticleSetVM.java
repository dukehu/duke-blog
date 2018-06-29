package com.duke.microservice.blog.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created duke on 2018/6/23
 */
@ApiModel(description = "博文设置VM")
public class BlogArticleSetVM {

    @ApiModelProperty(value = "博文标题", required = true)
    @NotBlank(message = "标题不能为空")
    @Length(max = 100, min = 1, message = "博文标题长度应为1-3之间！")
    private String title;

    @ApiModelProperty(value = "博文html原数据", required = true)
    @NotBlank(message = "博文html原数据不能为空")
    @Length(max = 10000, min = 1, message = "博文html原数据长度应为1-3之间！")
    private String htmlContent;

    @ApiModelProperty(value = "markdown原数据", required = true)
    @NotBlank(message = "博文html原数据不能为空")
    @Length(max = 10000, min = 1, message = "博文html原数据长度应为1-3之间！")
    private String mdContent;

    private List<BlogLabelVM> blogLabelVMS;

    private List<BlogTypeVM> blogTypeVMS;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getMdContent() {
        return mdContent;
    }

    public void setMdContent(String mdContent) {
        this.mdContent = mdContent;
    }

    public List<BlogLabelVM> getBlogLabelVMS() {
        return blogLabelVMS;
    }

    public void setBlogLabelVMS(List<BlogLabelVM> blogLabelVMS) {
        this.blogLabelVMS = blogLabelVMS;
    }

    public List<BlogTypeVM> getBlogTypeVMS() {
        return blogTypeVMS;
    }

    public void setBlogTypeVMS(List<BlogTypeVM> blogTypeVMS) {
        this.blogTypeVMS = blogTypeVMS;
    }
}
