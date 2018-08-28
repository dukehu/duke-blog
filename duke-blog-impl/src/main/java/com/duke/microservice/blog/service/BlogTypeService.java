package com.duke.microservice.blog.service;

import com.duke.microservice.blog.vm.BlogTypeVM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created duke on 2018/8/27
 */
@Transactional
@Service
public class BlogTypeService {

    /**
     * 根据博文id集合查找
     *
     * @param articleIds 博文id集合
     * @return map
     */
    public Map<String, List<BlogTypeVM>> selectByArticleIds(List<String> articleIds) {
        Map<String, List<BlogTypeVM>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(articleIds)) {
            // todo
        }
        return map;
    }
}
