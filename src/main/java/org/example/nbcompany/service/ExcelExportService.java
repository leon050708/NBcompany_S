package org.example.nbcompany.service;

import org.example.nbcompany.dto.CourseDto.CourseListItemDTO;

import java.util.List;

/**
 * Excel导出服务接口
 */
public interface ExcelExportService {
    
    /**
     * 导出课程列表到Excel
     * @param courseList 课程列表
     * @return Excel文件的字节数组
     */
    byte[] exportCourseListToExcel(List<CourseListItemDTO> courseList);
} 