package org.example.nbcompany.security;

import org.example.nbcompany.dao.SysUserDao;
import org.example.nbcompany.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用你的 DAO 根据用户名查询用户
        SysUser sysUser = sysUserDao.findByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 将查询到的 SysUser 对象包装成 CustomUserDetails 对象返回
        return new CustomUserDetails(sysUser);
    }
}