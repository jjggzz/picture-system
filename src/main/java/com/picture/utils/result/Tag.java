package com.picture.utils.result;

import lombok.Data;

@Data
public class Tag {
    /**
     * 标签名
     */
    private String tag_name;
    /**
     * 置信度，越大越可信
     */
    private Integer tag_confidence;
}
