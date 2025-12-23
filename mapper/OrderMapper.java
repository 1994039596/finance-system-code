package com.supermarket.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.finance.entity.OrderEntity;

// 继承BaseMapper，自动获得CRUD方法
public interface OrderMapper extends BaseMapper<OrderEntity> {
}