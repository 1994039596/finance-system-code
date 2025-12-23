package com.supermarket.finance.hr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("hr_salary_info")
public class HrSalaryInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long employeeId;
    private BigDecimal basicSalary;
    private BigDecimal bonus;
    private BigDecimal deduction;
    private String period; // YYYYMM

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}


