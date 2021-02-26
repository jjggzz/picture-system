package com.picture.business.mapper;

import com.picture.business.model.ImageModel;
import com.picture.business.model.ImageModelExample;
import com.springboot.simple.jdbc.mapper.BaseModelMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageModelMapper extends BaseModelMapper<ImageModel,ImageModelExample> {

}