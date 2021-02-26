package com.picture.business.service;

import com.picture.business.model.TagModel;
import com.picture.business.model.TagModelExample;
import com.springboot.simple.jdbc.service.BaseService;

import java.util.List;
import java.util.Map;

public interface TagService extends BaseService<TagModel, TagModelExample> {
    /**
     * 通过标签获取主键,其中key代表标签名称，value为主键
     * @param tags
     * @return
     */
    Map<String, Long> getTagIdByTagName(List<String> tags);

    /**
     * 通过accesskey查询tag信息
     * @param accessKey
     * @return
     */
    TagModel getTagByAccessKey(Long accessKey);
}
