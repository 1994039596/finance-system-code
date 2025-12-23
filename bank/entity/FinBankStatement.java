package com.supermarket.finance.bank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_bank_statement")
public class FinBankStatement {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String accountNo;
    private String period;
    private LocalDateTime transactionDate;
    private String bankFlowNo;
    private String summary;
    private BigDecimal debit;
    private BigDecimal credit;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








