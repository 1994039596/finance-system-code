package com.supermarket.finance.bank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_uncleared_item")
public class FinUnclearedItem {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long reconciliationId;
    private BigDecimal amount;
    private String reason;
    private Integer days;
    private String status; // OPEN/CLOSED

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








