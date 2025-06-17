package org.example.nbcompany.service;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.CompanyDto.CompanyDTO;
import org.example.nbcompany.dto.UserDto.RegisterCompanyDTO;
import org.example.nbcompany.dto.UserDto.UpdateCompanyStatusDTO;

public interface CompanyService {

    CompanyDTO registerCompany(RegisterCompanyDTO registerCompanyDTO);

    PageInfo<CompanyDTO> getAllCompanies(String keyword, int pageNum, int pageSize);

    CompanyDTO getCompanyById(Long id);

    void updateCompanyStatus(Long companyId, UpdateCompanyStatusDTO updateCompanyStatusDTO);

    void deleteCompany(Long companyId); // 酌情添加，通常企业不直接删除
}