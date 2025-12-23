package com.supermarket.finance.hr.service;

import com.supermarket.finance.hr.dto.EmployeeCreateRequest;
import com.supermarket.finance.hr.dto.SalaryInfoUpsertRequest;
import com.supermarket.finance.hr.dto.SalaryPayRequest;
import com.supermarket.finance.hr.entity.HrEmployee;
import com.supermarket.finance.hr.entity.HrSalaryInfo;
import com.supermarket.finance.hr.entity.HrSalaryPayment;

import java.util.List;

public interface HrService {
    HrEmployee createEmployee(EmployeeCreateRequest req);
    List<HrEmployee> listEmployees();

    HrSalaryInfo upsertSalaryInfo(SalaryInfoUpsertRequest req);
    HrSalaryInfo getSalaryInfo(Long employeeId);

    HrSalaryPayment paySalary(SalaryPayRequest req);
    List<HrSalaryPayment> listPayments(Long employeeId);
}








