package org.example.nbcompany.service;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.CourseDto.*;

/**
 * 课程服务接口
 * 提供课程相关的业务操作
 */
public interface CourseService {
    
    /**
     * 创建课程
     * @param courseCreateDTO 课程创建信息
     * @return 创建后的课程详情
     */
    CourseDetailDTO createCourse(CourseCreateDTO courseCreateDTO);

    /**
     * 根据ID获取课程详情
     * @param id 课程ID
     * @return 课程详情
     */
    CourseDetailDTO getCourseById(Long id);

    /**
     * 分页查询课程列表
     * @param queryDTO 查询条件
     * @return 课程列表分页信息
     */
    PageInfo<CourseListItemDTO> getCourseList(CourseQueryDTO queryDTO);

    /**
     * 更新课程信息
     * @param id 课程ID
     * @param courseUpdateDTO 课程更新信息
     * @return 更新后的课程详情
     */
    CourseDetailDTO updateCourse(Long id, CourseUpdateDTO courseUpdateDTO);

    /**
     * 删除课程
     * @param id 课程ID
     */
    void deleteCourse(Long id);
    
    /**
     * 审核课程
     * @param courseAuditDTO 课程审核信息
     * @return 审核后的课程详情
     */
    CourseDetailDTO auditCourse(CourseAuditDTO courseAuditDTO);

    /**
     * 导出课程列表到Excel
     * @param queryDTO 查询条件
     * @return Excel文件的字节数组
     */
    byte[] exportCourseListToExcel(CourseQueryDTO queryDTO);
}
