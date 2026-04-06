package com.finance.dashboard.exception;

import lombok.Data;

@Data
public class BaseException extends  RuntimeException{
    private final String errorCode;
    private final int status;

    public BaseException(String message, String errorCode, int status){
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

}
