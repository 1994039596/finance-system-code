package com.supermarket.finance.gl.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GlTxnCreateRequest {
    private Long bookId;
    private String sourceType;
    private Long sourceId;
    private LocalDateTime txnDate;
    private String description;
    private List<Split> splits;

    @Data
    public static class Split {
        private Long accountId;
        private String summary;
        private String side; // DR/CR
        private BigDecimal quantity;
        private BigDecimal price;
        private BigDecimal amount;
    }
}








