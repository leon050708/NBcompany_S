package org.example.nbcompany.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业信息表 (sys_company) 对应的实体类
 */
@Data
public class SysCompany implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 企业ID */
    private Long id;

    /** 企业名称 */
    private String companyName;

    /** 企业联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 联系邮箱 */
    private String contactEmail;

    /** 企业状态 (0:禁用, 1:正常) */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}