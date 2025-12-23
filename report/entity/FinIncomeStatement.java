package com.supermarket.finance.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.supermarket.finance.report.dto.ReportLine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("fin_income_statement")
public class FinIncomeStatement {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long reportId;
    private String period;
    private BigDecimal revenue;
    private BigDecimal expense;
    private BigDecimal netProfit;

    // ===== 展示用扩展字段（不落库）=====
    @TableField(exist = false)
    private List<ReportLine> revenueLines;

    @TableField(exist = false)
    private List<ReportLine> costLines;

    @TableField(exist = false)
    private List<ReportLine> expenseLines;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}





