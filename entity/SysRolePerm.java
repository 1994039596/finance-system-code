package com.supermarket.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_role_perm")
public class SysRolePerm {
    private Long id;
    private Long roleId;
    private Long permId;
    private LocalDateTime createdAt;
    private Long createdBy;
}








