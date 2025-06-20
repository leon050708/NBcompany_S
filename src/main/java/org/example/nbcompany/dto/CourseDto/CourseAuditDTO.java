package org.example.nbcompany.dto.CourseDto;

import lombok.Data;

/**
 * 课程审核DTO
 * 用于管理员审核课程时的数据传输
 * 只包含BizCourse实体类中存在的字段
 */
@Data
public class CourseAuditDTO {
    
    /** 课程ID - 必填 */
    private Long id;
    
    /** 审核状态 (1:审核通过, 2:审核未通过) - 必填 */
    private Integer status;
    
    /**
     * 验证审核状态是否有效
     */
    public boolean isValidStatus() {
        return status != null && (status == 1 || status == 2);
    }
    
    /**
     * 获取审核状态描述
     */
    public String getStatusDesc() {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "审核通过";
            case 2: return "审核未通过";
            default: return "未知";
        }
    }
} 