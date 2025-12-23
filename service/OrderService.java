package com.supermarket.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermarket.finance.entity.OrderEntity;

// 继承IService，获得通用CRUD服务
public interface OrderService extends IService<OrderEntity> {
}