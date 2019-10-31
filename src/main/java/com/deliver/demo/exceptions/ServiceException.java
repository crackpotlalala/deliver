package com.deliver.demo.exceptions;

import com.deliver.demo.enums.StatusCodeEnum;

public class ServiceException extends RuntimeException {
    private String message;

    private int code;

    public ServiceException(String message) {
        this(message, 400);
    }

    public ServiceException(StatusCodeEnum codeEnum) {
        this(codeEnum.getDesc(), codeEnum.getCode());
    }

    public ServiceException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return code;
    }

}
