package org.example.nbcompany.dto.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Long companyId;
    private String verifyCode;
} 