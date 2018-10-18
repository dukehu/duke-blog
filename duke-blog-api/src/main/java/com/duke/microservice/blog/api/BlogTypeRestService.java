package com.duke.microservice.blog.api;

import com.duke.framework.web.Response;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.vm.BlogTypeVM;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created duke on 2018/9/29
 */
@FeignClient(value = BlogConstants.SERVICE_ID)
public interface BlogTypeRestService {

    /**
     * 查询所有可用的类别
     *
     * @return List<BlogLabelVM>
     */
    @RequestMapping(value = "/nologin/blog_type", method = RequestMethod.GET)
    Response<List<BlogTypeVM>> noLoginSelect();

    /**
     * 查询所有可用的类别（需要登陆）
     *
     * @return List<BlogLabelVM>
     */
    @RequestMapping(value = "/blog_type", method = RequestMethod.GET)
    Response<List<BlogTypeVM>> select();

    /**
     * 新增类别
     *
     * @param name 类别名称
     * @return String
     */
    @RequestMapping(value = "/blog_type", method = RequestMethod.POST)
    Response<String> save(@RequestParam(value = "name", required = false) String name);

    /**
     * 修改类别
     *
     * @param id   类别id
     * @param name 类别名称
     * @return String
     */
    @RequestMapping(value = "/blog_type", method = RequestMethod.PUT)
    Response<String> update(@RequestParam(value = "id", required = false) String id,
                            @RequestParam(value = "name", required = false) String name);

    /**
     * 删除类别
     *
     * @param id 类别id
     * @return String
     */
    @RequestMapping(value = "/blog_type", method = RequestMethod.DELETE)
    Response<String> delete(@PathVariable(value = "id", required = false) String id);

}
