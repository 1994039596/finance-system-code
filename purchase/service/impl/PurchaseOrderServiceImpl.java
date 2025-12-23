package com.supermarket.finance.purchase.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.entity.SupplierEntity;
import com.supermarket.finance.mapper.SupplierMapper;
import com.supermarket.finance.purchase.dto.PurchaseOrderCreateRequest;
import com.supermarket.finance.purchase.dto.PurchaseOrderResponse;
import com.supermarket.finance.purchase.entity.PurchaseOrder;
import com.supermarket.finance.purchase.entity.PurchaseOrderLine;
import com.supermarket.finance.purchase.mapper.PurchaseOrderLineMapper;
import com.supermarket.finance.purchase.mapper.PurchaseOrderMapper;
import com.supermarket.finance.purchase.service.PurchaseOrderService;
import com.supermarket.finance.supplier.entity.FinSupplier;
import com.supermarket.finance.supplier.mapper.FinSupplierMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderMapper poMapper;
    private final PurchaseOrderLineMapper lineMapper;
    private final SupplierMapper supplierMapper;
    private final FinSupplierMapper finSupplierMapper;

    public PurchaseOrderServiceImpl(PurchaseOrderMapper poMapper,
                                   PurchaseOrderLineMapper lineMapper,
                                   SupplierMapper supplierMapper,
                                   FinSupplierMapper finSupplierMapper) {
        this.poMapper = poMapper;
        this.lineMapper = lineMapper;
        this.supplierMapper = supplierMapper;
        this.finSupplierMapper = finSupplierMapper;
    }

    @Override
    @Transactional
    public PurchaseOrderResponse create(PurchaseOrderCreateRequest req, String operatorUsername) {
        if (req.getSupplierId() == null) {
            throw new IllegalArgumentException("supplierId 不能为空");
        }
        if (req.getLines() == null || req.getLines().isEmpty()) {
            throw new IllegalArgumentException("订单明细不能为空");
        }

        // 供应商存在性校验：优先使用增强版 fin_supplier；如果没迁移则回退旧表 t_supplier
        FinSupplier finSupplier = finSupplierMapper.selectById(req.getSupplierId());
        if (finSupplier == null || (finSupplier.getIsDeleted() != null && finSupplier.getIsDeleted() == 1)) {
            SupplierEntity supplier = supplierMapper.selectById(req.getSupplierId());
            if (supplier == null) {
                throw new IllegalArgumentException("供应商不存在");
            }
        }

        BigDecimal total = BigDecimal.ZERO;
        List<PurchaseOrderLine> lines = new ArrayList<>();
        for (PurchaseOrderCreateRequest.Line l : req.getLines()) {
            if (l.getProductName() == null || l.getProductName().isBlank()) {
                throw new IllegalArgumentException("productName 不能为空");
            }
            if (l.getQuantity() == null || l.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("quantity 必须 > 0");
            }
            if (l.getPrice() == null || l.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("price 必须 >= 0");
            }
            BigDecimal amount = l.getQuantity().multiply(l.getPrice());
            total = total.add(amount);

            PurchaseOrderLine pol = new PurchaseOrderLine();
            pol.setProductCode(l.getProductCode());
            pol.setProductName(l.getProductName());
            pol.setQuantity(l.getQuantity());
            pol.setPrice(l.getPrice());
            pol.setAmount(amount);
            pol.setTaxRate(l.getTaxRate() == null ? new BigDecimal("0.070000") : l.getTaxRate());
            pol.setExpenseAccountId(l.getExpenseAccountId());
            pol.setExpectedArrivalDate(l.getExpectedArrivalDate());
            pol.setCreatedAt(LocalDateTime.now());
            pol.setUpdatedAt(LocalDateTime.now());
            pol.setIsDeleted(0);
            lines.add(pol);
        }

        PurchaseOrder po = new PurchaseOrder();
        po.setOrderNo("PO-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        po.setSupplierId(req.getSupplierId());
        po.setBookId(req.getBookId());
        po.setPeriod(req.getPeriod());
        po.setProject(req.getProject());
        po.setCustomerId(req.getCustomerId());
        po.setDescription(req.getDescription());
        po.setOrderDate(req.getOrderDate() == null ? LocalDateTime.now() : req.getOrderDate());
        po.setTotalAmount(total);
        po.setPaidAmount(BigDecimal.ZERO);
        po.setStatus("DRAFT");
        po.setCreatedAt(LocalDateTime.now());
        po.setUpdatedAt(LocalDateTime.now());
        po.setIsDeleted(0);
        poMapper.insert(po);

        for (PurchaseOrderLine pol : lines) {
            pol.setPoId(po.getId());
            lineMapper.insert(pol);
        }

        return toResponse(po, lines);
    }

    @Override
    public PurchaseOrderResponse getById(Long id) {
        PurchaseOrder po = poMapper.selectById(id);
        if (po == null || (po.getIsDeleted() != null && po.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("采购订单不存在");
        }
        List<PurchaseOrderLine> lines = lineMapper.selectList(Wrappers.<PurchaseOrderLine>lambdaQuery()
                .eq(PurchaseOrderLine::getPoId, id)
                .eq(PurchaseOrderLine::getIsDeleted, 0));
        return toResponse(po, lines);
    }

    @Override
    public List<PurchaseOrderResponse> list() {
        List<PurchaseOrder> pos = poMapper.selectList(Wrappers.<PurchaseOrder>lambdaQuery()
                .eq(PurchaseOrder::getIsDeleted, 0)
                .orderByDesc(PurchaseOrder::getId));
        List<PurchaseOrderResponse> out = new ArrayList<>();
        for (PurchaseOrder po : pos) {
            out.add(toResponse(po, List.of()));
        }
        return out;
    }

    private static PurchaseOrderResponse toResponse(PurchaseOrder po, List<PurchaseOrderLine> lines) {
        PurchaseOrderResponse r = new PurchaseOrderResponse();
        r.setId(po.getId());
        r.setOrderNo(po.getOrderNo());
        r.setSupplierId(po.getSupplierId());
        r.setOrderDate(po.getOrderDate());
        r.setTotalAmount(po.getTotalAmount());
        r.setPaidAmount(po.getPaidAmount());
        r.setStatus(po.getStatus());
        r.setDescription(po.getDescription());

        if (lines != null && !lines.isEmpty()) {
            List<PurchaseOrderResponse.Line> ls = new ArrayList<>();
            for (PurchaseOrderLine l : lines) {
                PurchaseOrderResponse.Line rl = new PurchaseOrderResponse.Line();
                rl.setId(l.getId());
                rl.setProductCode(l.getProductCode());
                rl.setProductName(l.getProductName());
                rl.setQuantity(l.getQuantity());
                rl.setPrice(l.getPrice());
                rl.setAmount(l.getAmount());
                rl.setTaxRate(l.getTaxRate());
                rl.setExpenseAccountId(l.getExpenseAccountId());
                rl.setExpectedArrivalDate(l.getExpectedArrivalDate());
                ls.add(rl);
            }
            r.setLines(ls);
        } else {
            r.setLines(List.of());
        }
        return r;
    }
}


