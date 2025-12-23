package com.supermarket.finance.supplier.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.common.Result;
import com.supermarket.finance.supplier.entity.FinSupplier;
import com.supermarket.finance.supplier.mapper.FinSupplierMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class FinSupplierController {

    private final FinSupplierMapper supplierMapper;

    public FinSupplierController(FinSupplierMapper supplierMapper) {
        this.supplierMapper = supplierMapper;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('supplier:fin:create') or hasRole('ADMIN')")
    public Result<FinSupplier> create(@RequestBody FinSupplier s) {
        s.setId(null);
        if (s.getStatus() == null || s.getStatus().isBlank()) s.setStatus("ACTIVE");
        if (s.getCreatedAt() == null) s.setCreatedAt(LocalDateTime.now());
        s.setUpdatedAt(LocalDateTime.now());
        if (s.getIsDeleted() == null) s.setIsDeleted(0);
        supplierMapper.insert(s);
        return Result.success(s);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('supplier:fin:list') or hasRole('ADMIN')")
    public Result<List<FinSupplier>> list() {
        return Result.success(supplierMapper.selectList(Wrappers.<FinSupplier>lambdaQuery()
                .eq(FinSupplier::getIsDeleted, 0)
                .orderByDesc(FinSupplier::getId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('supplier:fin:view') or hasRole('ADMIN')")
    public Result<FinSupplier> get(@PathVariable Long id) {
        FinSupplier s = supplierMapper.selectById(id);
        if (s == null || (s.getIsDeleted() != null && s.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("供应商不存在");
        }
        return Result.success(s);
    }
}








