package com.picture.business.vo.request;

import com.picture.utils.page.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SelectImageListRequestVO extends Page {
    private String fileName;
    private Date startTime;
    private Date endTime;
    private Boolean focus;
    private List<Long> tagAccessKeys;
}
