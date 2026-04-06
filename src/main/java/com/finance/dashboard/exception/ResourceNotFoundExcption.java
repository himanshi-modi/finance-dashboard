package com.finance.dashboard.exception;

public class ResourceNotFoundExcption extends BaseException{


    public ResourceNotFoundExcption(String message) {
        super(message, "RESOURCE_NOT_FOUND", 404);
    }
}
