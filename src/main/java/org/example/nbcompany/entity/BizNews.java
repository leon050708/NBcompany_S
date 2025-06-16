package org.example.nbcompany.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 行业动态/资讯表 (biz_news) 对应的实体类
 */
@Data
public class BizNews implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 动态ID */
    private Long id;

    /** 动态标题 */
    private String title;

    /** 新闻图片URL */
    private String coverImageUrl;

    /** 新闻简介 */
    private String summary;

    /** 动态内容（富文本） */
    private String content;

    /** 作者ID (发布者) */
    private Long authorId;

    /** 作者名称 */
    private String authorName;

    /** 发布企业ID */
    private Long companyId;

    /** 审核状态 (0:待审核, 1:已发布/审核通过, 2:审核未通过) */
    private Integer status;

    /** 浏览次数 */
    private Integer viewCount;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}