package org.example.nbcompany.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 合作模块通用表 (biz_collaboration) 对应的实体类
 */
@Data
public class BizCollaboration implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 合作ID */
    private Long id;

    /** 合作类型 (1:会议研讨, 2:标准定制, 3:技术培训, 4:工具研发, 5:公益行动) */
    private Integer category;

    /** 模块名称/标题 */
    private String title;

    /** 模块简介 */
    private String summary;

    /** 详细内容（富文本） */
    private String content;

    /** 外部链接 */
    private String externalLink;

    /** 创建人ID */
    private Long creatorId;

    /** 创建企业ID */
    private Long companyId;

    /** 状态 (0:禁用, 1:正常) */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}