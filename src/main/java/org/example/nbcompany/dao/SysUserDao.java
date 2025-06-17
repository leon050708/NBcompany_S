package org.example.nbcompany.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nbcompany.entity.SysUser;

import java.util.List;

@Mapper
public interface SysUserDao {

    SysUser findById(@Param("id") Long id);

    SysUser findByUsername(@Param("username") String username);

    // 新增：根据条件查找用户列表（用于平台管理员）
    List<SysUser> findUsersByCriteria(
            @Param("companyId") Long companyId,
            @Param("companyRole") Integer companyRole,
            @Param("userType") Integer userType,
            @Param("username") String username,
            @Param("phoneNumber") String phoneNumber,
            @Param("status") Integer status);

    // 新增：根据企业ID查找企业成员（用于企业管理员）
    List<SysUser> findCompanyMembers(
            @Param("companyId") Long companyId,
            @Param("username") String username,
            @Param("companyRole") Integer companyRole,
            @Param("status") Integer status);

    int insert(SysUser user);

    int update(SysUser user);

    int deleteById(@Param("id") Long id);
}