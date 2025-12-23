package com.supermarket.finance.purchase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_purchase_order")
public class PurchaseOrder {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;
    private Long supplierId;
    private Long bookId;
    private String period;
    private String project;
    private Long customerId;
    private String description;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private String status;
    private Long approvedBy;
    private LocalDateTime approvedAt;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








