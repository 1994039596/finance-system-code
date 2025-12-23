package com.supermarket.finance.gl.service;

import com.supermarket.finance.gl.dto.GlTxnCreateRequest;
import com.supermarket.finance.gl.dto.GlTxnResponse;

import java.util.List;

public interface GlTxnService {
    GlTxnResponse create(GlTxnCreateRequest req);
    GlTxnResponse get(Long id);
    List<GlTxnResponse> list(Long bookId);
    GlTxnResponse post(Long id);
}








