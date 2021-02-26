package com.picture.business.service.impl;

import com.picture.business.mapper.TagModelMapper;
import com.picture.business.model.ImageModel;
import com.picture.business.model.ImageModelExample;
import com.picture.business.model.TagModel;
import com.picture.business.model.TagModelExample;
import com.picture.business.service.TagService;
import com.picture.utils.IdUtils;
import com.springboot.simple.jdbc.service.impl.BaseServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl extends BaseServiceImpl<TagModel, TagModelMapper, TagModelExample> implements TagService {

    @Resource
    @Override
    public void setMapper(TagModelMapper mapper) {
        super.setMapper(mapper);
        super.setExampleSupplier(TagModelExample::new);
        super.setModelSupplier(TagModel::new);
    }

    @Override
    public Map<String, Long> getTagIdByTagName(List<String> tags) {
        return tags.stream().reduce(new HashMap<>(), (map, tag) -> {
            // 查询结果，放入map中
            TagModelExample tagModelExample = this.newExample();
            tagModelExample.createCriteria()
                    .andDeletedEqualTo(false)
                    .andTagNameEqualTo(tag);
            List<TagModel> tagModels = selectByExample(tagModelExample);
            if (CollectionUtils.isNotEmpty(tagModels)) {
                map.put(tag,tagModels.get(0).getId());
            } else {
                TagModel tagModel = new TagModel();
                tagModel.setAccessKey(IdUtils.getInstance().nextId());
                tagModel.setTagName(tag);
                insert(tagModel);
                map.put(tag,tagModel.getId());
            }
            return map;
        }, (stringLongHashMap, stringLongHashMap2) -> {
            // 并行计算时合并结果集
            stringLongHashMap.putAll(stringLongHashMap2);
            return stringLongHashMap;
        });
    }

    @Override
    public TagModel getTagByAccessKey(Long accessKey) {
        TagModelExample tagModelExample = new TagModelExample();
        tagModelExample.createCriteria()
                .andDeletedEqualTo(false)
                .andAccessKeyEqualTo(accessKey);
        List<TagModel> tagModels = this.mapper.selectByExample(tagModelExample);
        // 返回集合中的第一个元素，否则返回null
        return tagModels.stream().findFirst().orElse(null);
    }
}
