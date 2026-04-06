package com.finance.dashboard.exception;

import com.finance.dashboard.entity.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildError("INVALID_CRDENTIALS",
                "Email or password is incorrect",null, HttpStatus.UNAUTHORIZED));

    }



    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError("CONFLICT",
                ex.getMessage(),null, HttpStatus.CONFLICT));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                buildError(
                        "FORBIDDEN",
                        "You do not have permission to perform this action",
                        null,
                        HttpStatus.FORBIDDEN
                )
        );

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                buildError(
                        "VALIDATION_ERROR",
                        "Invalid input data",
                        errors,
                        HttpStatus.BAD_REQUEST
                )
        );
    }


    @ExceptionHandler(RoleAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleRoleAccess(RoleAccessDeniedException ex) {

        Map<String, Object> details = new HashMap<>();
        details.put("requiredRole", ex.getRequiredRole());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                buildError(
                        "ACCESS_DENIED",
                        ex.getMessage(),
                        details,
                        HttpStatus.FORBIDDEN
                )
        );
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                buildError(
                        "INTERNAL_SERVER_ERROR",
                        "Something went wrong",
                        null,
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
        );
    }

    private ErrorResponse buildError(String code, String message, Object details, HttpStatus status) {
        return ErrorResponse.builder()
                .errorCode(code)
                .message(message)
                .details(details)
                .timeStamp(LocalDateTime.now())
                .status(status.value())
                .build();
    }






}
