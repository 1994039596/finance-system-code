package com.supermarket.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.finance.entity.OrderEntity;
import com.supermarket.finance.mapper.OrderMapper;
import com.supermarket.finance.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {
}