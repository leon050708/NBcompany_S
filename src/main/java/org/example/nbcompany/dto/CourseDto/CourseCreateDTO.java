package org.example.nbcompany.dto.CourseDto;

import lombok.Data;

/**
 * 课程创建DTO
 * 用于创建新课程时的数据传输
 * 包含创建课程所需的基本信息，不包含自动生成的字段如id、创建时间等
 */
@Data
public class CourseCreateDTO {
    
    /** 课程名称 - 必填 */
    private String courseName;
    
    /** 课程封面URL */
    private String coverImageUrl;
    
    /** 课程简介 */
    private String summary;
    
    /** 课程视频URL - 必填 */
    private String courseVideoUrl;
    
    /** 课程排序值 */
    private Integer sortOrder;
    
    /** 作者ID (发布者) - 必填 */
    private Long authorId;
    
    /** 作者名称 */
    private String authorName;
    
    /** 发布企业ID - 必填 */
    private Long companyId;
    
    /** 审核状态 (0:待审核, 1:已发布/审核通过, 2:审核未通过) */
    private Integer status = 0; // 默认待审核
} 