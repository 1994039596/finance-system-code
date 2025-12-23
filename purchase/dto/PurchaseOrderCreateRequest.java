package com.supermarket.finance.purchase.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderCreateRequest {
    private Long supplierId;
    private Long bookId;
    private String period;
    private String project;
    private Long customerId;
    private String description;
    private LocalDateTime orderDate;

    private List<Line> lines;

    @Data
    public static class Line {
        private String productCode;
        private String productName;
        private BigDecimal quantity;
        private BigDecimal price;
        private BigDecimal taxRate;
        private Long expenseAccountId;
        private LocalDateTime expectedArrivalDate;
    }
}








