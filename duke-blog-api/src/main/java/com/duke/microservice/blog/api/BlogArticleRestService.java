package com.duke.microservice.blog.api;

import com.duke.framework.web.Response;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.vm.BlogArticleDetailVM;
import com.duke.microservice.blog.vm.BlogArticleSetVM;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created duke on 2018/6/23
 */
@FeignClient(value = BlogConstants.SERVICE_ID)
public interface BlogArticleRestService {

    /**
     * 发表博文
     *
     * @param blogArticleSetVM 博文设置vm
     * @return String
     */
    @RequestMapping(value = "/blog_article", method = RequestMethod.POST)
    Response<String> publish(@RequestParam(value = "blogArticleSetVM", required = false) BlogArticleSetVM blogArticleSetVM);

    @RequestMapping(value = "/blog_article/update/{id}", method = RequestMethod.PUT)
    Response<String> update(
            @PathVariable(value = "id", required = false) String id,
            @RequestParam(value = "blogArticleSetVM", required = false) BlogArticleSetVM blogArticleSetVM);

    /**
     * 删除博文
     *
     * @param id 主键
     * @return String
     */
    @RequestMapping(value = "/blog_article/{id}", method = RequestMethod.DELETE)
    Response<String> delete(@PathVariable(value = "id", required = false) String id);

    /**
     * 博文详情，不需要登陆的
     *
     * @param id 主键
     * @return BlogArticleSetVM
     */
    @RequestMapping(value = "/nologin/blog_article/{id}", method = RequestMethod.GET)
    Response<BlogArticleDetailVM> selectByIdNologin(@PathVariable(value = "id", required = false) String id);

    /**
     * 博文详情，需要登陆的
     *
     * @param id 主键
     * @return BlogArticleSetVM
     */
    @RequestMapping(value = "/blog_article/{id}", method = RequestMethod.GET)
    Response<BlogArticleDetailVM> selectById(@PathVariable(value = "id", required = false) String id);

    /**
     * 博文列表，不需要登陆
     *
     * @param page 起始页
     * @param size 每页条数
     * @param tag  标签
     * @param type 类别
     * @return BlogArticleSetVM
     */
    @RequestMapping(value = "/nologin/blog_article", method = RequestMethod.GET)
    Response<PageInfo<BlogArticleDetailVM>> selectNologin(@RequestParam(value = "page", required = false) Integer page,
                                                   @RequestParam(value = "size", required = false) Integer size,
                                                   @RequestParam(value = "tag", required = false) String tag,
                                                   @RequestParam(value = "type", required = false) String type);

    /**
     * 博文列表，需要登陆
     *
     * @param page 起始页
     * @param size 每页条数
     * @return BlogArticleSetVM
     */
    @RequestMapping(value = "/blog_article", method = RequestMethod.GET)
    Response<PageInfo<BlogArticleDetailVM>> select(@RequestParam(value = "page", required = false) Integer page,
                                                   @RequestParam(value = "size", required = false) Integer size);


    /**
     * 最新文章推荐
     *
     * @return List<BlogArticleDetailVM>
     */
    @RequestMapping(value = "/nologin/latest_articles", method = RequestMethod.GET)
    Response<List<BlogArticleDetailVM>> latestRecommendedArticles();

    /**
     * 文章归档查询
     * todo 后期改
     *
     * @return List<BlogArticleDetailVM>
     */
    @RequestMapping(value = "/nologin/archive_query", method = RequestMethod.GET)
    Response<Map<Integer, List<BlogArticleDetailVM>>> archiveQuery();
}
