package com.supermarket.finance.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.supermarket.finance.entity.SysPerm;
import com.supermarket.finance.entity.SysRole;
import com.supermarket.finance.entity.SysRolePerm;
import com.supermarket.finance.entity.SysUser;
import com.supermarket.finance.entity.SysUserRole;
import com.supermarket.finance.mapper.SysPermMapper;
import com.supermarket.finance.mapper.SysRoleMapper;
import com.supermarket.finance.mapper.SysRolePermMapper;
import com.supermarket.finance.mapper.SysUserMapper;
import com.supermarket.finance.mapper.SysUserRoleMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysPermMapper permMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRolePermMapper rolePermMapper;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public DataInitializer(SysUserMapper userMapper,
                           SysRoleMapper roleMapper,
                           SysPermMapper permMapper,
                           SysUserRoleMapper userRoleMapper,
                           SysRolePermMapper rolePermMapper,
                           PasswordEncoder passwordEncoder,
                           JdbcTemplate jdbcTemplate) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.permMapper = permMapper;
        this.userRoleMapper = userRoleMapper;
        this.rolePermMapper = rolePermMapper;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        // 0) 初始化默认账套 + 默认科目（用于总账/分录模块快速可用）
        initDefaultBookAndAccounts();

        // 1) 角色 ADMIN
        SysRole adminRole = roleMapper.findByCode("ADMIN");
        if (adminRole == null) {
            adminRole = new SysRole();
            adminRole.setCode("ADMIN");
            adminRole.setName("系统管理员");
            adminRole.setCreatedAt(LocalDateTime.now());
            adminRole.setUpdatedAt(LocalDateTime.now());
            adminRole.setIsDeleted(0);
            roleMapper.insert(adminRole);
        }

        // 1.1) 角色 VIEWER（只读）
        SysRole viewerRole = roleMapper.findByCode("VIEWER");
        if (viewerRole == null) {
            viewerRole = new SysRole();
            viewerRole.setCode("VIEWER");
            viewerRole.setName("只读用户");
            viewerRole.setCreatedAt(LocalDateTime.now());
            viewerRole.setUpdatedAt(LocalDateTime.now());
            viewerRole.setIsDeleted(0);
            roleMapper.insert(viewerRole);
        }

        // 2) 一些基础权限（示例）
        List<SysPerm> basePerms = List.of(
                perm("order:add", "新增订单"),
                perm("order:list", "订单列表"),
                perm("supplier:add", "新增供应商"),
                perm("supplier:list", "供应商列表"),
                perm("purchase:order:create", "采购订单-创建"),
                perm("purchase:order:view", "采购订单-查看"),
                perm("purchase:order:list", "采购订单-列表"),
                perm("supplier:fin:create", "供应商档案(增强)-创建"),
                perm("supplier:fin:view", "供应商档案(增强)-查看"),
                perm("supplier:fin:list", "供应商档案(增强)-列表"),
                perm("gl:txn:create", "总账-凭证创建"),
                perm("gl:txn:view", "总账-凭证查看"),
                perm("gl:txn:list", "总账-凭证列表"),
                perm("gl:txn:post", "总账-凭证过账"),
                perm("gl:account:list", "总账-科目列表")
                ,perm("payment:create", "采购支付-创建")
                ,perm("payment:list", "采购支付-列表")
                ,perm("customer:create", "客户-创建")
                ,perm("customer:view", "客户-查看")
                ,perm("customer:list", "客户-列表")
                ,perm("bank:statement:create", "银行对账-对账单创建")
                ,perm("bank:statement:list", "银行对账-对账单列表")
                ,perm("bank:journal:create", "银行对账-企业日记账创建")
                ,perm("bank:recon:auto", "银行对账-自动匹配")
                ,perm("report:generate", "报表-生成")
                ,perm("report:view", "报表-查看")
                ,perm("tax:adjust:create", "税率调整-提交")
                ,perm("tax:adjust:list", "税率调整-列表")
                ,perm("tax:adjust:approve", "税率调整-审核")
                ,perm("tax:adjust:apply", "税率调整-生效执行")
                ,perm("hr:employee:create", "员工-创建")
                ,perm("hr:employee:list", "员工-列表")
                ,perm("hr:salary:config", "薪资-配置")
                ,perm("hr:salary:view", "薪资-查看")
                ,perm("hr:salary:pay", "薪资-发放")
                ,perm("hr:salary:list", "薪资-发放记录")
        );
        for (SysPerm p : basePerms) {
            SysPerm exist = permMapper.findByCode(p.getCode());
            if (exist == null) {
                p.setCreatedAt(LocalDateTime.now());
                p.setUpdatedAt(LocalDateTime.now());
                p.setIsDeleted(0);
                permMapper.insert(p);
                exist = p;
            }
            // 绑定到 ADMIN 角色（幂等）
            long count = rolePermMapper.selectCount(new LambdaQueryWrapper<SysRolePerm>()
                    .eq(SysRolePerm::getRoleId, adminRole.getId())
                    .eq(SysRolePerm::getPermId, exist.getId()));
            if (count == 0) {
                SysRolePerm rp = new SysRolePerm();
                rp.setRoleId(adminRole.getId());
                rp.setPermId(exist.getId());
                rp.setCreatedAt(LocalDateTime.now());
                rolePermMapper.insert(rp);
            }
        }

        // 2.1) 给 VIEWER 绑定只读权限（幂等）
        List<String> viewerPermCodes = List.of(
                "purchase:order:list",
                "purchase:order:view",
                "supplier:fin:list",
                "supplier:fin:view",
                "customer:list",
                "customer:view",
                "gl:account:list",
                "gl:txn:list",
                "gl:txn:view",
                "payment:list",
                "bank:statement:list",
                "report:view",
                "tax:adjust:list",
                "hr:employee:list",
                "hr:salary:view",
                "hr:salary:list"
        );
        for (String code : viewerPermCodes) {
            SysPerm p = permMapper.findByCode(code);
            if (p == null) continue;
            long c = rolePermMapper.selectCount(new LambdaQueryWrapper<SysRolePerm>()
                    .eq(SysRolePerm::getRoleId, viewerRole.getId())
                    .eq(SysRolePerm::getPermId, p.getId()));
            if (c == 0) {
                SysRolePerm rp = new SysRolePerm();
                rp.setRoleId(viewerRole.getId());
                rp.setPermId(p.getId());
                rp.setCreatedAt(LocalDateTime.now());
                rolePermMapper.insert(rp);
            }
        }

        // 3) 初始管理员账号：admin / admin123
        SysUser admin = userMapper.findByUsername("admin");
        if (admin == null) {
            admin = new SysUser();
            admin.setUsername("admin");
            admin.setDisplayName("管理员");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setStatus(1);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            admin.setIsDeleted(0);
            userMapper.insert(admin);
        }

        // 绑定 admin -> ADMIN 角色（幂等）
        long urCount = userRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, admin.getId())
                .eq(SysUserRole::getRoleId, adminRole.getId()));
        if (urCount == 0) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(admin.getId());
            ur.setRoleId(adminRole.getId());
            ur.setCreatedAt(LocalDateTime.now());
            userRoleMapper.insert(ur);
        }
    }

    private void initDefaultBookAndAccounts() {
        Integer bookCount = jdbcTemplate.queryForObject("select count(1) from fin_book where is_deleted = 0", Integer.class);
        if (bookCount == null) bookCount = 0;
        if (bookCount == 0) {
            jdbcTemplate.update("""
                    insert into fin_book (name, book_date, base_ccy, status)
                    values ('默认账套', now(), 'CNY', 1)
                    """);
        }
        Long bookId = jdbcTemplate.queryForObject("select id from fin_book where is_deleted = 0 order by id limit 1", Long.class);
        if (bookId == null) return;

        // 默认科目：按照“资产/负债/权益/收入/成本/费用”补齐一套更完整的科目表
        // 说明：使用 insert ignore，确保“缺啥补啥”，不会覆盖你已有科目/自定义科目
        jdbcTemplate.update("""
            insert ignore into gl_account (book_id, code, name, type, parent_id, normal_side, status, created_at, updated_at, is_deleted)
            values
              -- 资产 ASSET
              (?, '1001', '库存现金', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1002', '银行存款', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1012', '其他货币资金', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1122', '应收账款', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1131', '应收票据', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1221', '其他应收款', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1403', '原材料', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1405', '库存商品', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1407', '在途物资', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1501', '固定资产', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1502', '累计折旧', 'ASSET', null, 'CR', 1, now(), now(), 0),
              (?, '1601', '无形资产', 'ASSET', null, 'DR', 1, now(), now(), 0),
              (?, '1602', '累计摊销', 'ASSET', null, 'CR', 1, now(), now(), 0),
              (?, '1701', '长期待摊费用', 'ASSET', null, 'DR', 1, now(), now(), 0),

              -- 负债 LIABILITY
              (?, '2001', '短期借款', 'LIABILITY', null, 'CR', 1, now(), now(), 0),
              (?, '2202', '应付账款', 'LIABILITY', null, 'CR', 1, now(), now(), 0),
              (?, '2211', '应付职工薪酬', 'LIABILITY', null, 'CR', 1, now(), now(), 0),
              (?, '2221', '应交税费', 'LIABILITY', null, 'CR', 1, now(), now(), 0),
              (?, '2241', '其他应付款', 'LIABILITY', null, 'CR', 1, now(), now(), 0),
              (?, '2301', '长期借款', 'LIABILITY', null, 'CR', 1, now(), now(), 0),

              -- 权益 EQUITY
              (?, '3001', '实收资本', 'EQUITY', null, 'CR', 1, now(), now(), 0),
              (?, '3002', '资本公积', 'EQUITY', null, 'CR', 1, now(), now(), 0),
              (?, '3101', '盈余公积', 'EQUITY', null, 'CR', 1, now(), now(), 0),
              (?, '3103', '未分配利润', 'EQUITY', null, 'CR', 1, now(), now(), 0),

              -- 收入 REVENUE
              (?, '5001', '主营业务收入', 'REVENUE', null, 'CR', 1, now(), now(), 0),
              (?, '5051', '其他业务收入', 'REVENUE', null, 'CR', 1, now(), now(), 0),

              -- 成本 COST
              (?, '6001', '主营业务成本', 'COST', null, 'DR', 1, now(), now(), 0),
              (?, '6051', '其他业务成本', 'COST', null, 'DR', 1, now(), now(), 0),

              -- 费用 EXPENSE
              (?, '6601', '销售费用', 'EXPENSE', null, 'DR', 1, now(), now(), 0),
              (?, '6602', '管理费用', 'EXPENSE', null, 'DR', 1, now(), now(), 0),
              (?, '6603', '财务费用', 'EXPENSE', null, 'DR', 1, now(), now(), 0),
              (?, '6604', '研发费用', 'EXPENSE', null, 'DR', 1, now(), now(), 0),
              (?, '6403', '税金及附加', 'EXPENSE', null, 'DR', 1, now(), now(), 0),
              (?, '6711', '资产减值损失', 'EXPENSE', null, 'DR', 1, now(), now(), 0)
            """,
                // 资产
                bookId, bookId, bookId, bookId, bookId, bookId, bookId, bookId, bookId, bookId, bookId, bookId, bookId, bookId,
                // 负债
                bookId, bookId, bookId, bookId, bookId, bookId,
                // 权益
                bookId, bookId, bookId, bookId,
                // 收入
                bookId, bookId,
                // 成本
                bookId, bookId,
                // 费用
                bookId, bookId, bookId, bookId, bookId, bookId
        );
    }

    private static SysPerm perm(String code, String name) {
        SysPerm p = new SysPerm();
        p.setCode(code);
        p.setName(name);
        return p;
    }
}


