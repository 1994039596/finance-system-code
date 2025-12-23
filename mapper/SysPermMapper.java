package com.supermarket.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.finance.entity.SysPerm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysPermMapper extends BaseMapper<SysPerm> {
    @Select("select * from sys_perm where code = #{code} and is_deleted = 0 limit 1")
    SysPerm findByCode(String code);
}








