package com.supermarket.finance.gl.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GlTxnResponse {
    private Long id;
    private Long bookId;
    private LocalDateTime txnDate;
    private String description;
    private String status;
    private LocalDateTime postedAt;
    private List<Split> splits;

    @Data
    public static class Split {
        private Long id;
        private Long accountId;
        private String summary;
        private String side;
        private BigDecimal amount;
        private BigDecimal quantity;
        private BigDecimal price;
    }
}








