package com.supermarket.finance.bank.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EnterpriseJournalCreateRequest {
    private String accountNo;
    private String voucherNo; // 可用银行流水号填充用于匹配
    private LocalDateTime transactionDate;
    private String summary;
    private String type; // DEBIT/CREDIT
    private BigDecimal amount;
}








