package com.supermarket.finance.gl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.gl.dto.GlTxnCreateRequest;
import com.supermarket.finance.gl.dto.GlTxnResponse;
import com.supermarket.finance.gl.entity.GlSplit;
import com.supermarket.finance.gl.entity.GlTxn;
import com.supermarket.finance.gl.mapper.GlSplitMapper;
import com.supermarket.finance.gl.mapper.GlTxnMapper;
import com.supermarket.finance.gl.service.GlTxnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GlTxnServiceImpl implements GlTxnService {

    private final GlTxnMapper txnMapper;
    private final GlSplitMapper splitMapper;

    public GlTxnServiceImpl(GlTxnMapper txnMapper, GlSplitMapper splitMapper) {
        this.txnMapper = txnMapper;
        this.splitMapper = splitMapper;
    }

    @Override
    @Transactional
    public GlTxnResponse create(GlTxnCreateRequest req) {
        if (req.getBookId() == null) throw new IllegalArgumentException("bookId 不能为空");
        if (req.getSplits() == null || req.getSplits().isEmpty()) throw new IllegalArgumentException("splits 不能为空");
        validateBalanced(req.getSplits());

        GlTxn txn = new GlTxn();
        txn.setBookId(req.getBookId());
        txn.setSourceType(req.getSourceType());
        txn.setSourceId(req.getSourceId());
        txn.setTxnDate(req.getTxnDate() == null ? LocalDateTime.now() : req.getTxnDate());
        txn.setDescription(req.getDescription());
        txn.setStatus("DRAFT");
        txn.setCreatedAt(LocalDateTime.now());
        txn.setUpdatedAt(LocalDateTime.now());
        txn.setIsDeleted(0);
        txnMapper.insert(txn);

        List<GlSplit> splits = new ArrayList<>();
        for (GlTxnCreateRequest.Split s : req.getSplits()) {
            GlSplit sp = new GlSplit();
            sp.setTxnId(txn.getId());
            sp.setAccountId(s.getAccountId());
            sp.setSummary(s.getSummary() == null ? "" : s.getSummary());
            sp.setSide(normSide(s.getSide()));
            sp.setQuantity(s.getQuantity() == null ? BigDecimal.ONE : s.getQuantity());
            sp.setPrice(s.getPrice() == null ? BigDecimal.ZERO : s.getPrice());
            sp.setAmount(s.getAmount());
            sp.setCreatedAt(LocalDateTime.now());
            sp.setUpdatedAt(LocalDateTime.now());
            sp.setIsDeleted(0);
            splitMapper.insert(sp);
            splits.add(sp);
        }

        return toResponse(txn, splits);
    }

    @Override
    public GlTxnResponse get(Long id) {
        GlTxn txn = txnMapper.selectById(id);
        if (txn == null || (txn.getIsDeleted() != null && txn.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("凭证不存在");
        }
        List<GlSplit> splits = splitMapper.selectList(Wrappers.<GlSplit>lambdaQuery()
                .eq(GlSplit::getTxnId, id)
                .eq(GlSplit::getIsDeleted, 0));
        return toResponse(txn, splits);
    }

    @Override
    public List<GlTxnResponse> list(Long bookId) {
        if (bookId == null) throw new IllegalArgumentException("bookId 不能为空");
        List<GlTxn> txns = txnMapper.selectList(Wrappers.<GlTxn>lambdaQuery()
                .eq(GlTxn::getBookId, bookId)
                .eq(GlTxn::getIsDeleted, 0)
                .orderByDesc(GlTxn::getId));
        List<GlTxnResponse> out = new ArrayList<>();
        for (GlTxn t : txns) {
            GlTxnResponse r = new GlTxnResponse();
            r.setId(t.getId());
            r.setBookId(t.getBookId());
            r.setTxnDate(t.getTxnDate());
            r.setDescription(t.getDescription());
            r.setStatus(t.getStatus());
            r.setPostedAt(t.getPostedAt());
            r.setSplits(List.of());
            out.add(r);
        }
        return out;
    }

    @Override
    @Transactional
    public GlTxnResponse post(Long id) {
        GlTxn txn = txnMapper.selectById(id);
        if (txn == null || (txn.getIsDeleted() != null && txn.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("凭证不存在");
        }
        if ("POSTED".equalsIgnoreCase(txn.getStatus())) {
            return get(id);
        }
        List<GlSplit> splits = splitMapper.selectList(Wrappers.<GlSplit>lambdaQuery()
                .eq(GlSplit::getTxnId, id)
                .eq(GlSplit::getIsDeleted, 0));
        validateBalanced(splits.stream().map(s -> {
            GlTxnCreateRequest.Split x = new GlTxnCreateRequest.Split();
            x.setSide(s.getSide());
            x.setAmount(s.getAmount());
            x.setAccountId(s.getAccountId());
            return x;
        }).toList());

        txn.setStatus("POSTED");
        txn.setPostedAt(LocalDateTime.now());
        txn.setUpdatedAt(LocalDateTime.now());
        txnMapper.updateById(txn);
        return toResponse(txn, splits);
    }

    private static void validateBalanced(List<GlTxnCreateRequest.Split> splits) {
        BigDecimal dr = BigDecimal.ZERO;
        BigDecimal cr = BigDecimal.ZERO;
        for (GlTxnCreateRequest.Split s : splits) {
            if (s.getAccountId() == null) throw new IllegalArgumentException("accountId 不能为空");
            if (s.getAmount() == null) throw new IllegalArgumentException("amount 不能为空");
            if (s.getAmount().compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("amount 必须 >= 0");
            String side = normSide(s.getSide());
            if ("DR".equals(side)) dr = dr.add(s.getAmount());
            else cr = cr.add(s.getAmount());
        }
        if (dr.compareTo(cr) != 0) {
            throw new IllegalArgumentException("借贷不平衡：借方=" + dr + "，贷方=" + cr);
        }
    }

    private static String normSide(String side) {
        if (side == null) throw new IllegalArgumentException("side 不能为空");
        String s = side.trim().toUpperCase();
        if (!s.equals("DR") && !s.equals("CR")) throw new IllegalArgumentException("side 只能为 DR/CR");
        return s;
    }

    private static GlTxnResponse toResponse(GlTxn txn, List<GlSplit> splits) {
        GlTxnResponse r = new GlTxnResponse();
        r.setId(txn.getId());
        r.setBookId(txn.getBookId());
        r.setTxnDate(txn.getTxnDate());
        r.setDescription(txn.getDescription());
        r.setStatus(txn.getStatus());
        r.setPostedAt(txn.getPostedAt());
        List<GlTxnResponse.Split> out = new ArrayList<>();
        for (GlSplit s : splits) {
            GlTxnResponse.Split x = new GlTxnResponse.Split();
            x.setId(s.getId());
            x.setAccountId(s.getAccountId());
            x.setSummary(s.getSummary());
            x.setSide(s.getSide());
            x.setAmount(s.getAmount());
            x.setQuantity(s.getQuantity());
            x.setPrice(s.getPrice());
            out.add(x);
        }
        r.setSplits(out);
        return r;
    }
}








