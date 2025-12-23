package com.supermarket.finance.customer.service;

import com.supermarket.finance.customer.dto.CustomerCreateRequest;
import com.supermarket.finance.customer.entity.FinCustomer;

import java.util.List;

public interface CustomerService {
    FinCustomer create(CustomerCreateRequest req);
    FinCustomer get(Long id);
    List<FinCustomer> list();
}








