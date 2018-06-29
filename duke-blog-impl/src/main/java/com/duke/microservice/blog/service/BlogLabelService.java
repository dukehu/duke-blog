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
import java.util.List;

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
}
