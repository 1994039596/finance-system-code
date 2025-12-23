package com.supermarket.finance.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.supermarket.finance.report.dto.ReportLine;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("fin_balance_sheet")
public class FinBalanceSheet {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long reportId;
    private String period;
    private BigDecimal assets;
    private BigDecimal liabilities;
    private BigDecimal equity;

    // ===== 展示用扩展字段（不落库）=====
    @TableField(exist = false)
    private List<ReportLine> assetLines;

    @TableField(exist = false)
    private List<ReportLine> liabilityLines;

    @TableField(exist = false)
    private List<ReportLine> equityLines;

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





