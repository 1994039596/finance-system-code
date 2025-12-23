package com.supermarket.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// 对应MySQL表：t_order（需先创建该表）
@Data
@TableName("t_order")
public class OrderEntity {
    @TableId(type = IdType.AUTO)  // 主键自增
    private Long id;              // 订单ID
    private String orderNo;       // 订单编号
    private Long supplierId;      // 供应商ID
    private BigDecimal totalAmount; // 订单总金额
    private LocalDateTime createTime; // 创建时间
}