package com.supermarket.finance.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_cash_flow_statement")
public class FinCashFlowStatement {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long reportId;
    private String period;
    private BigDecimal operatingFlow;
    private BigDecimal investingFlow;
    private BigDecimal financingFlow;

    // ===== 展示用扩展字段（不落库）=====
    @TableField(exist = false)
    private BigDecimal cashAndBankBegin;

    @TableField(exist = false)
    private BigDecimal cashAndBankEnd;

    @TableField(exist = false)
    private BigDecimal cashAndBankChange;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}





