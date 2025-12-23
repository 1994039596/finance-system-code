package com.supermarket.finance.gl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("gl_account")
public class GlAccount {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bookId;
    private String code;
    private String name;
    private String type;
    private Long parentId;
    private String normalSide;
    private Integer status;

    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Integer isDeleted;
}








