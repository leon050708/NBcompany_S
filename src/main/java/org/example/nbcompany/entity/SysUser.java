package org.example.nbcompany.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表 (sys_user) 对应的实体类
 */
@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;

    /** 用户名/账号 */
    private String username;

    /** 密码（哈希存储） */
    private String password;

    /** 用户昵称 */
    private String nickname;

    /** 手机号码 */
    private String phoneNumber;

    /** 电子邮箱 */
    private String email;

    /** 性别 (0:未知, 1:男, 2:女) */
    private Integer gender;

    /** 平台用户类型 (1:企业用户, 2:平台超级管理员) */
    private Integer userType;

    /** 所属企业ID */
    private Long companyId;

    /** 企业内部角色 (1:普通员工, 2:企业管理员) */
    private Integer companyRole;

    /** 用户状态 (0:禁用, 1:正常) */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}