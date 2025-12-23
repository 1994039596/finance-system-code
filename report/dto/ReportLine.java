package com.supermarket.finance.report.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReportLine {
    private String accountCode;
    private String accountName;
    private String accountType; // ASSET/LIABILITY/EQUITY/REVENUE/COST/EXPENSE
    private BigDecimal amount;  // 按 normal_side 方向计算的“余额/发生额”（正数为正常方向）
}





