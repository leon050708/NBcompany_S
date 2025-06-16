package org.example.nbcompany.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Long companyId;
    private Integer companyRole;
    private Integer status;
}
