package com.picture.business.vo.request;

import com.picture.utils.page.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class SelectImageListRequestVO extends Page {
    private String fileName;
    private Date startTime;
    private Date endTime;
    private Long tagAccessKey;
}
