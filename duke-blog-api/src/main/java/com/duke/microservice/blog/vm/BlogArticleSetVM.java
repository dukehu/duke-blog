package com.duke.microservice.blog.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created duke on 2018/6/23
 */
@ApiModel(description = "博文设置VM")
public class BlogArticleSetVM {

    @ApiModelProperty(value = "博文标题", required = true)
    private String title;

    @ApiModelProperty(value = "博文html原数据", required = true)
    private String htmlContent;

    @ApiModelProperty(value = "markdown原数据", required = true)
    private String mdContent;

    @ApiModelProperty(value = "博文标签，多个之间用,（英文逗号）隔开", required = true)
    private String label;

    @ApiModelProperty(value = "博文类别，多个之间用,（英文逗号）隔开", required = true)
    private String type;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
