package org.example.nbcompany.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nbcompany.entity.SysUser;

import java.util.List;

@Mapper
public interface SysUserMapper {
    void insert(SysUser user);
    SysUser selectById(Long id);
    SysUser selectByUsername(String username);
    void updateById(SysUser user);
    void deleteById(Long id);
    
    List<SysUser> selectByConditions(@Param("companyId") Long companyId,
                                   @Param("companyRole") Integer companyRole,
                                   @Param("userType") Integer userType,
                                   @Param("username") String username,
                                   @Param("phoneNumber") String phoneNumber,
                                   @Param("status") Integer status,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);
    
    long countByConditions(@Param("companyId") Long companyId,
                          @Param("companyRole") Integer companyRole,
                          @Param("userType") Integer userType,
                          @Param("username") String username,
                          @Param("phoneNumber") String phoneNumber,
                          @Param("status") Integer status);
    
    List<SysUser> selectByCompanyId(@Param("companyId") Long companyId,
                                  @Param("username") String username,
                                  @Param("companyRole") Integer companyRole,
                                  @Param("status") Integer status,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);
    
    long countByCompanyId(@Param("companyId") Long companyId,
                         @Param("username") String username,
                         @Param("companyRole") Integer companyRole,
                         @Param("status") Integer status);
} 