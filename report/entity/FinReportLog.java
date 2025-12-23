package com.supermarket.finance.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("fin_report_log")
public class FinReportLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long reportId;
    private String operatorName;
    private LocalDateTime operateTime;
    private String operation; // GENERATE/EXPORT/APPROVE

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








