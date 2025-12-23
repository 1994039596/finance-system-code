package com.supermarket.finance.purchase.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderResponse {
    private Long id;
    private String orderNo;
    private Long supplierId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private String status;
    private String description;
    private List<Line> lines;

    @Data
    public static class Line {
        private Long id;
        private String productCode;
        private String productName;
        private BigDecimal quantity;
        private BigDecimal price;
        private BigDecimal amount;
        private BigDecimal taxRate;
        private Long expenseAccountId;
        private LocalDateTime expectedArrivalDate;
    }
}








