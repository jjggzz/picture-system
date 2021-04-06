package com.picture.business.service;

import com.github.pagehelper.PageInfo;
import com.picture.business.model.ImageModel;
import com.picture.business.model.ImageModelExample;
import com.picture.business.vo.request.SelectImageListRequestVO;
import com.picture.business.vo.response.ImageInfoResponseVO;
import com.springboot.simple.jdbc.service.BaseService;
import com.springboot.simple.res.ResultEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


public interface ImageService extends BaseService<ImageModel, ImageModelExample> {

    /**
     * 解析文件
     * @param file
     */
    ResultEntity<Void> parsingImage(MultipartFile file) throws IOException;

    /**
     * 分页查询图片信息
     * @param selectImageListRequestVO
     * @return
     */
    ResultEntity<PageInfo<ImageInfoResponseVO>> selectImageList(SelectImageListRequestVO selectImageListRequestVO);

    /**
     * 通过accessKey加载文件
     * @param accessKey
     * @return
     */
    File LoadFileByAccessKey(Long accessKey);

    /**
     * 改变图片收藏状态
     * @param accessKey
     * @return
     */
    ResultEntity<Void> changeFocusImage(Long accessKey);

}
