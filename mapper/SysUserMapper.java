package com.supermarket.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supermarket.finance.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("select * from sys_user where username = #{username} and is_deleted = 0 limit 1")
    SysUser findByUsername(String username);
}








