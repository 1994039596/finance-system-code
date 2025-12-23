package com.supermarket.finance.bank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReconciliationAutoMatchResponse {
    private Long reconciliationId;
    private String period;
    private String accountNo;
    private BigDecimal matchRate;
    private Integer matchedCount;
    private Integer unmatchedCount;
}








