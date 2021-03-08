package com.picture.business.service;

import com.picture.business.model.TagImageModel;
import com.picture.business.model.TagImageModelExample;
import com.springboot.simple.jdbc.service.BaseService;

import java.util.List;

public interface TagImageService extends BaseService<TagImageModel, TagImageModelExample> {

    void saveList(List<TagImageModel> tagImageModelList);

}
