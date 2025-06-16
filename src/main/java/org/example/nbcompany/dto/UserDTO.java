package org.example.nbcompany.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Integer userType;
    private Long companyId;
    private Integer companyRole;
    private Integer status;
    private LocalDateTime createdAt;
}