package com.supermarket.finance.gl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("gl_txn")
public class GlTxn {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bookId;
    private String sourceType;
    private Long sourceId;
    private LocalDateTime txnDate;
    private String description;
    private String status;
    private LocalDateTime postedAt;
    private Long postedBy;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








