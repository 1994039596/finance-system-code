package com.supermarket.finance.purchase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_purchase_order_line")
public class PurchaseOrderLine {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long poId;
    private String productCode;
    private String productName;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal taxRate;
    private Long expenseAccountId;
    private LocalDateTime expectedArrivalDate;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








