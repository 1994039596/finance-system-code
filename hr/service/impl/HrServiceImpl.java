package com.supermarket.finance.hr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.hr.dto.EmployeeCreateRequest;
import com.supermarket.finance.hr.dto.SalaryInfoUpsertRequest;
import com.supermarket.finance.hr.dto.SalaryPayRequest;
import com.supermarket.finance.hr.entity.HrEmployee;
import com.supermarket.finance.hr.entity.HrSalaryInfo;
import com.supermarket.finance.hr.entity.HrSalaryPayment;
import com.supermarket.finance.hr.mapper.HrEmployeeMapper;
import com.supermarket.finance.hr.mapper.HrSalaryInfoMapper;
import com.supermarket.finance.hr.mapper.HrSalaryPaymentMapper;
import com.supermarket.finance.hr.service.HrService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HrServiceImpl implements HrService {

    private final HrEmployeeMapper employeeMapper;
    private final HrSalaryInfoMapper salaryInfoMapper;
    private final HrSalaryPaymentMapper salaryPaymentMapper;

    public HrServiceImpl(HrEmployeeMapper employeeMapper,
                         HrSalaryInfoMapper salaryInfoMapper,
                         HrSalaryPaymentMapper salaryPaymentMapper) {
        this.employeeMapper = employeeMapper;
        this.salaryInfoMapper = salaryInfoMapper;
        this.salaryPaymentMapper = salaryPaymentMapper;
    }

    @Override
    @Transactional
    public HrEmployee createEmployee(EmployeeCreateRequest req) {
        if (req.getName() == null || req.getName().isBlank()) throw new IllegalArgumentException("name 不能为空");
        if (req.getDepartment() == null || req.getDepartment().isBlank()) throw new IllegalArgumentException("department 不能为空");
        if (req.getPosition() == null || req.getPosition().isBlank()) throw new IllegalArgumentException("position 不能为空");

        HrEmployee e = new HrEmployee();
        e.setName(req.getName());
        e.setDepartment(req.getDepartment());
        e.setPosition(req.getPosition());
        e.setHireDate(req.getHireDate() == null ? LocalDateTime.now() : req.getHireDate());
        e.setStatus("ACTIVE");
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        e.setIsDeleted(0);
        employeeMapper.insert(e);
        return e;
    }

    @Override
    public List<HrEmployee> listEmployees() {
        return employeeMapper.selectList(Wrappers.<HrEmployee>lambdaQuery()
                .eq(HrEmployee::getIsDeleted, 0)
                .orderByDesc(HrEmployee::getId));
    }

    @Override
    @Transactional
    public HrSalaryInfo upsertSalaryInfo(SalaryInfoUpsertRequest req) {
        if (req.getEmployeeId() == null) throw new IllegalArgumentException("employeeId 不能为空");
        if (req.getPeriod() == null || req.getPeriod().isBlank()) throw new IllegalArgumentException("period 不能为空");
        if (req.getBasicSalary() == null) throw new IllegalArgumentException("basicSalary 不能为空");
        if (req.getBasicSalary().compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("basicSalary 必须 >= 0");
        BigDecimal bonus = req.getBonus() == null ? BigDecimal.ZERO : req.getBonus();
        if (bonus.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("bonus 必须 >= 0");
        BigDecimal deduction = req.getDeduction() == null ? BigDecimal.ZERO : req.getDeduction();
        if (deduction.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("deduction 必须 >= 0");

        HrEmployee e = employeeMapper.selectById(req.getEmployeeId());
        if (e == null || (e.getIsDeleted() != null && e.getIsDeleted() == 1)) throw new IllegalArgumentException("员工不存在");

        HrSalaryInfo old = salaryInfoMapper.selectOne(Wrappers.<HrSalaryInfo>lambdaQuery()
                .eq(HrSalaryInfo::getEmployeeId, req.getEmployeeId())
                .eq(HrSalaryInfo::getPeriod, req.getPeriod())
                .eq(HrSalaryInfo::getIsDeleted, 0)
                .last("limit 1"));

        if (old != null) {
            old.setBasicSalary(req.getBasicSalary());
            old.setBonus(bonus);
            old.setDeduction(deduction);
            old.setPeriod(req.getPeriod());
            old.setUpdatedAt(LocalDateTime.now());
            salaryInfoMapper.updateById(old);
            return old;
        }

        HrSalaryInfo info = new HrSalaryInfo();
        info.setEmployeeId(req.getEmployeeId());
        info.setBasicSalary(req.getBasicSalary());
        info.setBonus(bonus);
        info.setDeduction(deduction);
        info.setPeriod(req.getPeriod());
        info.setCreatedAt(LocalDateTime.now());
        info.setUpdatedAt(LocalDateTime.now());
        info.setIsDeleted(0);
        salaryInfoMapper.insert(info);
        return info;
    }

    @Override
    public HrSalaryInfo getSalaryInfo(Long employeeId) {
        if (employeeId == null) throw new IllegalArgumentException("employeeId 不能为空");
        return salaryInfoMapper.selectOne(Wrappers.<HrSalaryInfo>lambdaQuery()
                .eq(HrSalaryInfo::getEmployeeId, employeeId)
                .eq(HrSalaryInfo::getIsDeleted, 0)
                .last("limit 1"));
    }

    @Override
    @Transactional
    public HrSalaryPayment paySalary(SalaryPayRequest req) {
        if (req.getEmployeeId() == null) throw new IllegalArgumentException("employeeId 不能为空");
        if (req.getPeriod() == null || req.getPeriod().isBlank()) throw new IllegalArgumentException("period 不能为空");

        HrSalaryInfo info = salaryInfoMapper.selectOne(Wrappers.<HrSalaryInfo>lambdaQuery()
                .eq(HrSalaryInfo::getEmployeeId, req.getEmployeeId())
                .eq(HrSalaryInfo::getPeriod, req.getPeriod())
                .eq(HrSalaryInfo::getIsDeleted, 0)
                .last("limit 1"));
        if (info == null) throw new IllegalArgumentException("未配置该期间薪资信息");

        HrSalaryPayment p = new HrSalaryPayment();
        p.setEmployeeId(req.getEmployeeId());
        p.setPaymentTime(LocalDateTime.now());
        p.setSalaryId(info.getId());
        p.setBankAccount((req.getBankAccount() == null || req.getBankAccount().isBlank()) ? "TEST-ACCOUNT" : req.getBankAccount());
        p.setArrivalStatus("ARRIVED");
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        p.setIsDeleted(0);
        salaryPaymentMapper.insert(p);
        return p;
    }

    @Override
    public List<HrSalaryPayment> listPayments(Long employeeId) {
        if (employeeId == null) throw new IllegalArgumentException("employeeId 不能为空");
        return salaryPaymentMapper.selectList(Wrappers.<HrSalaryPayment>lambdaQuery()
                .eq(HrSalaryPayment::getEmployeeId, employeeId)
                .eq(HrSalaryPayment::getIsDeleted, 0)
                .orderByDesc(HrSalaryPayment::getId));
    }

    // no-op
}


