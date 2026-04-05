package com.finance.dashboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex){
        Map<String,Object> response= new HashMap<>();
        response.put("error","INVALID_CREDENTIALS");
        response.put("message","Email or password is incorrect");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex){
        Map<String,Object> response=new HashMap<>();
        response.put("error" , "BAD_REQUEST");
        response.put("message",ex.getMessage());
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
