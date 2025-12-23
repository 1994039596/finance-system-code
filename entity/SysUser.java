package com.supermarket.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    private Long id;
    private String username;
    private String passwordHash;
    private String displayName;
    private String phone;
    private String email;
    private Integer status;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








