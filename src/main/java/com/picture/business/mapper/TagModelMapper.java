package com.picture.business.mapper;

import com.picture.business.model.TagModel;
import com.picture.business.model.TagModelExample;
import com.springboot.simple.jdbc.mapper.BaseModelMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagModelMapper extends BaseModelMapper<TagModel, TagModelExample> {

}