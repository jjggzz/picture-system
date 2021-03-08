package com.picture.business.mapper;

import com.picture.business.model.TagImageModel;
import com.picture.business.model.TagImageModelExample;
import com.springboot.simple.jdbc.mapper.BaseModelMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagImageModelMapper  extends BaseModelMapper<TagImageModel, TagImageModelExample> {

}