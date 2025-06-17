package org.example.nbcompany.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nbcompany.entity.SysCompany;

import java.util.List;

@Mapper
public interface SysCompanyDao {

    SysCompany findById(@Param("id") Long id);

    SysCompany findByCompanyName(@Param("companyName") String companyName);

    List<SysCompany> findAllCompanies(@Param("keyword") String keyword);

    int insert(SysCompany company);

    int update(SysCompany company);

    int deleteById(@Param("id") Long id);
}