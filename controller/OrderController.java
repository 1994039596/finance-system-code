package com.supermarket.finance.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.entity.OrderEntity;
import com.supermarket.finance.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping({"/order", "/api/order"})
public class OrderController {
    @Resource
    private OrderService orderService;

    // 新增订单
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('order:add') or hasRole('ADMIN')")
    public Result<Boolean> addOrder(@RequestBody OrderEntity order) {
        return Result.success(orderService.save(order));
    }

    // 查询所有订单
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('order:list') or hasRole('ADMIN')")
    public Result<List<OrderEntity>> listOrder() {
        return Result.success(orderService.list());
    }
}