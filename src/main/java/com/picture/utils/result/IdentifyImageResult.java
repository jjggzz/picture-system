package com.picture.utils.result;

import lombok.Data;

@Data
public class IdentifyImageResult {
    /**
     * 返回0代表成功
     */
    private Integer ret;
    /**
     * ret不为0时包含错误信息
     */
    private String msg;
    /**
     * 返回的数据
     */
    private IdentifyImageData data;
}
