package com.supermarket.finance.tax.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.tax.dto.TaxAdjustmentCreateRequest;
import com.supermarket.finance.tax.entity.FinTaxAdjustment;
import com.supermarket.finance.tax.service.TaxAdjustmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tax/adjustments")
public class TaxAdjustmentController {

    private final TaxAdjustmentService service;

    public TaxAdjustmentController(TaxAdjustmentService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('tax:adjust:create') or hasRole('ADMIN')")
    public Result<FinTaxAdjustment> create(@RequestBody TaxAdjustmentCreateRequest req) {
        return Result.success(service.create(req));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('tax:adjust:list') or hasRole('ADMIN')")
    public Result<List<FinTaxAdjustment>> list() {
        return Result.success(service.list());
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('tax:adjust:approve') or hasRole('ADMIN')")
    public Result<FinTaxAdjustment> approve(@PathVariable Long id) {
        return Result.success(service.approve(id));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('tax:adjust:approve') or hasRole('ADMIN')")
    public Result<FinTaxAdjustment> reject(@PathVariable Long id) {
        return Result.success(service.reject(id));
    }

    @PostMapping("/{id}/apply")
    @PreAuthorize("hasAuthority('tax:adjust:apply') or hasRole('ADMIN')")
    public Result<FinTaxAdjustment> apply(@PathVariable Long id) {
        return Result.success(service.apply(id));
    }
}








