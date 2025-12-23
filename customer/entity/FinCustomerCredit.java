package com.supermarket.finance.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_customer_credit")
public class FinCustomerCredit {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long customerId;
    private BigDecimal creditLimit;
    private BigDecimal currentPayable;
    private String repaymentHistory;
    private String adjustmentRecord;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








