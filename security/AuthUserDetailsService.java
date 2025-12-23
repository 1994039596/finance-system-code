package com.supermarket.finance.security;

import com.supermarket.finance.entity.SysPerm;
import com.supermarket.finance.entity.SysRole;
import com.supermarket.finance.entity.SysUser;
import com.supermarket.finance.mapper.SysRolePermMapper;
import com.supermarket.finance.mapper.SysUserMapper;
import com.supermarket.finance.mapper.SysUserRoleMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRolePermMapper rolePermMapper;

    public AuthUserDetailsService(SysUserMapper userMapper,
                                  SysUserRoleMapper userRoleMapper,
                                  SysRolePermMapper rolePermMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.rolePermMapper = rolePermMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.findByUsername(username);
        if (user == null || user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new UsernameNotFoundException("用户已停用");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        List<SysRole> roles = userRoleMapper.listRolesByUserId(user.getId());
        for (SysRole r : roles) {
            if (r.getCode() != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getCode()));
            }
            List<SysPerm> perms = rolePermMapper.listPermsByRoleId(r.getId());
            for (SysPerm p : perms) {
                if (p.getCode() != null) {
                    authorities.add(new SimpleGrantedAuthority(p.getCode()));
                }
            }
        }

        return User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .build();
    }
}








