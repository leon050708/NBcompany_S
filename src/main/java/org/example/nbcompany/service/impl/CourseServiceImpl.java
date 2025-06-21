package org.example.nbcompany.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dao.BizCourseDao;
import org.example.nbcompany.dao.SysCompanyMapper;
import org.example.nbcompany.dao.SysUserMapper;
import org.example.nbcompany.dto.CourseDto.*;
import org.example.nbcompany.entity.BizCourse;
import org.example.nbcompany.entity.SysCompany;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.CourseService;
import org.example.nbcompany.service.ExcelExportService;
import org.example.nbcompany.util.UserContext;
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
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private ExcelExportService excelExportService;

    @Override
    @Transactional
    public CourseDetailDTO createCourse(CourseCreateDTO courseCreateDTO) {
        // 从Token中获取当前用户信息
        SysUser currentUser = getCurrentUserOrThrow();
        
        // 验证用户是否有权限创建课程
        if (currentUser.getCompanyId() == null) {
            throw new IllegalArgumentException("用户不属于任何企业，无法创建课程");
        }
        
        BizCourse bizCourse = new BizCourse();
        BeanUtils.copyProperties(courseCreateDTO, bizCourse);
        
        // 使用Token中的用户信息，而不是前端传递的信息
        bizCourse.setAuthorId(currentUser.getId());
        bizCourse.setAuthorName(currentUser.getNickname());
        bizCourse.setCompanyId(currentUser.getCompanyId());
        
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
        
        // 获取当前用户信息
        SysUser currentUser = getCurrentUserOrThrow();
        
        // 权限验证：不同状态的课程有不同的访问权限
        if (!canAccessCourse(bizCourse, currentUser)) {
            throw new IllegalArgumentException("无权限访问此课程");
        }
        
        return convertToDetailDTO(bizCourse);
    }

    @Override
    public PageInfo<CourseListItemDTO> getCourseList(CourseQueryDTO queryDTO) {
        // 获取当前用户信息
        SysUser currentUser = getCurrentUserOrThrow();
        
        // 设置分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        // 根据用户权限过滤课程状态
        if (!isAdminUser(currentUser)) {
            // 普通用户只能看到已发布的课程
            if (queryDTO.getStatus() == null) {
                queryDTO.setStatus(1); // 默认只查询已发布的课程
            } else if (queryDTO.getStatus() != 1) {
                // 如果用户指定了非已发布状态，且不是管理员，则不允许查询
                throw new IllegalArgumentException("无权限查询此状态的课程");
            }
        }
        
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
                    // 设置状态描述
                    dto.setStatusDesc(getStatusDesc(course.getStatus()));
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

        // 权限验证
        if (!hasCoursePermission(existing)) {
            throw new IllegalArgumentException("无权限更新此课程");
        }

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
        BizCourse existing = bizCourseDao.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        
        // 权限验证
        if (!hasCoursePermission(existing)) {
            throw new IllegalArgumentException("无权限删除此课程");
        }
        
        bizCourseDao.deleteById(id);
    }
    
    @Override
    @Transactional
    public CourseDetailDTO auditCourse(CourseAuditDTO courseAuditDTO) {
        if (!courseAuditDTO.isValidStatus()) {
            throw new IllegalArgumentException("无效的审核状态");
        }
        
        // 权限验证：只有平台管理员可以审核课程
        if (!UserContext.isPlatformAdmin()) {
            throw new IllegalArgumentException("无权限审核课程");
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
        
        // 设置状态描述
        detailDTO.setStatusDesc(getStatusDesc(bizCourse.getStatus()));
        
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
        
        // 设置状态描述
        listItemDTO.setStatusDesc(getStatusDesc(bizCourse.getStatus()));
        
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

    /**
     * 检查用户是否有权限操作课程
     * @param course 课程信息
     * @return true表示有权限，false表示无权限
     */
    private boolean hasCoursePermission(BizCourse course) {
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }
        
        SysUser currentUser = sysUserMapper.selectById(currentUserId);
        if (currentUser == null) {
            return false;
        }
        
        // 1. 课程发布者可以操作
        if (currentUserId.equals(course.getAuthorId())) {
            return true;
        }
        // 2. 企业管理员可以操作本企业的课程
        if (currentUser.getCompanyRole() == 2 && 
            currentUser.getCompanyId() != null && 
            currentUser.getCompanyId().equals(course.getCompanyId())) {
            return true;
        }
        // 3. 平台超级管理员可以操作任何课程
        if (currentUser.getUserType() == 2) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 获取当前用户信息，如果未登录则抛出异常
     * @return 当前用户信息
     */
    private SysUser getCurrentUserOrThrow() {
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null) {
            throw new IllegalArgumentException("用户未登录");
        }
        
        SysUser currentUser = sysUserMapper.selectById(currentUserId);
        if (currentUser == null) {
            throw new IllegalArgumentException("用户信息不存在");
        }
        
        return currentUser;
    }

    /**
     * 检查用户是否可以访问课程
     * @param course 课程信息
     * @param currentUser 当前用户
     * @return true表示可以访问，false表示无权限
     */
    private boolean canAccessCourse(BizCourse course, SysUser currentUser) {
        // 1. 课程发布者可以访问自己的所有课程（任何状态）
        if (currentUser.getId().equals(course.getAuthorId())) {
            return true;
        }
        
        // 2. 企业管理员可以访问本企业的所有课程（任何状态）
        if (currentUser.getCompanyRole() == 2 && 
            currentUser.getCompanyId() != null && 
            currentUser.getCompanyId().equals(course.getCompanyId())) {
            return true;
        }
        
        // 3. 平台管理员可以访问所有课程（任何状态）
        if (currentUser.getUserType() == 2) {
            return true;
        }
        
        // 4. 其他用户只能访问已发布的课程
        return course.getStatus() == 1;
    }

    /**
     * 检查是否为管理员用户
     * @param user 用户信息
     * @return true表示是管理员，false表示不是
     */
    private boolean isAdminUser(SysUser user) {
        // 企业管理员或平台管理员
        return user.getCompanyRole() == 2 || user.getUserType() == 2;
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待审核";
            case 1: return "已发布";
            case 2: return "审核未通过";
            default: return "未知";
        }
    }
    
    @Override
    public byte[] exportCourseListToExcel(CourseQueryDTO queryDTO) {
        // 获取当前用户信息
        SysUser currentUser = getCurrentUserOrThrow();
        
        // 根据用户权限过滤课程
        List<BizCourse> courseList;
        
        if (isAdminUser(currentUser)) {
            // 管理员可以导出所有课程
            courseList = bizCourseDao.findAll();
        } else {
            // 普通用户只能导出已发布的课程和自己上传的课程
            List<BizCourse> allCourses = bizCourseDao.findAll();
            courseList = allCourses.stream()
                    .filter(course -> {
                        // 已发布的课程
                        if (course.getStatus() == 1) {
                            return true;
                        }
                        // 自己上传的课程（任何状态）
                        if (currentUser.getId().equals(course.getAuthorId())) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            
            // 如果用户指定了特定状态，需要进一步过滤
            if (queryDTO.getStatus() != null) {
                if (queryDTO.getStatus() == 1) {
                    // 导出已发布课程，不需要额外过滤
                } else {
                    // 导出非已发布状态，只能导出自己的课程
                    courseList = courseList.stream()
                            .filter(course -> currentUser.getId().equals(course.getAuthorId()))
                            .collect(Collectors.toList());
                }
            }
        }
        
        // 批量获取企业名称，提高性能
        List<Long> companyIds = courseList.stream()
                .map(BizCourse::getCompanyId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        
        Map<Long, String> companyNameMap = getCompanyNames(companyIds);
        
        // 转换为DTO列表
        List<CourseListItemDTO> dtoList = courseList.stream()
                .map(course -> {
                    CourseListItemDTO dto = new CourseListItemDTO();
                    BeanUtils.copyProperties(course, dto);
                    // 设置企业名称
                    if (course.getCompanyId() != null) {
                        dto.setCompanyName(companyNameMap.get(course.getCompanyId()));
                    }
                    // 设置状态描述
                    dto.setStatusDesc(getStatusDesc(course.getStatus()));
                    return dto;
                })
                .collect(Collectors.toList());
        
        // 导出到Excel
        return excelExportService.exportCourseListToExcel(dtoList);
    }
}