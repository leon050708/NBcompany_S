package org.example.nbcompany.dto.UserDto;

import lombok.Data;

@Data
public class AdminCreateUserDTO {
    private String username;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Integer userType;      // 平台超级管理员只能创建企业用户或另一个超级管理员
    private Long companyId;   // 必须指定所属企业ID (如果创建企业用户)
    private Integer companyRole;   // 1:普通员工, 2:企业管理员 (如果创建企业用户)
    private Integer status;
}