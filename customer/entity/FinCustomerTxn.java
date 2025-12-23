package com.supermarket.finance.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_customer_txn")
public class FinCustomerTxn {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long customerId;
    private Long customerAccountId;
    private Long orderId;
    private BigDecimal amount;
    private String type; // RECEIVABLE/PAYMENT/REFUND/ADJUSTMENT
    private LocalDateTime txnTime;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








