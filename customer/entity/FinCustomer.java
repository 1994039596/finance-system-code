package com.supermarket.finance.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("fin_customer")
public class FinCustomer {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String type;      // PERSONAL/BUSINESS
    private String name;
    private String idNumber;
    private String status;    // ACTIVE/INACTIVE
    private LocalDateTime createTime;

    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








