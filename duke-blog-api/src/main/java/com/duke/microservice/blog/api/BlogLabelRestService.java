package com.duke.microservice.blog.api;

import com.duke.framework.web.Response;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.vm.BlogLabelVM;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created duke on 2018/6/29
 */
@FeignClient(value = BlogConstants.SERVICE_ID)
public interface BlogLabelRestService {

    /**
     * 查询所有可用的标签
     *
     * @return List<BlogLabelVM>
     */
    @RequestMapping(value = "/blog_label", method = RequestMethod.GET)
    Response<List<BlogLabelVM>> select();

}
