package com.duke.microservice.blog.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created duke on 2018/6/23
 */
@Setter
@Getter
@ApiModel(description = "博文设置VM")
public class BlogArticleSetVM {

    @ApiModelProperty(value = "博文标题", required = true)
    @NotBlank(message = "标题不能为空")
    @Length(max = 100, min = 1, message = "博文标题长度应为1-100之间！")
    private String title;

    @ApiModelProperty(value = "导航目录")
    @Length(max = 10000, message = "导航目录长度应为1-10000之间！")
    private String navigation;

    @ApiModelProperty(value = "博文html原数据", required = true)
    @NotBlank(message = "博文html原数据不能为空")
    @Length(max = 10000, min = 1, message = "博文html原数据长度应为1-10000之间！")
    private String htmlContent;

    @ApiModelProperty(value = "markdown原数据", required = true)
    @NotBlank(message = "markdown原数据不能为空")
    @Length(max = 10000, min = 1, message = "markdown原数据长度应为1-3之间！")
    private String mdContent;

    @ApiModelProperty(value = "是否为草稿（1：不是 2：是）", required = true, allowableValues = "1, 2")
    private Integer isDraft;

    private List<String> labelIds;

    private List<String> typeIds;
}
