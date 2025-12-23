package com.supermarket.finance.bank.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.bank.dto.ReconciliationAutoMatchResponse;
import com.supermarket.finance.bank.entity.FinBankStatement;
import com.supermarket.finance.bank.entity.FinEnterpriseJournal;
import com.supermarket.finance.bank.entity.FinReconciliation;
import com.supermarket.finance.bank.entity.FinReconciliationItem;
import com.supermarket.finance.bank.mapper.FinBankStatementMapper;
import com.supermarket.finance.bank.mapper.FinEnterpriseJournalMapper;
import com.supermarket.finance.bank.mapper.FinReconciliationItemMapper;
import com.supermarket.finance.bank.mapper.FinReconciliationMapper;
import com.supermarket.finance.bank.service.ReconciliationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReconciliationServiceImpl implements ReconciliationService {

    private final FinBankStatementMapper statementMapper;
    private final FinEnterpriseJournalMapper journalMapper;
    private final FinReconciliationMapper reconciliationMapper;
    private final FinReconciliationItemMapper itemMapper;

    public ReconciliationServiceImpl(FinBankStatementMapper statementMapper,
                                     FinEnterpriseJournalMapper journalMapper,
                                     FinReconciliationMapper reconciliationMapper,
                                     FinReconciliationItemMapper itemMapper) {
        this.statementMapper = statementMapper;
        this.journalMapper = journalMapper;
        this.reconciliationMapper = reconciliationMapper;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional
    public ReconciliationAutoMatchResponse autoMatch(String period, String accountNo) {
        if (period == null || period.isBlank()) throw new IllegalArgumentException("period 不能为空");
        if (accountNo == null || accountNo.isBlank()) throw new IllegalArgumentException("accountNo 不能为空");

        List<FinBankStatement> statements = statementMapper.selectList(Wrappers.<FinBankStatement>lambdaQuery()
                .eq(FinBankStatement::getPeriod, period)
                .eq(FinBankStatement::getAccountNo, accountNo)
                .eq(FinBankStatement::getIsDeleted, 0));

        List<FinEnterpriseJournal> journals = journalMapper.selectList(Wrappers.<FinEnterpriseJournal>lambdaQuery()
                .eq(FinEnterpriseJournal::getAccountNo, accountNo)
                .eq(FinEnterpriseJournal::getIsDeleted, 0));

        // 为了实现“流水号+金额”优先匹配：voucherNo == bankFlowNo
        Map<String, List<FinEnterpriseJournal>> byVoucher = new HashMap<>();
        for (FinEnterpriseJournal j : journals) {
            if (j.getVoucherNo() == null) continue;
            byVoucher.computeIfAbsent(j.getVoucherNo(), k -> new ArrayList<>()).add(j);
        }

        FinReconciliation rec = new FinReconciliation();
        rec.setPeriod(period);
        rec.setAccountNo(accountNo);
        rec.setStatus("DRAFT");
        rec.setCreatedAt(LocalDateTime.now());
        rec.setUpdatedAt(LocalDateTime.now());
        rec.setIsDeleted(0);
        reconciliationMapper.insert(rec);

        int matched = 0;
        int unmatched = 0;

        for (FinBankStatement s : statements) {
            BigDecimal amount = amountOf(s);
            String type = typeOf(s);
            List<FinEnterpriseJournal> candidates = byVoucher.getOrDefault(s.getBankFlowNo(), List.of());
            FinEnterpriseJournal hit = null;
            for (FinEnterpriseJournal j : candidates) {
                if (j.getIsDeleted() != null && j.getIsDeleted() == 1) continue;
                if (j.getAmount() == null) continue;
                if (!Objects.equals(normType(j.getType()), type)) continue;
                if (j.getAmount().compareTo(amount) != 0) continue;
                hit = j;
                break;
            }

            if (hit != null) {
                matched++;
                FinReconciliationItem item = new FinReconciliationItem();
                item.setReconciliationId(rec.getId());
                item.setBankStatementId(s.getId());
                item.setEnterpriseJournalId(hit.getId());
                item.setMatchType("AUTO");
                item.setMatchBasis("flow_no+amount");
                item.setConfirmed(1);
                item.setCreatedAt(LocalDateTime.now());
                item.setUpdatedAt(LocalDateTime.now());
                item.setIsDeleted(0);
                itemMapper.insert(item);
            } else {
                unmatched++;
            }
        }

        BigDecimal rate = statements.isEmpty()
                ? BigDecimal.ZERO
                : new BigDecimal(matched).divide(new BigDecimal(statements.size()), 6, java.math.RoundingMode.HALF_UP);

        rec.setMatchedCount(matched);
        rec.setUnmatchedCount(unmatched);
        rec.setMatchRate(rate);
        rec.setUpdatedAt(LocalDateTime.now());
        reconciliationMapper.updateById(rec);

        ReconciliationAutoMatchResponse resp = new ReconciliationAutoMatchResponse();
        resp.setReconciliationId(rec.getId());
        resp.setPeriod(period);
        resp.setAccountNo(accountNo);
        resp.setMatchedCount(matched);
        resp.setUnmatchedCount(unmatched);
        resp.setMatchRate(rate);
        return resp;
    }

    private static BigDecimal amountOf(FinBankStatement s) {
        BigDecimal d = s.getDebit() == null ? BigDecimal.ZERO : s.getDebit();
        BigDecimal c = s.getCredit() == null ? BigDecimal.ZERO : s.getCredit();
        return d.compareTo(BigDecimal.ZERO) > 0 ? d : c;
    }

    private static String typeOf(FinBankStatement s) {
        BigDecimal d = s.getDebit() == null ? BigDecimal.ZERO : s.getDebit();
        return d.compareTo(BigDecimal.ZERO) > 0 ? "DEBIT" : "CREDIT";
    }

    private static String normType(String type) {
        return type == null ? "" : type.trim().toUpperCase();
    }
}








