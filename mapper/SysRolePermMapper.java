package com.supermarket.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.finance.entity.SysRolePerm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRolePermMapper extends BaseMapper<SysRolePerm> {
    @Select("""
        select p.*
        from sys_perm p
        join sys_role_perm rp on rp.perm_id = p.id
        where rp.role_id = #{roleId} and p.is_deleted = 0
        """)
    List<com.supermarket.finance.entity.SysPerm> listPermsByRoleId(Long roleId);
}








