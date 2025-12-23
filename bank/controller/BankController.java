package com.supermarket.finance.bank.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.bank.dto.BankStatementCreateRequest;
import com.supermarket.finance.bank.dto.EnterpriseJournalCreateRequest;
import com.supermarket.finance.bank.dto.ReconciliationAutoMatchResponse;
import com.supermarket.finance.bank.entity.FinBankStatement;
import com.supermarket.finance.bank.entity.FinEnterpriseJournal;
import com.supermarket.finance.bank.mapper.FinBankStatementMapper;
import com.supermarket.finance.bank.mapper.FinEnterpriseJournalMapper;
import com.supermarket.finance.bank.service.ReconciliationService;
import com.supermarket.finance.common.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final FinBankStatementMapper statementMapper;
    private final FinEnterpriseJournalMapper journalMapper;
    private final ReconciliationService reconciliationService;

    public BankController(FinBankStatementMapper statementMapper,
                          FinEnterpriseJournalMapper journalMapper,
                          ReconciliationService reconciliationService) {
        this.statementMapper = statementMapper;
        this.journalMapper = journalMapper;
        this.reconciliationService = reconciliationService;
    }

    @PostMapping("/statements")
    @PreAuthorize("hasAuthority('bank:statement:create') or hasRole('ADMIN')")
    public Result<FinBankStatement> createStatement(@RequestBody BankStatementCreateRequest req) {
        FinBankStatement s = new FinBankStatement();
        s.setAccountNo(req.getAccountNo());
        s.setPeriod(req.getPeriod());
        s.setTransactionDate(req.getTransactionDate() == null ? LocalDateTime.now() : req.getTransactionDate());
        s.setBankFlowNo(req.getBankFlowNo());
        s.setSummary(req.getSummary() == null ? "" : req.getSummary());
        s.setDebit(req.getDebit() == null ? BigDecimal.ZERO : req.getDebit());
        s.setCredit(req.getCredit() == null ? BigDecimal.ZERO : req.getCredit());
        s.setCreatedAt(LocalDateTime.now());
        s.setUpdatedAt(LocalDateTime.now());
        s.setIsDeleted(0);
        statementMapper.insert(s);
        return Result.success(s);
    }

    @GetMapping("/statements")
    @PreAuthorize("hasAuthority('bank:statement:list') or hasRole('ADMIN')")
    public Result<List<FinBankStatement>> listStatements(@RequestParam String period, @RequestParam String accountNo) {
        return Result.success(statementMapper.selectList(Wrappers.<FinBankStatement>lambdaQuery()
                .eq(FinBankStatement::getPeriod, period)
                .eq(FinBankStatement::getAccountNo, accountNo)
                .eq(FinBankStatement::getIsDeleted, 0)
                .orderByDesc(FinBankStatement::getId)));
    }

    @PostMapping("/journals")
    @PreAuthorize("hasAuthority('bank:journal:create') or hasRole('ADMIN')")
    public Result<FinEnterpriseJournal> createJournal(@RequestBody EnterpriseJournalCreateRequest req) {
        FinEnterpriseJournal j = new FinEnterpriseJournal();
        j.setAccountNo(req.getAccountNo());
        j.setVoucherNo(req.getVoucherNo());
        j.setTransactionDate(req.getTransactionDate() == null ? LocalDateTime.now() : req.getTransactionDate());
        j.setSummary(req.getSummary() == null ? "" : req.getSummary());
        j.setType(req.getType() == null ? "DEBIT" : req.getType().trim().toUpperCase());
        j.setAmount(req.getAmount() == null ? BigDecimal.ZERO : req.getAmount());
        j.setPosted(1);
        j.setCreatedAt(LocalDateTime.now());
        j.setUpdatedAt(LocalDateTime.now());
        j.setIsDeleted(0);
        journalMapper.insert(j);
        return Result.success(j);
    }

    @PostMapping("/reconciliations/auto-match")
    @PreAuthorize("hasAuthority('bank:recon:auto') or hasRole('ADMIN')")
    public Result<ReconciliationAutoMatchResponse> autoMatch(@RequestParam String period, @RequestParam String accountNo) {
        return Result.success(reconciliationService.autoMatch(period, accountNo));
    }
}








