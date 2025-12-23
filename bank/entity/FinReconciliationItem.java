package com.supermarket.finance.bank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("fin_reconciliation_item")
public class FinReconciliationItem {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long reconciliationId;
    private Long bankStatementId;
    private Long enterpriseJournalId;
    private String matchType;  // AUTO/MANUAL
    private String matchBasis; // flow_no+amount
    private Integer confirmed; // 0/1

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








