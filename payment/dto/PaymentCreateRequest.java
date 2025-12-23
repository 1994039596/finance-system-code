package com.supermarket.finance.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateRequest {
    private Long poId;
    private BigDecimal amount;
    private String method;      // BANK/ALIPAY/WX/CASH
    private String bankAccount; // 可选
    private String remark;      // 可选
}








