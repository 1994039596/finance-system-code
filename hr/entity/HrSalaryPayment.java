package com.supermarket.finance.hr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("hr_salary_payment")
public class HrSalaryPayment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long employeeId;
    private LocalDateTime paymentTime;
    private Long salaryId;
    private String bankAccount;
    private String arrivalStatus; // ARRIVED/PENDING/FAILED

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}


