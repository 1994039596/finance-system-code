package com.supermarket.finance.hr.dto;

import lombok.Data;

@Data
public class SalaryPayRequest {
    private Long employeeId;
    private String period; // YYYYMM
    private String bankAccount; // 可选（默认 TEST-ACCOUNT）
}


