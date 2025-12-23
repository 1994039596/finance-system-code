package com.supermarket.finance.bank.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BankStatementCreateRequest {
    private String accountNo;
    private String period;
    private LocalDateTime transactionDate;
    private String bankFlowNo;
    private String summary;
    private BigDecimal debit;
    private BigDecimal credit;
}








