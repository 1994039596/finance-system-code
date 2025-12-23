package com.supermarket.finance.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.entity.SupplierEntity;
import com.supermarket.finance.service.SupplierService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping({"/supplier", "/api/supplier"})
public class SupplierController {
    @Resource
    private SupplierService supplierService;

    // 新增供应商
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('supplier:add') or hasRole('ADMIN')")
    public Result<Boolean> addSupplier(@RequestBody SupplierEntity supplier) {
        return Result.success(supplierService.save(supplier));
    }

    // 查询所有供应商
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('supplier:list') or hasRole('ADMIN')")
    public Result<List<SupplierEntity>> listSupplier() {
        return Result.success(supplierService.list());
    }
}