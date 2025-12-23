package com.supermarket.finance.customer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.supermarket.finance.customer.dto.CustomerCreateRequest;
import com.supermarket.finance.customer.entity.FinCustomer;
import com.supermarket.finance.customer.entity.FinCustomerAccount;
import com.supermarket.finance.customer.entity.FinCustomerCredit;
import com.supermarket.finance.customer.mapper.FinCustomerAccountMapper;
import com.supermarket.finance.customer.mapper.FinCustomerCreditMapper;
import com.supermarket.finance.customer.mapper.FinCustomerMapper;
import com.supermarket.finance.customer.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final FinCustomerMapper customerMapper;
    private final FinCustomerCreditMapper creditMapper;
    private final FinCustomerAccountMapper accountMapper;

    public CustomerServiceImpl(FinCustomerMapper customerMapper,
                               FinCustomerCreditMapper creditMapper,
                               FinCustomerAccountMapper accountMapper) {
        this.customerMapper = customerMapper;
        this.creditMapper = creditMapper;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional
    public FinCustomer create(CustomerCreateRequest req) {
        if (req.getType() == null || req.getType().isBlank()) throw new IllegalArgumentException("type 不能为空");
        if (req.getName() == null || req.getName().isBlank()) throw new IllegalArgumentException("name 不能为空");
        if (req.getIdNumber() == null || req.getIdNumber().isBlank()) throw new IllegalArgumentException("idNumber 不能为空");

        long dup = customerMapper.selectCount(Wrappers.<FinCustomer>lambdaQuery()
                .eq(FinCustomer::getIdNumber, req.getIdNumber())
                .eq(FinCustomer::getIsDeleted, 0));
        if (dup > 0) throw new IllegalArgumentException("证件号已存在");

        FinCustomer c = new FinCustomer();
        c.setType(req.getType().trim().toUpperCase());
        c.setName(req.getName());
        c.setIdNumber(req.getIdNumber());
        c.setStatus("ACTIVE");
        c.setCreateTime(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        c.setIsDeleted(0);
        customerMapper.insert(c);

        // 初始化信用额度
        FinCustomerCredit credit = new FinCustomerCredit();
        credit.setCustomerId(c.getId());
        credit.setCreditLimit(req.getCreditLimit() == null ? BigDecimal.ZERO : req.getCreditLimit());
        credit.setCurrentPayable(BigDecimal.ZERO);
        credit.setCreatedAt(LocalDateTime.now());
        credit.setUpdatedAt(LocalDateTime.now());
        credit.setIsDeleted(0);
        creditMapper.insert(credit);

        // 初始化收款账户（可选）
        if (req.getAccountNo() != null && !req.getAccountNo().isBlank()) {
            FinCustomerAccount acc = new FinCustomerAccount();
            acc.setCustomerId(c.getId());
            acc.setBank(req.getBank() == null ? "" : req.getBank());
            acc.setAccountNo(req.getAccountNo());
            acc.setBalance(BigDecimal.ZERO);
            acc.setStatus("ACTIVE");
            acc.setCreatedAt(LocalDateTime.now());
            acc.setUpdatedAt(LocalDateTime.now());
            acc.setIsDeleted(0);
            accountMapper.insert(acc);
        }

        return c;
    }

    @Override
    public FinCustomer get(Long id) {
        FinCustomer c = customerMapper.selectById(id);
        if (c == null || (c.getIsDeleted() != null && c.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("客户不存在");
        }
        return c;
    }

    @Override
    public List<FinCustomer> list() {
        return customerMapper.selectList(Wrappers.<FinCustomer>lambdaQuery()
                .eq(FinCustomer::getIsDeleted, 0)
                .orderByDesc(FinCustomer::getId));
    }
}








