package org.example.nbcompany.dto.UserDto;

import lombok.Data;

@Data
public class UpdateProfileDTO {
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
}