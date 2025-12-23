package com.supermarket.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermarket.finance.entity.SupplierEntity;
import com.supermarket.finance.mapper.SupplierMapper;
import com.supermarket.finance.service.SupplierService;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, SupplierEntity> implements SupplierService {
}