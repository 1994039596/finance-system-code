package com.supermarket.finance.payment.service;

import com.supermarket.finance.payment.dto.PaymentCreateRequest;
import com.supermarket.finance.payment.entity.FinPayment;

import java.util.List;

public interface PaymentService {
    FinPayment create(PaymentCreateRequest req);
    List<FinPayment> listByPoId(Long poId);
}








