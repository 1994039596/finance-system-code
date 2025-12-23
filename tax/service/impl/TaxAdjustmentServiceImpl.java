package com.supermarket.finance.tax.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.tax.dto.TaxAdjustmentCreateRequest;
import com.supermarket.finance.tax.entity.FinTaxAdjustment;
import com.supermarket.finance.tax.mapper.FinTaxAdjustmentMapper;
import com.supermarket.finance.tax.service.TaxAdjustmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaxAdjustmentServiceImpl implements TaxAdjustmentService {

    private final FinTaxAdjustmentMapper mapper;

    public TaxAdjustmentServiceImpl(FinTaxAdjustmentMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public FinTaxAdjustment create(TaxAdjustmentCreateRequest req) {
        if (req.getScope() == null || req.getScope().isBlank()) throw new IllegalArgumentException("scope 不能为空");
        if (req.getOldRate() == null) throw new IllegalArgumentException("oldRate 不能为空");
        if (req.getNewRate() == null) throw new IllegalArgumentException("newRate 不能为空");
        if (req.getEffectiveDate() == null) throw new IllegalArgumentException("effectiveDate 不能为空");
        if (req.getReason() == null || req.getReason().isBlank()) throw new IllegalArgumentException("reason 不能为空");
        if (req.getOldRate().compareTo(BigDecimal.ZERO) < 0 || req.getNewRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("税率必须 >= 0");
        }

        FinTaxAdjustment a = new FinTaxAdjustment();
        a.setScope(req.getScope());
        a.setOldRate(req.getOldRate());
        a.setNewRate(req.getNewRate());
        a.setEffectiveDate(req.getEffectiveDate());
        a.setReason(req.getReason());
        a.setStatus("SUBMITTED");
        a.setCreatedAt(LocalDateTime.now());
        a.setUpdatedAt(LocalDateTime.now());
        a.setIsDeleted(0);
        mapper.insert(a);
        return a;
    }

    @Override
    public List<FinTaxAdjustment> list() {
        return mapper.selectList(Wrappers.<FinTaxAdjustment>lambdaQuery()
                .eq(FinTaxAdjustment::getIsDeleted, 0)
                .orderByDesc(FinTaxAdjustment::getId));
    }

    @Override
    @Transactional
    public FinTaxAdjustment approve(Long id) {
        FinTaxAdjustment a = require(id);
        if (!"SUBMITTED".equals(a.getStatus())) throw new IllegalArgumentException("只有 SUBMITTED 才能审核");
        a.setStatus("APPROVED");
        a.setAuditedAt(LocalDateTime.now());
        a.setUpdatedAt(LocalDateTime.now());
        mapper.updateById(a);
        return a;
    }

    @Override
    @Transactional
    public FinTaxAdjustment reject(Long id) {
        FinTaxAdjustment a = require(id);
        if (!"SUBMITTED".equals(a.getStatus())) throw new IllegalArgumentException("只有 SUBMITTED 才能审核");
        a.setStatus("REJECTED");
        a.setAuditedAt(LocalDateTime.now());
        a.setUpdatedAt(LocalDateTime.now());
        mapper.updateById(a);
        return a;
    }

    @Override
    @Transactional
    public FinTaxAdjustment apply(Long id) {
        FinTaxAdjustment a = require(id);
        if (!"APPROVED".equals(a.getStatus())) throw new IllegalArgumentException("只有 APPROVED 才能生效");
        if (a.getEffectiveDate() != null && a.getEffectiveDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("未到生效时间");
        }
        a.setStatus("EFFECTIVE");
        a.setUpdatedAt(LocalDateTime.now());
        mapper.updateById(a);
        return a;
    }

    private FinTaxAdjustment require(Long id) {
        if (id == null) throw new IllegalArgumentException("id 不能为空");
        FinTaxAdjustment a = mapper.selectById(id);
        if (a == null || (a.getIsDeleted() != null && a.getIsDeleted() == 1)) throw new IllegalArgumentException("记录不存在");
        return a;
    }
}








