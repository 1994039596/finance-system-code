package com.supermarket.finance.purchase.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.purchase.dto.PurchaseOrderCreateRequest;
import com.supermarket.finance.purchase.dto.PurchaseOrderResponse;
import com.supermarket.finance.purchase.service.PurchaseOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase/orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('purchase:order:create') or hasRole('ADMIN')")
    public Result<PurchaseOrderResponse> create(@RequestBody PurchaseOrderCreateRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth == null ? "" : String.valueOf(auth.getPrincipal());
        return Result.success(purchaseOrderService.create(req, username));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('purchase:order:view') or hasRole('ADMIN')")
    public Result<PurchaseOrderResponse> get(@PathVariable Long id) {
        return Result.success(purchaseOrderService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('purchase:order:list') or hasRole('ADMIN')")
    public Result<List<PurchaseOrderResponse>> list() {
        return Result.success(purchaseOrderService.list());
    }
}








