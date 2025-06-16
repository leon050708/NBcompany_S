package org.example.nbcompany.dao;

import org.example.nbcompany.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserDao {

    SysUser findById(@Param("id") Long id);

    SysUser findByUsername(@Param("username") String username);

    // PageHelper 会拦截这个方法，实现物理分页
    List<SysUser> findAll();

    int insert(SysUser user);

    int update(SysUser user);

    int deleteById(@Param("id") Long id);
}