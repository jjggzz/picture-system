package com.picture.business.service.impl;

import com.picture.business.mapper.TagImageModelMapper;
import com.picture.business.model.TagImageModel;
import com.picture.business.model.TagImageModelExample;
import com.picture.business.service.TagImageService;
import com.springboot.simple.jdbc.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagImageServiceImpl extends BaseServiceImpl<TagImageModel, TagImageModelMapper, TagImageModelExample> implements TagImageService {

    @Resource
    @Override
    public void setMapper(TagImageModelMapper mapper) {
        super.setMapper(mapper);
        super.setExampleSupplier(TagImageModelExample::new);
        super.setModelSupplier(TagImageModel::new);
    }

    @Override
    public void saveList(List<TagImageModel> tagImageModelList) {
        tagImageModelList.forEach(this::insert);

    }
}
