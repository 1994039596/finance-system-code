package com.supermarket.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.finance.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    @Select("""
        select r.*
        from sys_role r
        join sys_user_role ur on ur.role_id = r.id
        where ur.user_id = #{userId} and r.is_deleted = 0
        """)
    List<com.supermarket.finance.entity.SysRole> listRolesByUserId(Long userId);
}








