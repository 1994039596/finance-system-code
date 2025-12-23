package com.supermarket.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

// 对应MySQL表：t_supplier（需先创建该表）
@Data
@TableName("t_supplier")
public class SupplierEntity {
    @TableId(type = IdType.AUTO)
    private Long id;              // 供应商ID
    private String supplierName;  // 供应商名称
    private String contactPhone;  // 联系电话
    private String address;       // 地址
}