package com.picture.business.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.picture.business.exception.BusinessExceptionEnum;
import com.picture.business.mapper.ImageModelMapper;
import com.picture.business.model.ImageModel;
import com.picture.business.model.ImageModelExample;
import com.picture.business.model.TagModel;
import com.picture.business.service.ImageService;
import com.picture.business.service.TagService;
import com.picture.business.vo.request.SelectImageListRequestVO;
import com.picture.business.vo.response.ImageInfoResponseVO;
import com.picture.constant.FileConstant;
import com.picture.utils.IdUtils;
import com.picture.utils.TencentApiUtils;
import com.picture.utils.result.IdentifyImageResult;
import com.picture.utils.result.Tag;
import com.springboot.simple.jdbc.service.impl.BaseServiceImpl;
import com.springboot.simple.res.ResultEntity;
import com.springboot.simple.support.constant.NumberConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl extends BaseServiceImpl<ImageModel, ImageModelMapper, ImageModelExample> implements ImageService {

    @Resource
    private TagService tagService;

    @Resource
    @Override
    public void setMapper(ImageModelMapper mapper) {
        super.setMapper(mapper);
        super.setExampleSupplier(ImageModelExample::new);
        super.setModelSupplier(ImageModel::new);
    }

    @Override
    public ResultEntity<Void> parsingImage(MultipartFile file) throws IOException {

        String filePath = FilenameUtils.concat(FileConstant.IMAGE_PATH,
                IdUtils.getInstance().nextId() + "." + FilenameUtils.getExtension(file.getOriginalFilename()));

        // 解析图片
        IdentifyImageResult identifyImageResult = TencentApiUtils.identifyImage(file.getBytes());
        // 封装参数
        ImageModel imageModel = this.newModel();
        imageModel.setAccessKey(IdUtils.getInstance().nextId());
        // 如果不等于0解析失败,插入未定义
        if (identifyImageResult.getRet() != NumberConstant.INTEGER_0) {
            imageModel.setFileTag("0");
        } else {
            // 获取置信度最高的三个标签
            List<String> tags = identifyImageResult.getData().getTag_list()
                    .stream()   //转换成流对象
                    .sorted(Comparator.comparingInt(Tag::getTag_confidence).reversed()) //比较置信度,倒序
                    .map(Tag::getTag_name)
                    .collect(Collectors.toList());
            // 通过标签名获取标签id,前三个
            Map<String,Long> map = tagService.getTagIdByTagName(tags);
            List<String> ids = map.values().stream().map(Object::toString).limit(3).collect(Collectors.toList());
            imageModel.setFileTag(StringUtils.join(ids,','));
        }
        imageModel.setUploadTime(new Date(System.currentTimeMillis()));
        imageModel.setFilePath(filePath);
        imageModel.setFileName(file.getOriginalFilename());
        imageModel.setFileSize(file.getSize());
        insert(imageModel);
        // 保存文件
        FileUtils.copyToFile(file.getInputStream(),new File(filePath));
        return ResultEntity.success();
    }

    @Override
    public ResultEntity<PageInfo<ImageInfoResponseVO>> selectImageList(SelectImageListRequestVO selectImageListRequestVO) {

        Page<ImageModel> imageModels = PageHelper.startPage(selectImageListRequestVO.getPageNum(), selectImageListRequestVO.getPageSize());

        ImageModelExample imageModelExample = this.newExample();
        ImageModelExample.Criteria criteria = imageModelExample.createCriteria();
        criteria.andDeletedEqualTo(false);
        if (StringUtils.isNotEmpty(selectImageListRequestVO.getFileName())) {
            criteria.andFileNameLike("%" + selectImageListRequestVO.getFileName() + "%");
        }
        if (Objects.nonNull(selectImageListRequestVO.getStartTime())) {
            criteria.andUploadTimeGreaterThanOrEqualTo(selectImageListRequestVO.getStartTime());
        }
        if (Objects.nonNull(selectImageListRequestVO.getEndTime())) {
            criteria.andUploadTimeLessThanOrEqualTo(selectImageListRequestVO.getEndTime());
        }
        if (Objects.nonNull(selectImageListRequestVO.getTagAccessKey())) {
            TagModel tagModel = tagService.getTagByAccessKey(selectImageListRequestVO.getTagAccessKey());
            BusinessExceptionEnum.NO_QUERY_DATA.assertNotNull(tagModel,"该标签不存在");

            criteria.andFileTagLike("%" + tagModel.getId() + "%");
        }
        this.mapper.selectByExample(imageModelExample);

        List<ImageInfoResponseVO> list = imageModels.stream().map(imageModel -> {
            ImageInfoResponseVO imageInfoResponseVO = new ImageInfoResponseVO();
            // 封装参数
            BeanUtils.copyProperties(imageModel, imageInfoResponseVO);
            return imageInfoResponseVO;
        }).collect(Collectors.toList());
        PageInfo<ImageInfoResponseVO> imageInfoResponseVOPageInfo = new PageInfo<>(list);
        imageInfoResponseVOPageInfo.setPageNum(imageModels.getPageNum());
        imageInfoResponseVOPageInfo.setPageSize(imageModels.getPageSize());
        imageInfoResponseVOPageInfo.setTotal(imageModels.getTotal());
        return ResultEntity.success(imageInfoResponseVOPageInfo);
    }

    @Override
    public File LoadFileByAccessKey(Long accessKey) {
        ImageModelExample imageModelExample = new ImageModelExample();
        imageModelExample.createCriteria()
                .andDeletedEqualTo(false)
                .andAccessKeyEqualTo(accessKey);
        List<ImageModel> imageModels = selectByExample(imageModelExample);
        if (CollectionUtils.isEmpty(imageModels)) {
            return null;
        }
        ImageModel imageModel = imageModels.get(0);
        return FileUtils.getFile(imageModel.getFilePath());
    }
}
