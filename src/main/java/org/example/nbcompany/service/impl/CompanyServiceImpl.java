package org.example.nbcompany.service.impl;

import org.example.nbcompany.dao.SysCompanyMapper;
import org.example.nbcompany.dto.request.CompanyRegisterRequest;
import org.example.nbcompany.dto.response.CompanyRegisterResponse;
import org.example.nbcompany.dto.response.PageResponse;
import org.example.nbcompany.entity.SysCompany;
import org.example.nbcompany.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private SysCompanyMapper companyMapper;

    @Override
    @Transactional
    public CompanyRegisterResponse register(CompanyRegisterRequest request) {
        SysCompany company = new SysCompany();
        company.setCompanyName(request.getCompanyName());
        company.setContactPerson(request.getContactPerson());
        company.setContactPhone(request.getContactPhone());
        company.setContactEmail(request.getContactEmail());
        company.setStatus(0); // 待审核状态
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        companyMapper.insert(company);

        CompanyRegisterResponse response = new CompanyRegisterResponse();
        response.setCompanyId(company.getId());
        response.setCompanyName(company.getCompanyName());
        return response;
    }

    @Override
    public PageResponse<SysCompany> listCompanies(String keyword, int page, int size) {
        PageResponse<SysCompany> response = new PageResponse<>();
        response.setCurrent(page);
        response.setRecords(companyMapper.selectByKeyword(keyword, (page - 1) * size, size));
        response.setTotal(companyMapper.countByKeyword(keyword));
        response.setPages((int) Math.ceil((double) response.getTotal() / size));
        return response;
    }

    @Override
    @Transactional
    public void updateCompanyStatus(Long companyId, Integer status) {
        SysCompany company = companyMapper.selectById(companyId);
        if (company == null) {
            throw new RuntimeException("企业不存在");
        }
        company.setStatus(status);
        company.setUpdatedAt(LocalDateTime.now());
        companyMapper.updateById(company);
    }
} 