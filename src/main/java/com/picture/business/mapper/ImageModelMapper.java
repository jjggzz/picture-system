package com.picture.business.mapper;

import com.picture.business.model.ImageModel;
import com.picture.business.model.ImageModelExample;
import com.picture.business.vo.request.SelectImageListRequestVO;
import com.springboot.simple.jdbc.mapper.BaseModelMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageModelMapper extends BaseModelMapper<ImageModel,ImageModelExample> {

    List<ImageModel> selectImageList(@Param("condition") SelectImageListRequestVO selectImageListRequestVO);

}