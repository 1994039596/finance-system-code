package com.supermarket.finance.gl.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.gl.dto.GlTxnCreateRequest;
import com.supermarket.finance.gl.dto.GlTxnResponse;
import com.supermarket.finance.gl.service.GlTxnService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gl/txns")
public class GlTxnController {

    private final GlTxnService txnService;

    public GlTxnController(GlTxnService txnService) {
        this.txnService = txnService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('gl:txn:create') or hasRole('ADMIN')")
    public Result<GlTxnResponse> create(@RequestBody GlTxnCreateRequest req) {
        return Result.success(txnService.create(req));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('gl:txn:view') or hasRole('ADMIN')")
    public Result<GlTxnResponse> get(@PathVariable Long id) {
        return Result.success(txnService.get(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('gl:txn:list') or hasRole('ADMIN')")
    public Result<List<GlTxnResponse>> list(@RequestParam Long bookId) {
        return Result.success(txnService.list(bookId));
    }

    @PostMapping("/{id}/post")
    @PreAuthorize("hasAuthority('gl:txn:post') or hasRole('ADMIN')")
    public Result<GlTxnResponse> post(@PathVariable Long id) {
        return Result.success(txnService.post(id));
    }
}








