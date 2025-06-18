package org.example.nbcompany.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nbcompany.entity.SysCompany;

import java.util.List;

@Mapper
public interface SysCompanyMapper {
    void insert(SysCompany company);
    SysCompany selectById(Long id);
    void updateById(SysCompany company);
    List<SysCompany> selectByKeyword(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);
    long countByKeyword(@Param("keyword") String keyword);
} 