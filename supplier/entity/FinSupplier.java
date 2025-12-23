package com.supermarket.finance.supplier.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_supplier")
public class FinSupplier {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String type;
    private String creditCode;
    private String region;
    private String status;
    private BigDecimal creditLimit;
    private Integer payTermDays;
    private String bankName;
    private String bankAccount;
    private String bankHolder;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








