package org.example.nbcompany.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会议表 (biz_meeting) 对应的实体类
 */
@Data
public class BizMeeting implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 会议ID */
    private Long id;

    /** 会议名称 */
    private String meetingName;

    /** 会议开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /** 会议结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 会议封面URL */
    private String coverImageUrl;

    /** 会议内容（富文本） */
    private String content;

    /** 会议地点 */
    private String location;

    /** 主办单位 */
    private String organizer;

    /** 会议议程 */
    private String agenda;

    /** 嘉宾介绍 */
    private String speakers;

    /** 创建人ID */
    private Long creatorId;

    /** 创建人姓名 */
    private String creatorName;

    /** 创建企业ID */
    private Long companyId;

    /** 审核状态 (0:待审核, 1:已发布/审核通过, 2:审核未通过) */
    private Integer status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
