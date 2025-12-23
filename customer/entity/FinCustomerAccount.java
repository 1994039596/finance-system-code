package com.supermarket.finance.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_customer_account")
public class FinCustomerAccount {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long customerId;
    private String bank;
    private String accountNo;
    private BigDecimal balance;
    private String status;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








