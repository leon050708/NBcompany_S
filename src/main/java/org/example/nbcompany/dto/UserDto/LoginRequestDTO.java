package org.example.nbcompany.dto.UserDto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}