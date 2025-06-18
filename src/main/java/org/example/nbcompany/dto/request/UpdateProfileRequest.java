package org.example.nbcompany.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
} 