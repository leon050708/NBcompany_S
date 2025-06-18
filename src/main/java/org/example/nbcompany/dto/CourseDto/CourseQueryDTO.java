package org.example.nbcompany.dto.CourseDto;

import lombok.Data;

/**
 * 课程查询DTO
 * 用于课程查询和筛选时的数据传输
 * 包含查询条件，支持按多个维度筛选课程
 */
@Data
public class CourseQueryDTO {
    
    /** 课程名称 - 模糊查询 */
    private String courseName;
    
    /** 作者ID - 精确查询 */
    private Long authorId;
    
    /** 作者名称 - 模糊查询 */
    private String authorName;
    
    /** 企业ID - 精确查询 */
    private Long companyId;
    
    /** 审核状态 (0:待审核, 1:已发布/审核通过, 2:审核未通过) */
    private Integer status;
    
    /** 排序字段 (courseName, createdAt, viewCount, sortOrder) */
    private String sortBy = "createdAt";
    
    /** 排序方向 (asc, desc) */
    private String sortOrder = "desc";
    
    /** 页码 */
    private Integer pageNum = 1;
    
    /** 每页大小 */
    private Integer pageSize = 10;
} 