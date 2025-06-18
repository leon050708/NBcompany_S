package org.example.nbcompany.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dao.BizCourseDao;
import org.example.nbcompany.dao.SysCompanyMapper;
import org.example.nbcompany.dto.CourseDto.*;
import org.example.nbcompany.entity.BizCourse;
import org.example.nbcompany.entity.SysCompany;
import org.example.nbcompany.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 课程服务实现类
 */
@Service
public class CourseServiceImpl implements CourseService {
    
    @Autowired
    private BizCourseDao bizCourseDao;
    
    @Autowired
    private SysCompanyMapper sysCompanyMapper;

    @Override
    @Transactional
    public CourseDetailDTO createCourse(CourseCreateDTO courseCreateDTO) {
        BizCourse bizCourse = new BizCourse();
        BeanUtils.copyProperties(courseCreateDTO, bizCourse);
        
        // 设置默认值
        bizCourse.setViewCount(0);
        bizCourse.setCreatedAt(LocalDateTime.now());
        bizCourse.setUpdatedAt(LocalDateTime.now());
        
        bizCourseDao.insert(bizCourse);
        
        return convertToDetailDTO(bizCourse);
    }

    @Override
    public CourseDetailDTO getCourseById(Long id) {
        BizCourse bizCourse = bizCourseDao.findById(id);
        if (bizCourse == null) return null;
        return convertToDetailDTO(bizCourse);
    }

    @Override
    public PageInfo<CourseListItemDTO> getCourseList(CourseQueryDTO queryDTO) {
        // 设置分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        // TODO: 根据查询条件构建查询参数
        List<BizCourse> courseList = bizCourseDao.findAll();
        
        // 批量获取企业名称，提高性能
        List<Long> companyIds = courseList.stream()
                .map(BizCourse::getCompanyId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        
        Map<Long, String> companyNameMap = getCompanyNames(companyIds);
        
        List<CourseListItemDTO> dtoList = courseList.stream()
                .map(course -> {
                    CourseListItemDTO dto = new CourseListItemDTO();
                    BeanUtils.copyProperties(course, dto);
                    // 设置企业名称
                    if (course.getCompanyId() != null) {
                        dto.setCompanyName(companyNameMap.get(course.getCompanyId()));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
                
        return new PageInfo<>(dtoList);
    }

    @Override
    @Transactional
    public CourseDetailDTO updateCourse(Long id, CourseUpdateDTO courseUpdateDTO) {
        BizCourse existing = bizCourseDao.findById(id);
        if (existing == null) return null;

        // 只更新非null字段
        if (courseUpdateDTO.getCourseName() != null) {
            existing.setCourseName(courseUpdateDTO.getCourseName());
        }
        if (courseUpdateDTO.getCoverImageUrl() != null) {
            existing.setCoverImageUrl(courseUpdateDTO.getCoverImageUrl());
        }
        if (courseUpdateDTO.getSummary() != null) {
            existing.setSummary(courseUpdateDTO.getSummary());
        }
        if (courseUpdateDTO.getCourseVideoUrl() != null) {
            existing.setCourseVideoUrl(courseUpdateDTO.getCourseVideoUrl());
        }
        if (courseUpdateDTO.getSortOrder() != null) {
            existing.setSortOrder(courseUpdateDTO.getSortOrder());
        }
        if (courseUpdateDTO.getAuthorName() != null) {
            existing.setAuthorName(courseUpdateDTO.getAuthorName());
        }
        if (courseUpdateDTO.getStatus() != null) {
            existing.setStatus(courseUpdateDTO.getStatus());
        }
        
        existing.setUpdatedAt(LocalDateTime.now());
        bizCourseDao.update(existing);

        return convertToDetailDTO(existing);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        bizCourseDao.deleteById(id);
    }
    
    @Override
    @Transactional
    public CourseDetailDTO auditCourse(CourseAuditDTO courseAuditDTO) {
        if (!courseAuditDTO.isValidStatus()) {
            throw new IllegalArgumentException("无效的审核状态");
        }
        
        BizCourse existing = bizCourseDao.findById(courseAuditDTO.getId());
        if (existing == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        
        existing.setStatus(courseAuditDTO.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        bizCourseDao.update(existing);
        
        return convertToDetailDTO(existing);
    }

    /**
     * 转换为课程详情DTO
     */
    private CourseDetailDTO convertToDetailDTO(BizCourse bizCourse) {
        CourseDetailDTO detailDTO = new CourseDetailDTO();
        BeanUtils.copyProperties(bizCourse, detailDTO);
        
        // 获取企业名称
        if (bizCourse.getCompanyId() != null) {
            SysCompany company = sysCompanyMapper.selectById(bizCourse.getCompanyId());
            if (company != null) {
                detailDTO.setCompanyName(company.getCompanyName());
            }
        }
        
        return detailDTO;
    }
    
    /**
     * 转换为课程列表项DTO
     */
    private CourseListItemDTO convertToListItemDTO(BizCourse bizCourse) {
        CourseListItemDTO listItemDTO = new CourseListItemDTO();
        BeanUtils.copyProperties(bizCourse, listItemDTO);
        
        // 获取企业名称
        if (bizCourse.getCompanyId() != null) {
            SysCompany company = sysCompanyMapper.selectById(bizCourse.getCompanyId());
            if (company != null) {
                listItemDTO.setCompanyName(company.getCompanyName());
            }
        }
        
        return listItemDTO;
    }
    
    /**
     * 批量获取企业名称（优化性能）
     */
    private Map<Long, String> getCompanyNames(List<Long> companyIds) {
        if (companyIds == null || companyIds.isEmpty()) {
            return Map.of();
        }
        
        return companyIds.stream()
                .distinct()
                .collect(Collectors.toMap(
                    id -> id,
                    id -> {
                        SysCompany company = sysCompanyMapper.selectById(id);
                        return company != null ? company.getCompanyName() : null;
                    }
                ));
    }
}