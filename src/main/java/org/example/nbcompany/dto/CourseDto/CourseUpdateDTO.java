package org.example.nbcompany.dto.CourseDto;

import lombok.Data;

/**
 * 课程更新DTO
 * 用于更新课程信息时的数据传输
 * 包含可更新的课程字段，不包含id、创建时间等不可修改的字段
 */
@Data
public class CourseUpdateDTO {
    
    /** 课程名称 */
    private String courseName;
    
    /** 课程封面URL */
    private String coverImageUrl;
    
    /** 课程简介 */
    private String summary;
    
    /** 课程视频URL */
    private String courseVideoUrl;
    
    /** 课程排序值 */
    private Integer sortOrder;
    
    /** 作者名称 */
    private String authorName;
    
    /** 审核状态 (0:待审核, 1:已发布/审核通过, 2:审核未通过) */
    private Integer status;
} 