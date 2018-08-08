package com.duke.microservice.blog.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created duke on 2018/6/29
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("博文类型VM")
public class BlogTypeVM {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "类别名", required = true, example = "java")
    @NotBlank(message = "类别名称不能为空")
    @Length(max = 10, min = 1, message = "长度应为1-10！")
    private String name;

}