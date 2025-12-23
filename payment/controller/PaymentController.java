package com.supermarket.finance.payment.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.payment.dto.PaymentCreateRequest;
import com.supermarket.finance.payment.entity.FinPayment;
import com.supermarket.finance.payment.service.PaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('payment:create') or hasRole('ADMIN')")
    public Result<FinPayment> create(@RequestBody PaymentCreateRequest req) {
        return Result.success(paymentService.create(req));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('payment:list') or hasRole('ADMIN')")
    public Result<List<FinPayment>> list(@RequestParam Long poId) {
        return Result.success(paymentService.listByPoId(poId));
    }
}








