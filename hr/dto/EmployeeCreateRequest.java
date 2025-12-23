package com.supermarket.finance.hr.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeCreateRequest {
    private String name;
    private String department;
    private String position;
    private LocalDateTime hireDate;
}








