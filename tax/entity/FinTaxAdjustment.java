package com.supermarket.finance.tax.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_tax_adjustment")
public class FinTaxAdjustment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String scope;
    private BigDecimal oldRate;
    private BigDecimal newRate;
    private LocalDateTime effectiveDate;
    private String reason;
    private String status; // SUBMITTED/APPROVED/REJECTED/EFFECTIVE

    private Long applicantId;
    private Long auditorId;
    private LocalDateTime auditedAt;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








