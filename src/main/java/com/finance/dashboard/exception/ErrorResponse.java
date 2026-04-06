package com.finance.dashboard.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String errorCode;
    private String message;
    private Object details;
    private LocalDateTime timeStamp;
    private int status;
}
