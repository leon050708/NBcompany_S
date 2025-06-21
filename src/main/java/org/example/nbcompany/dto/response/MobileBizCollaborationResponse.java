package org.example.nbcompany.dto.response;

import lombok.Data;

@Data
public class MobileBizCollaborationResponse {
    private static final long serialVersionUID = 1L;

    /** 合作ID */
    private Long id;

    /** 合作类型 (1:会议研讨, 2:标准定制, 3:技术培训, 4:工具研发, 5:公益行动) */
    private Integer category;

    /** 模块名称/标题 */
    private String title;

    /** 模块简介 */
    private String summary;

    /** 外部链接 */
    private String externalLink;
}
