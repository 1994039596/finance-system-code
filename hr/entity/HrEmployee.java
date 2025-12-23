package com.supermarket.finance.hr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("hr_employee")
public class HrEmployee {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String department;
    private String position;
    private LocalDateTime hireDate;
    private String status; // ACTIVE/LEFT

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








