package org.example.nbcompany.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会议注册回执表 (biz_meeting_registration) 对应的实体类
 */
@Data
public class BizMeetingRegistration implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 注册ID */
    private Long id;

    /** 会议ID */
    private Long meetingId;

    /** 注册用户ID */
    private Long userId;

    /** 单位 */
    private String company;

    /** 姓名 */
    private String name;

    /** 性别 (0:未知, 1:男, 2:女) */
    private Integer gender;

    /** 手机号码 */
    private String phoneNumber;

    /** 电子邮箱 */
    private String email;

    /** 到达方式 */
    private String arrivalMethod;

    /** 到达车次/航班号 */
    private String arrivalTrainNo;

    /** 到达时间 */
    private LocalDateTime arrivalTime;

    /** 注册时间 */
    private LocalDateTime registeredAt;
}