package com.saas.admin.config;

import com.saas.admin.common.R;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<Void> handleAccessDeniedException(AccessDeniedException e) {
        return R.forbidden("无权限访问");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<Void> handleAuthenticationException(AuthenticationException e) {
        return R.unauthorized("未登录或登录已过期");
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleHttpMessageNotWritableException(HttpMessageNotWritableException e) {
        System.out.println("Serialization error: " + e.getMessage());
        if (e.getCause() != null) {
            System.out.println("Cause: " + e.getCause().getMessage());
            e.getCause().printStackTrace();
        }
        return R.error("序列化错误");
    }

    @ExceptionHandler(InvalidDefinitionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleInvalidDefinitionException(InvalidDefinitionException e) {
        System.out.println("JSON serialization error: " + e.getMessage());
        e.printStackTrace();
        return R.error("序列化错误");
    }

    @ExceptionHandler(JsonMappingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleJsonMappingException(JsonMappingException e) {
        System.out.println("JSON mapping error: " + e.getMessage());
        e.printStackTrace();
        return R.error("序列化错误");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleRuntimeException(RuntimeException e) {
        System.out.println("Runtime exception: " + e.getMessage());
        e.printStackTrace();
        return R.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleException(Exception e) {
        System.out.println("Exception: " + e.getMessage());
        e.printStackTrace();
        return R.error("系统繁忙，请稍后再试");
    }
}