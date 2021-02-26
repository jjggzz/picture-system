package com.picture.business.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class ImageInfoResponseVO {
    private Long accessKey;
    private Date uploadTime;
    private String fileName;
    private Long fileSize;
}
