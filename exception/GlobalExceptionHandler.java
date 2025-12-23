package com.supermarket.finance.exception;

import com.supermarket.finance.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 全局异常捕获，返回统一格式
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDenied(AccessDeniedException e) {
        return Result.forbidden("无权限访问");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace(); // 打印异常栈（调试用）
        return Result.fail("系统异常：" + e.getMessage());
    }
}