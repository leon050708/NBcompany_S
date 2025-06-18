package org.example.nbcompany.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserInfo userInfo;

    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String nickname;
        private Integer userType;
        private Long companyId;
        private String companyName;
        private Integer companyRole;
    }
} 