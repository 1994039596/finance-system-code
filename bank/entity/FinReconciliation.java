package com.supermarket.finance.bank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_reconciliation")
public class FinReconciliation {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String period;
    private String accountNo;
    private BigDecimal matchRate;
    private Integer matchedCount;
    private Integer unmatchedCount;
    private String auditor;
    private LocalDateTime auditedAt;
    private String status; // DRAFT/SUBMITTED/APPROVED

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








