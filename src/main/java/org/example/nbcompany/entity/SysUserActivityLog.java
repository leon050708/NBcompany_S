package org.example.nbcompany.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户活动日志表 (sys_user_activity_log) 对应的实体类
 */
@Data
public class SysUserActivityLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 操作类型 */
    private String actionType;

    /** 目标类型 */
    private String targetType;

    /** 目标ID */
    private Long targetId;

    /** IP地址 */
    private String ipAddress;

    /** 设备信息 */
    private String deviceInfo;

    /** 操作时间 */
    private LocalDateTime createdAt;
}