package com.duke.microservice.blog.service;

import com.duke.framework.CoreConstants;
import com.duke.framework.exception.BusinessException;
import com.duke.framework.security.AuthUserDetails;
import com.duke.framework.utils.SecurityUtils;
import com.duke.framework.utils.ValidationUtils;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.domain.basic.BlogType;
import com.duke.microservice.blog.domain.extend.ArticleCount;
import com.duke.microservice.blog.mapper.basic.BlogTypeMapper;
import com.duke.microservice.blog.mapper.extend.BlogTypeExtendMapper;
import com.duke.microservice.blog.vm.BlogTypeVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created duke on 2018/8/27
 */
@Service
@Transactional
@Slf4j
public class BlogTypeService {

    @Autowired
    private BlogTypeExtendMapper blogTypeExtendMapper;

    @Autowired
    private BlogTypeMapper blogTypeMapper;

    /**
     * 根据博文id集合查找
     *
     * @param articleIds 博文id集合
     * @return map
     */
    @Transactional(readOnly = true)
    public Map<String, List<BlogTypeVM>> selectByArticleIds(List<String> articleIds) {
        Map<String, List<BlogTypeVM>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(articleIds)) {
            List<Map<String, String>> blogTypes = blogTypeExtendMapper.selectByArticleIds(articleIds);
            blogTypes.forEach(tmp -> {
                String articleId = tmp.get("articleId");
                String id = tmp.get("id");
                String typeName = tmp.get("labelName");
                BlogTypeVM blogTypeVM = new BlogTypeVM(id, typeName, 0);
                if (map.containsKey(articleId)) {
                    map.get(articleId).add(blogTypeVM);
                } else {
                    List<BlogTypeVM> blogTypeVMS = new ArrayList<>();
                    blogTypeVMS.add(blogTypeVM);
                    map.put(articleId, blogTypeVMS);
                }
            });
        }
        return map;
    }

    /**
     * 批量保存
     *
     * @param types 类别集合对象
     */
    void batchSave(List<BlogTypeVM> types) {
        if (!CollectionUtils.isEmpty(types)) {
            blogTypeExtendMapper.batchSave(types);
        }
    }

    /**
     * 查询当前登陆用户的类别列表
     *
     * @param needArticleCount 是否需要文章数
     * @return List<BlogTypeVM>
     */
    @Transactional(readOnly = true)
    public List<BlogTypeVM> select(Boolean needArticleCount) {
        String userId = "b66a3fe7-8fdd-11e8-bcd8-18dbf21f6c28";
        try {
            // 获取用户信息
            AuthUserDetails authUserDetails = SecurityUtils.getCurrentUserInfo();
            userId = authUserDetails.getUserId();
        } catch (Exception e) {
            log.info("无登录用户，查询所有");
        }

        List<BlogTypeVM> blogTypeVMS = new ArrayList<>();
        List<BlogType> blogTypes = blogTypeExtendMapper.selectByUserId(userId);
        Map<String, Integer> articleCountMap = new HashMap<>();
        if (needArticleCount && !CollectionUtils.isEmpty(blogTypes)) {
            List<String> typeIds = blogTypes.stream().map(BlogType::getId).collect(Collectors.toList());
            articleCountMap = this.getArticleCountsByTypeIds(typeIds);
        }
        if (!CollectionUtils.isEmpty(blogTypes)) {
            for (BlogType blogType : blogTypes) {
                String typeId = blogType.getId();
                Integer articleCount = 0;
                if (needArticleCount) {
                    assert articleCountMap != null;
                    if (articleCountMap.containsKey(typeId)) {
                        articleCount = articleCountMap.get(typeId);
                    }
                }
                BlogTypeVM blogTypeVM = new BlogTypeVM(typeId, blogType.getName(), articleCount);
                blogTypeVMS.add(blogTypeVM);
            }
        }
        return blogTypeVMS;
    }


    private Map<String, Integer> getArticleCountsByTypeIds(List<String> typeIds) {
        Map<String, Integer> articleCountMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(typeIds)) {
            List<ArticleCount> articleCounts = blogTypeExtendMapper.selectArticleCountsByTypeIds(typeIds);
            articleCountMap = articleCounts.stream().collect(Collectors.toMap(ArticleCount::getTypeId, ArticleCount::getArticleCount));
        }
        return articleCountMap;
    }

    /**
     * 新增修改
     *
     * @param id   主键
     * @param name 名称
     * @param type 新增后者修改
     */
    public void saveOrUpdate(String id, String name, String type) {
        ValidationUtils.notEmpty(name, "typeName", "类别名称不能为空！");

        // 获取用户信息
        AuthUserDetails authUserDetails = SecurityUtils.getCurrentUserInfo();
        String userId = authUserDetails.getUserId();
        Date date = new Date();

        BlogType blogType;
        if (CoreConstants.UPDATE.equalsIgnoreCase(type)) {
            // 更新
            blogType = this.exist(id);
        } else {
            blogType = new BlogType();
            blogType.setId(UUID.randomUUID().toString());
            blogType.setStatus(BlogConstants.DELETE_STATUS.NOT_DELETE.getCode());
            blogType.setCreateTime(date);
        }

        blogType.setName(name);
        blogType.setModifyTime(date);
        blogType.setUserId(userId);

        if (CoreConstants.UPDATE.equalsIgnoreCase(type)) {
            blogTypeMapper.updateByPrimaryKey(blogType);
        } else {
            blogTypeMapper.insert(blogType);
        }
    }

    /**
     * 删除（假删除）
     *
     * @param id 主键
     */
    public void delete(String id) {
        this.exist(id);
        blogTypeExtendMapper.deleteById(id);
    }


    @Transactional(readOnly = true)
    public BlogType exist(String id) {
        ValidationUtils.notEmpty(id, "typeId", "类别id不能为空！");
        BlogType blogType = blogTypeMapper.selectByPrimaryKey(id);
        int status = blogType.getStatus();
        if (ObjectUtils.isEmpty(blogType) || BlogConstants.DELETE_STATUS.DELETED.getCode() == status) {
            throw new BusinessException("id为" + id + "的类别不存在或已删除！");
        }
        return blogType;
    }
}
