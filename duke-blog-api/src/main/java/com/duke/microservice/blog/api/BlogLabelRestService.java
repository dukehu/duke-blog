package com.duke.microservice.blog.api;

import com.duke.framework.web.Response;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.vm.BlogLabelVM;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created duke on 2018/6/29
 */
@FeignClient(value = BlogConstants.SERVICE_ID)
public interface BlogLabelRestService {

    /**
     * 查询所有可用的标签（不需要登陆）
     *
     * @return List<BlogLabelVM>
     */
    @RequestMapping(value = "/nologin/blog_label", method = RequestMethod.GET)
    Response<List<BlogLabelVM>> noLoginSelect();

    /**
     * 查询所有可用的标签（需要登陆）
     *
     * @return List<BlogLabelVM>
     */
    @RequestMapping(value = "/blog_label", method = RequestMethod.GET)
    Response<List<BlogLabelVM>> select();

    /**
     * 新增标签
     *
     * @param name 标签名称
     * @return String
     */
    @RequestMapping(value = "/blog_label", method = RequestMethod.POST)
    Response<String> save(@RequestParam(value = "name", required = false) String name);

    /**
     * 修改标签
     *
     * @param id   标签id
     * @param name 标签名称
     * @return String
     */
    @RequestMapping(value = "/blog_label", method = RequestMethod.PUT)
    Response<String> update(@RequestParam(value = "id", required = false) String id,
                            @RequestParam(value = "name", required = false) String name);

    /**
     * 删除标签
     *
     * @param id 标签id
     * @return String
     */
    @RequestMapping(value = "/blog_label", method = RequestMethod.DELETE)
    Response<String> delete(@PathVariable(value = "id", required = false) String id);

}
