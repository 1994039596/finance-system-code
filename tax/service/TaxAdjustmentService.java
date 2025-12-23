package com.supermarket.finance.tax.service;

import com.supermarket.finance.tax.dto.TaxAdjustmentCreateRequest;
import com.supermarket.finance.tax.entity.FinTaxAdjustment;

import java.util.List;

public interface TaxAdjustmentService {
    FinTaxAdjustment create(TaxAdjustmentCreateRequest req);
    List<FinTaxAdjustment> list();
    FinTaxAdjustment approve(Long id);
    FinTaxAdjustment reject(Long id);
    FinTaxAdjustment apply(Long id);
}








