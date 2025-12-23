package com.supermarket.finance.gl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("gl_split")
public class GlSplit {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long txnId;
    private Long accountId;
    private String summary;
    private String side; // DR/CR
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








