package org.example.nbcompany.security;

import org.example.nbcompany.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    // 持有你自己的用户实体类
    private final SysUser sysUser;

    public CustomUserDetails(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    /**
     * 方便地获取原始的 SysUser 对象
     */
    public SysUser getSysUser() {
        return sysUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 从 SysUser 对象中获取角色信息，并转换为 GrantedAuthority 集合
        //
        String role = "ROLE_USER"; // 默认角色
        if (sysUser.getUserType() != null && sysUser.getUserType() == 2) {
            role = "ROLE_SUPER_ADMIN";
        } else if (sysUser.getCompanyRole() != null && sysUser.getCompanyRole() == 2) {
            role = "ROLE_COMPANY_ADMIN";
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        // 返回从数据库中查到的加密后的密码
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        // 返回用户名
        return sysUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 账户是否未过期，这里简单返回 true
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 账户是否未被锁定，我们根据 status 字段判断 (1:正常)
        return sysUser.getStatus() == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 凭证是否未过期，这里简单返回 true
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 账户是否启用，我们根据 status 字段判断 (1:正常)
        return sysUser.getStatus() == 1;
    }
}