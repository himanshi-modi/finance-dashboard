package com.finance.dashboard.exception;

import com.finance.dashboard.entity.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error("BAD_REQUEST",ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex){
        Map<String,Object> response= new HashMap<>();
        response.put("error","INVALID_CREDENTIALS");
        response.put("message","Email or password is incorrect");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

    }



    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException ex){
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(error("CONFLICT",ex.getMessage()));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleIllegalState(AccessDeniedException ex){
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(error("FORBIDDEN",ex.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err-> errors.put(err.getField(),err.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex){
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error("INTERNAL_SERVER_ERROR",ex.getMessage()));
    }

    @ExceptionHandler(RoleAccessDeniedException.class)
    public ResponseEntity<?> handleRoleAccess(RoleAccessDeniedException ex){
        Map<String,Object> response=new HashMap<>();
        response.put("error","ACCESS_DENIED");
        response.put("message",ex.getMessage());
        response.put("requiredRole",ex.getRequiredRole());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    private Map<String,String> error(String code,String message){
        Map<String,String> response=new HashMap<>();
        response.put("error",code);
        response.put("message",message);
        return response;
    }






}
