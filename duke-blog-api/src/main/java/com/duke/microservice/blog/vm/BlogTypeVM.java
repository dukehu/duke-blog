package com.duke.microservice.blog.vm;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created duke on 2018/6/29
 */
public class BlogTypeVM {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "类别名", required = true, example = "java")
    @NotBlank(message = "类别名称不能为空")
    @Length(max = 10, min = 1, message = "长度应为1-10！")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}