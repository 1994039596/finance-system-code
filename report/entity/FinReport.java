package com.supermarket.finance.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("fin_report")
public class FinReport {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String type;   // BS/IS/CF
    private String period; // YYYYMM
    private String maker;
    private LocalDateTime generateTime;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








