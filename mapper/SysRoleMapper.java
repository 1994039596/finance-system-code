package com.supermarket.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.finance.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    @Select("select * from sys_role where code = #{code} and is_deleted = 0 limit 1")
    SysRole findByCode(String code);
}








