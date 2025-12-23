package com.supermarket.finance.hr.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalaryInfoUpsertRequest {
    private Long employeeId;
    private String period; // YYYYMM
    private BigDecimal basicSalary;
    private BigDecimal bonus;
    private BigDecimal deduction;
}


