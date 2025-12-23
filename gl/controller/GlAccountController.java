package com.supermarket.finance.gl.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.common.Result;
import com.supermarket.finance.gl.entity.GlAccount;
import com.supermarket.finance.gl.mapper.GlAccountMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gl/accounts")
public class GlAccountController {

    private final GlAccountMapper accountMapper;

    public GlAccountController(GlAccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('gl:account:list') or hasRole('ADMIN')")
    public Result<List<GlAccount>> list(@RequestParam Long bookId) {
        return Result.success(accountMapper.selectList(Wrappers.<GlAccount>lambdaQuery()
                .eq(GlAccount::getBookId, bookId)
                .eq(GlAccount::getIsDeleted, 0)
                .orderByAsc(GlAccount::getCode)));
    }
}








