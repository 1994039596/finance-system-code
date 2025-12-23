package com.supermarket.finance.hr.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.hr.dto.EmployeeCreateRequest;
import com.supermarket.finance.hr.dto.SalaryInfoUpsertRequest;
import com.supermarket.finance.hr.dto.SalaryPayRequest;
import com.supermarket.finance.hr.entity.HrEmployee;
import com.supermarket.finance.hr.entity.HrSalaryInfo;
import com.supermarket.finance.hr.entity.HrSalaryPayment;
import com.supermarket.finance.hr.service.HrService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr")
public class HrController {

    private final HrService hrService;

    public HrController(HrService hrService) {
        this.hrService = hrService;
    }

    @PostMapping("/employees")
    @PreAuthorize("hasAuthority('hr:employee:create') or hasRole('ADMIN')")
    public Result<HrEmployee> createEmployee(@RequestBody EmployeeCreateRequest req) {
        return Result.success(hrService.createEmployee(req));
    }

    @GetMapping("/employees")
    @PreAuthorize("hasAuthority('hr:employee:list') or hasRole('ADMIN')")
    public Result<List<HrEmployee>> listEmployees() {
        return Result.success(hrService.listEmployees());
    }

    @PostMapping("/salary-info")
    @PreAuthorize("hasAuthority('hr:salary:config') or hasRole('ADMIN')")
    public Result<HrSalaryInfo> upsertSalary(@RequestBody SalaryInfoUpsertRequest req) {
        return Result.success(hrService.upsertSalaryInfo(req));
    }

    @GetMapping("/salary-info")
    @PreAuthorize("hasAuthority('hr:salary:view') or hasRole('ADMIN')")
    public Result<HrSalaryInfo> getSalary(@RequestParam Long employeeId) {
        return Result.success(hrService.getSalaryInfo(employeeId));
    }

    @PostMapping("/salary-payments")
    @PreAuthorize("hasAuthority('hr:salary:pay') or hasRole('ADMIN')")
    public Result<HrSalaryPayment> pay(@RequestBody SalaryPayRequest req) {
        return Result.success(hrService.paySalary(req));
    }

    @GetMapping("/salary-payments")
    @PreAuthorize("hasAuthority('hr:salary:list') or hasRole('ADMIN')")
    public Result<List<HrSalaryPayment>> listPayments(@RequestParam Long employeeId) {
        return Result.success(hrService.listPayments(employeeId));
    }
}








