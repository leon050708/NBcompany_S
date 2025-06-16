package org.example.nbcompany.dto;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String username;
    private String password; // 创建时需要密码
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Integer userType;
    private Long companyId;
    private Integer companyRole;
    private Integer status;
}