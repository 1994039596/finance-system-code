package com.supermarket.finance.customer.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.customer.dto.CustomerCreateRequest;
import com.supermarket.finance.customer.entity.FinCustomer;
import com.supermarket.finance.customer.service.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('customer:create') or hasRole('ADMIN')")
    public Result<FinCustomer> create(@RequestBody CustomerCreateRequest req) {
        return Result.success(customerService.create(req));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('customer:list') or hasRole('ADMIN')")
    public Result<List<FinCustomer>> list() {
        return Result.success(customerService.list());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('customer:view') or hasRole('ADMIN')")
    public Result<FinCustomer> get(@PathVariable Long id) {
        return Result.success(customerService.get(id));
    }
}








