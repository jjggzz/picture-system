package com.picture.business.controller;

import com.github.pagehelper.PageInfo;
import com.picture.business.async.FileListAsyncParsingHandler;
import com.picture.business.service.ImageService;
import com.picture.business.vo.request.SelectImageListRequestVO;
import com.picture.business.vo.response.ImageInfoResponseVO;
import com.springboot.simple.base.async.EventPusher;
import com.springboot.simple.base.async.event.BaseEvent;
import com.springboot.simple.controller.BaseController;
import com.springboot.simple.res.ResultEntity;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/image")
public class ImageController extends BaseController {

    @Resource
    private ImageService imageService;

    @Resource
    private EventPusher eventPusher;

    /**
     * 解析图片
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadImage")
    public ResultEntity<Void> uploadImage(MultipartFile file) throws Exception {
        return result(file, imageService::parsingImage);
    }

    /**
     * 解析图片列表
     * @param fileList
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadImageList")
    public ResultEntity<Void> uploadImageList(List<MultipartFile> fileList) throws Exception {
        FileListAsyncParsingHandler fileListAsyncParsingHandler = new FileListAsyncParsingHandler(fileList,imageService::parsingImage);
        eventPusher.eventPush(new BaseEvent(fileListAsyncParsingHandler));
        return ResultEntity.success();
    }


    /**
     * 分页获取图片
     * @param selectImageListRequestVO
     * @return
     */
    @GetMapping("/selectImageList")
    public ResultEntity<PageInfo<ImageInfoResponseVO>> selectImageList(@RequestBody SelectImageListRequestVO selectImageListRequestVO) throws Exception {
        return result(selectImageListRequestVO,imageService::selectImageList);
    }

    /**
     * 通过业务id获取图片
     * @param accessKey
     */
    @GetMapping("/getImage/{accessKey}")
    public void getImage(@PathVariable("accessKey") Long accessKey) throws IOException {
        File file = imageService.LoadFileByAccessKey(accessKey);
        if (Objects.nonNull(file)) {
            HttpServletResponse response = getResponse();
            FileUtils.copyFile(file,response.getOutputStream());
        }
    }

    /**
     * 改变图片收藏状态
     * @param accessKey
     * @return
     * @throws Exception
     */
    @GetMapping("/focusImage/{accessKey}")
    public ResultEntity<Void> changeFocusImage(@PathVariable("accessKey") Long accessKey) throws Exception{
        return result(accessKey, imageService::changeFocusImage);
    }


}
