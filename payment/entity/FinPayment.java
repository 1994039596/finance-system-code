package com.supermarket.finance.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_payment")
public class FinPayment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String paymentNo;
    private Long poId;
    private Long supplierId;
    private Long cashierId;
    private LocalDateTime payDate;
    private BigDecimal amount;
    private String method;
    private String bankAccount;
    private String status;
    private String remark;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








