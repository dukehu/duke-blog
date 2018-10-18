package com.duke.microservice.blog.service;

import com.duke.framework.CoreConstants;
import com.duke.framework.exception.BusinessException;
import com.duke.framework.security.AuthUserDetails;
import com.duke.framework.utils.SecurityUtils;
import com.duke.framework.utils.ValidationUtils;
import com.duke.microservice.blog.BlogConstants;
import com.duke.microservice.blog.domain.basic.BlogLabel;
import com.duke.microservice.blog.mapper.basic.BlogLabelMapper;
import com.duke.microservice.blog.mapper.extend.BlogLabelExtendMapper;
import com.duke.microservice.blog.vm.BlogLabelVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Created duke on 2018/6/29
 */
@Service
@Transactional
@Slf4j
public class BlogLabelService {

    @Autowired
    private BlogLabelExtendMapper blogLabelExtendMapper;

    @Autowired
    private BlogLabelMapper blogLabelMapper;

    /**
     * 查询当前登陆用户的标签列表
     *
     * @return List<BlogLabelVM>
     */
    @Transactional(readOnly = true)
    public List<BlogLabelVM> select() {
        String userId = "b66a3fe7-8fdd-11e8-bcd8-18dbf21f6c28";
        try {
            // 获取用户信息
            AuthUserDetails authUserDetails = SecurityUtils.getCurrentUserInfo();
            userId = authUserDetails.getUserId();
        } catch (Exception e) {
            log.info("无登录用户，查询所有");
        }

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

    /**
     * 新增修改
     *
     * @param id   主键
     * @param name 名称
     * @param type 新增或者保存
     */
    public void saveOrUpdate(String id, String name, String type) {
        ValidationUtils.notEmpty(name, "labelName", "标签名称不能为空！");

        // 获取用户信息
        AuthUserDetails authUserDetails = SecurityUtils.getCurrentUserInfo();
        String userId = authUserDetails.getUserId();
        Date date = new Date();

        BlogLabel blogLabel;
        if (CoreConstants.UPDATE.equalsIgnoreCase(type)) {
            // 更新
            blogLabel = this.exist(id);
        } else {
            blogLabel = new BlogLabel();
            blogLabel.setId(UUID.randomUUID().toString());
            blogLabel.setStatus(BlogConstants.DELETE_STATUS.NOT_DELETE.getCode());
            blogLabel.setCreateTime(date);
        }

        blogLabel.setName(name);
        blogLabel.setModifyTime(date);
        blogLabel.setUserId(userId);

        if (CoreConstants.UPDATE.equalsIgnoreCase(type)) {
            blogLabelMapper.updateByPrimaryKey(blogLabel);
        } else {
            blogLabelMapper.insert(blogLabel);
        }
    }

    /**
     * 删除（假删除）
     *
     * @param id 主键
     */
    public void delete(String id) {
        this.exist(id);
        blogLabelExtendMapper.deleteById(id);
    }

    /**
     * 校验id有效性
     *
     * @param id 主机那
     * @return BlogLabel
     */
    @Transactional(readOnly = true)
    public BlogLabel exist(String id) {
        ValidationUtils.notEmpty(id, "labelId", "标签id不能为空！");
        BlogLabel blogLabel = blogLabelMapper.selectByPrimaryKey(id);
        int status = blogLabel.getStatus();
        if (ObjectUtils.isEmpty(blogLabel) || BlogConstants.DELETE_STATUS.DELETED.getCode() == status) {
            throw new BusinessException("id为" + id + "的标签不存在或已删除！");
        }
        return blogLabel;
    }
}
