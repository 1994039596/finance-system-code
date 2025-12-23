package com.supermarket.finance.purchase.service;

import com.supermarket.finance.purchase.dto.PurchaseOrderCreateRequest;
import com.supermarket.finance.purchase.dto.PurchaseOrderResponse;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderResponse create(PurchaseOrderCreateRequest req, String operatorUsername);
    PurchaseOrderResponse getById(Long id);
    List<PurchaseOrderResponse> list();
}








