package com.supermarket.finance.payment.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.gl.dto.GlTxnCreateRequest;
import com.supermarket.finance.gl.entity.GlAccount;
import com.supermarket.finance.gl.mapper.GlAccountMapper;
import com.supermarket.finance.gl.service.GlTxnService;
import com.supermarket.finance.payment.dto.PaymentCreateRequest;
import com.supermarket.finance.payment.entity.FinPayment;
import com.supermarket.finance.payment.mapper.FinPaymentMapper;
import com.supermarket.finance.payment.service.PaymentService;
import com.supermarket.finance.purchase.entity.PurchaseOrder;
import com.supermarket.finance.purchase.mapper.PurchaseOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final FinPaymentMapper paymentMapper;
    private final PurchaseOrderMapper poMapper;
    private final GlAccountMapper accountMapper;
    private final GlTxnService glTxnService;

    public PaymentServiceImpl(FinPaymentMapper paymentMapper,
                              PurchaseOrderMapper poMapper,
                              GlAccountMapper accountMapper,
                              GlTxnService glTxnService) {
        this.paymentMapper = paymentMapper;
        this.poMapper = poMapper;
        this.accountMapper = accountMapper;
        this.glTxnService = glTxnService;
    }

    @Override
    @Transactional
    public FinPayment create(PaymentCreateRequest req) {
        if (req.getPoId() == null) throw new IllegalArgumentException("poId 不能为空");
        if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount 必须 > 0");
        }
        String method = (req.getMethod() == null || req.getMethod().isBlank()) ? "BANK" : req.getMethod().trim().toUpperCase();

        PurchaseOrder po = poMapper.selectById(req.getPoId());
        if (po == null || (po.getIsDeleted() != null && po.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("采购订单不存在");
        }

        BigDecimal paid = po.getPaidAmount() == null ? BigDecimal.ZERO : po.getPaidAmount();
        BigDecimal total = po.getTotalAmount() == null ? BigDecimal.ZERO : po.getTotalAmount();
        BigDecimal remain = total.subtract(paid);
        if (req.getAmount().compareTo(remain) > 0) {
            throw new IllegalArgumentException("支付金额超出未付金额，未付=" + remain);
        }

        FinPayment p = new FinPayment();
        p.setPaymentNo("PAY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        p.setPoId(po.getId());
        p.setSupplierId(po.getSupplierId());
        p.setPayDate(LocalDateTime.now());
        p.setAmount(req.getAmount());
        p.setMethod(method);
        p.setBankAccount(req.getBankAccount());
        p.setStatus("SUCCESS");
        p.setRemark(req.getRemark());
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        p.setIsDeleted(0);
        paymentMapper.insert(p);

        // 更新采购订单已付金额与状态
        BigDecimal newPaid = paid.add(req.getAmount());
        po.setPaidAmount(newPaid);
        if (newPaid.compareTo(total) >= 0) po.setStatus("PAID");
        else po.setStatus("PART_PAID");
        po.setUpdatedAt(LocalDateTime.now());
        poMapper.updateById(po);

        // 生成并过账总账凭证：借 应付账款(2202) 贷 银行存款(1002)
        Long bookId = po.getBookId() == null ? 1L : po.getBookId();
        GlAccount ap = findAccount(bookId, "2202");  // 应付账款
        GlAccount bank = findAccount(bookId, "1002"); // 银行存款
        if (ap == null || bank == null) {
            throw new IllegalArgumentException("缺少默认科目（2202/1002），请先初始化科目");
        }

        GlTxnCreateRequest txn = new GlTxnCreateRequest();
        txn.setBookId(bookId);
        txn.setSourceType("PAYMENT");
        txn.setSourceId(p.getId());
        txn.setTxnDate(LocalDateTime.now());
        txn.setDescription("采购付款 " + p.getPaymentNo());

        GlTxnCreateRequest.Split dr = new GlTxnCreateRequest.Split();
        dr.setAccountId(ap.getId());
        dr.setSummary("采购付款-冲应付");
        dr.setSide("DR");
        dr.setAmount(req.getAmount());

        GlTxnCreateRequest.Split cr = new GlTxnCreateRequest.Split();
        cr.setAccountId(bank.getId());
        cr.setSummary("采购付款-出银行");
        cr.setSide("CR");
        cr.setAmount(req.getAmount());

        txn.setSplits(List.of(dr, cr));
        Long txnId = glTxnService.create(txn).getId();
        glTxnService.post(txnId);

        return p;
    }

    @Override
    public List<FinPayment> listByPoId(Long poId) {
        if (poId == null) throw new IllegalArgumentException("poId 不能为空");
        return paymentMapper.selectList(Wrappers.<FinPayment>lambdaQuery()
                .eq(FinPayment::getPoId, poId)
                .eq(FinPayment::getIsDeleted, 0)
                .orderByDesc(FinPayment::getId));
    }

    private GlAccount findAccount(Long bookId, String code) {
        return accountMapper.selectOne(Wrappers.<GlAccount>lambdaQuery()
                .eq(GlAccount::getBookId, bookId)
                .eq(GlAccount::getCode, code)
                .eq(GlAccount::getIsDeleted, 0)
                .last("limit 1"));
    }
}








