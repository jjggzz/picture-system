package com.picture.business.exception;

import com.springboot.simple.exception.BusinessExceptionAssert;

public enum BusinessExceptionEnum implements BusinessExceptionAssert {
    NO_QUERY_DATA(1000,"未检索到数据");



    BusinessExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private final String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
