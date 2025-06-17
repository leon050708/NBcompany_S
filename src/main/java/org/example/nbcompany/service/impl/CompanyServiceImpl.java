package org.example.nbcompany.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dao.SysCompanyDao;
import org.example.nbcompany.dto.CompanyDto.CompanyDTO;
import org.example.nbcompany.dto.UserDto.RegisterCompanyDTO;
import org.example.nbcompany.dto.UserDto.UpdateCompanyStatusDTO;
import org.example.nbcompany.entity.SysCompany;
import org.example.nbcompany.service.CompanyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private SysCompanyDao sysCompanyDao;

    @Override
    @Transactional
    public CompanyDTO registerCompany(RegisterCompanyDTO registerCompanyDTO) {
        // 检查企业名称是否已存在
        if (sysCompanyDao.findByCompanyName(registerCompanyDTO.getCompanyName()) != null) {
            throw new RuntimeException("企业名称已存在");
        }

        SysCompany sysCompany = new SysCompany();
        BeanUtils.copyProperties(registerCompanyDTO, sysCompany);
        sysCompany.setStatus(0); // 新注册企业状态为0 (待审核)
        sysCompanyDao.insert(sysCompany);

        CompanyDTO companyDTO = new CompanyDTO();
        BeanUtils.copyProperties(sysCompany, companyDTO);
        return companyDTO;
    }

    @Override
    public PageInfo<CompanyDTO> getAllCompanies(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysCompany> companyList = sysCompanyDao.findAllCompanies(keyword);
        PageInfo<SysCompany> companyPageInfo = new PageInfo<>(companyList);

        List<CompanyDTO> companyDTOList = companyList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        PageInfo<CompanyDTO> companyDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(companyPageInfo, companyDtoPageInfo);
        companyDtoPageInfo.setList(companyDTOList);
        return companyDtoPageInfo;
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        SysCompany sysCompany = sysCompanyDao.findById(id);
        if (sysCompany == null) {
            return null;
        }
        return convertToDto(sysCompany);
    }

    @Override
    @Transactional
    public void updateCompanyStatus(Long companyId, UpdateCompanyStatusDTO updateCompanyStatusDTO) {
        SysCompany existingCompany = sysCompanyDao.findById(companyId);
        if (existingCompany == null) {
            throw new RuntimeException("企业不存在");
        }
        existingCompany.setStatus(updateCompanyStatusDTO.getStatus());
        sysCompanyDao.update(existingCompany);
    }

    @Override
    @Transactional
    public void deleteCompany(Long companyId) {
        if (sysCompanyDao.findById(companyId) == null) {
            throw new RuntimeException("企业不存在");
        }
        sysCompanyDao.deleteById(companyId);
    }

    private CompanyDTO convertToDto(SysCompany sysCompany) {
        CompanyDTO companyDTO = new CompanyDTO();
        BeanUtils.copyProperties(sysCompany, companyDTO);
        return companyDTO;
    }
}