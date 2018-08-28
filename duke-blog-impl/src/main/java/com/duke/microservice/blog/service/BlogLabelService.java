package com.duke.microservice.blog.service;

import com.duke.microservice.blog.domain.basic.BlogLabel;
import com.duke.microservice.blog.mapper.extend.BlogLabelExtendMapper;
import com.duke.microservice.blog.vm.BlogLabelVM;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created duke on 2018/6/29
 */
@Service
@Transactional
public class BlogLabelService {

    @Autowired
    private BlogLabelExtendMapper blogLabelExtendMapper;

    /**
     * 查询当前登陆用户的标签列表
     *
     * @return List<BlogLabelVM>
     */
    @Transactional(readOnly = true)
    public List<BlogLabelVM> select() {
        // todo 获取用户信息
        String userId = "duke";

        List<BlogLabelVM> blogLabelVMS = new ArrayList<>();
        List<BlogLabel> blogLabels = blogLabelExtendMapper.selectByUserId(userId);
        if (!CollectionUtils.isEmpty(blogLabels)) {
            for (BlogLabel blogLabel : blogLabels) {
                BlogLabelVM blogLabelVM = new BlogLabelVM();
                BeanUtils.copyProperties(blogLabel, blogLabelVM);
                blogLabelVMS.add(blogLabelVM);
            }
        }
        return blogLabelVMS;
    }

    /**
     * 批量保存
     *
     * @param labels 标签集合对象
     */
    void batchSave(List<BlogLabel> labels) {
        if (!CollectionUtils.isEmpty(labels)) {
            blogLabelExtendMapper.batchSave(labels);
        }
    }

    /**
     * 根据博文id集合查找
     *
     * @param articleIds 博文id集合
     * @return map
     */
    @Transactional(readOnly = true)
    public Map<String, List<BlogLabelVM>> selectByArticleIds(List<String> articleIds) {
        Map<String, List<BlogLabelVM>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(articleIds)) {
            List<Map<String, String>> blogLabels = blogLabelExtendMapper.selectByArticleIds(articleIds);
            blogLabels.forEach(tmp -> {
                String articleId = tmp.get("articleId");
                String id = tmp.get("id");
                String labelName = tmp.get("labelName");
                BlogLabelVM blogLabelVM = new BlogLabelVM(id, labelName);
                if (map.containsKey(articleId)) {
                    map.get(articleId).add(blogLabelVM);
                } else {
                    List<BlogLabelVM> blogLabelVMS = new ArrayList<>();
                    blogLabelVMS.add(blogLabelVM);
                    map.put(articleId, blogLabelVMS);
                }
            });
        }
        return map;
    }
}
