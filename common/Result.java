package com.supermarket.finance.common;

import lombok.Data;

// 统一接口返回格式
@Data
public class Result<T> {
    private Integer code;    // 响应码：200成功，500失败
    private String msg;      // 响应信息
    private T data;          // 响应数据

    // 成功返回
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 失败返回
    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static <T> Result<T> unauthorized(String msg) {
        Result<T> result = new Result<>();
        result.setCode(401);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static <T> Result<T> forbidden(String msg) {
        Result<T> result = new Result<>();
        result.setCode(403);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}