package org.example.nbcompany.dto.UserDto;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String username;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Long companyId; // 用户选择的所属企业ID
    private String verifyCode; // 如果有验证码，可以添加
}