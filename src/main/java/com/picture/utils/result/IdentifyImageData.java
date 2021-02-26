package com.picture.utils.result;

import lombok.Data;

import java.util.List;

@Data
public class IdentifyImageData {
    /**
     * 标签列表
     */
    private List<Tag> tag_list;
}
