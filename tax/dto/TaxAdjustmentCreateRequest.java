package com.supermarket.finance.tax.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TaxAdjustmentCreateRequest {
    private String scope;
    private BigDecimal oldRate;
    private BigDecimal newRate;
    private LocalDateTime effectiveDate;
    private String reason;
}








