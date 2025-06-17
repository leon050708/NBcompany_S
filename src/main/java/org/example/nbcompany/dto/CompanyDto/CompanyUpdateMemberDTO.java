package org.example.nbcompany.dto.CompanyDto;

import lombok.Data;

@Data
public class CompanyUpdateMemberDTO {
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Integer status; // 0:禁用, 1:正常
}