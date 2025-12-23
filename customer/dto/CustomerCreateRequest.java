package com.supermarket.finance.customer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerCreateRequest {
    private String type;     // PERSONAL/BUSINESS
    private String name;
    private String idNumber;

    // 初始信用额度（可选）
    private BigDecimal creditLimit;

    // 初始收款账户（可选）
    private String bank;
    private String accountNo;
}








