package com.supermarket.finance.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.entity.SysRole;
import com.supermarket.finance.entity.SysUser;
import com.supermarket.finance.entity.SysUserRole;
import com.supermarket.finance.mapper.SysRoleMapper;
import com.supermarket.finance.mapper.SysUserMapper;
import com.supermarket.finance.mapper.SysUserRoleMapper;
import com.supermarket.finance.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          SysUserMapper userMapper,
                          SysRoleMapper roleMapper,
                          SysUserRoleMapper userRoleMapper,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String token = tokenProvider.generateToken(req.getUsername(), authorities);
        return Result.success(new LoginResponse(token, authorities));
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody @Valid RegisterRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()) throw new IllegalArgumentException("username 不能为空");
        if (req.getPassword() == null || req.getPassword().isBlank()) throw new IllegalArgumentException("password 不能为空");
        if (req.getPassword().length() < 6) throw new IllegalArgumentException("password 至少 6 位");

        String username = req.getUsername().trim();
        if (userMapper.findByUsername(username) != null) throw new IllegalArgumentException("用户名已存在");

        SysRole viewer = roleMapper.findByCode("VIEWER");
        if (viewer == null) throw new IllegalArgumentException("系统未初始化 VIEWER 角色，请重启后端");

        SysUser u = new SysUser();
        u.setUsername(username);
        u.setDisplayName((req.getDisplayName() == null || req.getDisplayName().isBlank()) ? username : req.getDisplayName().trim());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        u.setStatus(1);
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());
        u.setIsDeleted(0);
        userMapper.insert(u);

        SysUserRole ur = new SysUserRole();
        ur.setUserId(u.getId());
        ur.setRoleId(viewer.getId());
        ur.setCreatedAt(LocalDateTime.now());
        userRoleMapper.insert(ur);

        // 注册后直接登录
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, req.getPassword())
        );
        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String token = tokenProvider.generateToken(username, authorities);
        return Result.success(new LoginResponse(token, authorities));
    }

    @GetMapping("/me")
    public Result<MeResponse> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null || "anonymousUser".equals(String.valueOf(auth.getPrincipal()))) {
            return Result.unauthorized("未登录");
        }
        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return Result.success(new MeResponse(String.valueOf(auth.getPrincipal()), authorities));
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String displayName;
    }

    @Data
    public static class LoginResponse {
        private final String token;
        private final List<String> authorities;
    }

    @Data
    public static class MeResponse {
        private final String username;
        private final List<String> authorities;
    }
}


