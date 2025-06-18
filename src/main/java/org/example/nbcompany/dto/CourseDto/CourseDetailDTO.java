package org.example.nbcompany.dto.CourseDto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 课程详情DTO
 * 用于返回给前端的完整课程信息
 * 包含课程的所有字段，用于课程详情页面展示
 */
@Data
public class CourseDetailDTO {
    
    /** 课程ID */
    private Long id;
    
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
    
    /** 作者ID (发布者) */
    private Long authorId;
    
    /** 作者名称 */
    private String authorName;
    
    /** 发布企业ID */
    private Long companyId;
    
    /** 企业名称 */
    private String companyName;
    
    /** 审核状态 (0:待审核, 1:已发布/审核通过, 2:审核未通过) */
    private Integer status;
    
    /** 状态描述 */
    private String statusDesc;
    
    /** 观看次数 */
    private Integer viewCount;
    
    /** 创建时间 */
    private LocalDateTime createdAt;
    
    /** 更新时间 */
    private LocalDateTime updatedAt;
    
    /**
     * 获取状态描述
     */
    public String getStatusDesc() {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待审核";
            case 1: return "已发布";
            case 2: return "审核未通过";
            default: return "未知";
        }
    }
} 