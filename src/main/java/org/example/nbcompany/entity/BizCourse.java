package org.example.nbcompany.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程表 (biz_course) 对应的实体类
 */
@Data
public class BizCourse implements Serializable {
    private static final long serialVersionUID = 1L;

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

    /** 审核状态 (0:待审核, 1:已发布/审核通过, 2:审核未通过) */
    private Integer status;

    /** 观看次数 */
    private Integer viewCount;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
