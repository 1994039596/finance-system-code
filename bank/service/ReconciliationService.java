package com.supermarket.finance.bank.service;

import com.supermarket.finance.bank.dto.ReconciliationAutoMatchResponse;

public interface ReconciliationService {
    ReconciliationAutoMatchResponse autoMatch(String period, String accountNo);
}








