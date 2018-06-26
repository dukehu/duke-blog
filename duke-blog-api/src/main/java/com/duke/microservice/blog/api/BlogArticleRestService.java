package com.duke.microservice.blog.api;

import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.common.Response;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created duke on 2018/6/23
 */
@FeignClient(value = BlogConstants.SERVICE_ID)
public interface BlogArticleRestService {

    /**
     * 新增一片博文
     *
     * @param blogArticleSetVM 博文新增vm
     * @return String
     */
    @RequestMapping(value = "/blog_article", method = RequestMethod.POST)
    Response<String> save(@RequestParam(value = "blogArticleSetVM", required = false) BlogArticleSetVM blogArticleSetVM);
}
