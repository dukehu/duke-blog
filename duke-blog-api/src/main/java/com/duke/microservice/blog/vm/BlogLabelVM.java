package com.duke.microservice.blog.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created duke on 2018/6/29
 */
@Setter
@Getter
@AllArgsConstructor  // 该注解使用在类上，该注解提供一个全参数的构造方法，默认不提供无参构造。
@NoArgsConstructor
@ApiModel("博文标签VM")
public class BlogLabelVM {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "标签名", required = true, example = "java")
    @NotBlank(message = "标签名称不能为空")
    @Length(max = 10, min = 1, message = "长度应为1-10！")
    private String name;
}
