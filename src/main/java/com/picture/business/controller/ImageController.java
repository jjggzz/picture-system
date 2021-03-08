package com.picture.business.controller;

import com.github.pagehelper.PageInfo;
import com.picture.business.service.ImageService;
import com.picture.business.vo.request.SelectImageListRequestVO;
import com.picture.business.vo.response.ImageInfoResponseVO;
import com.springboot.simple.controller.BaseController;
import com.springboot.simple.res.ResultEntity;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/image")
public class ImageController extends BaseController {

    @Resource
    private ImageService imageService;

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



}
