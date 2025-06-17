package org.example.nbcompany.dto.CompanyDto;

import lombok.Data;

@Data
public class CompanyCreateMemberDTO {
    private String username;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Integer companyRole; // 1:普通员工, 2:企业管理员
    private Integer status;
}