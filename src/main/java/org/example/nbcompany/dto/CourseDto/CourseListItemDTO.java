package org.example.nbcompany.dto.CourseDto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 课程列表项DTO
 * 用于课程列表页面展示，包含较少但重要的信息
 * 优化列表页面的加载速度和用户体验
 */
@Data
public class CourseListItemDTO {
    
    /** 课程ID */
    private Long id;
    
    /** 课程名称 */
    private String courseName;
    
    /** 课程封面URL */
    private String coverImageUrl;
    
    /** 课程简介 - 截取前100个字符 */
    private String summary;
    
    /** 作者名称 */
    private String authorName;
    
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
    
    /**
     * 截取课程简介
     */
    public void setSummary(String summary) {
        if (summary != null && summary.length() > 100) {
            this.summary = summary.substring(0, 100) + "...";
        } else {
            this.summary = summary;
        }
    }
} 