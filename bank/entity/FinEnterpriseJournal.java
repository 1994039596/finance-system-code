package com.supermarket.finance.bank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_enterprise_journal")
public class FinEnterpriseJournal {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String accountNo;
    private String voucherNo;
    private LocalDateTime transactionDate;
    private String summary;
    private String type; // DEBIT/CREDIT
    private BigDecimal amount;
    private Integer posted;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








