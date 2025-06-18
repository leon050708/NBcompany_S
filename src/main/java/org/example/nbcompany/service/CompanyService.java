package org.example.nbcompany.service;

import org.example.nbcompany.dto.request.CompanyRegisterRequest;
import org.example.nbcompany.dto.response.CompanyRegisterResponse;
import org.example.nbcompany.dto.response.PageResponse;
import org.example.nbcompany.entity.SysCompany;

public interface CompanyService {
    CompanyRegisterResponse register(CompanyRegisterRequest request);
    PageResponse<SysCompany> listCompanies(String keyword, int page, int size);
    void updateCompanyStatus(Long companyId, Integer status);
} 