package org.example.nbcompany.dto.UserDto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token; // 如果使用JWT，这里返回token
    private UserInfoDTO userInfo;

    @Data
    public static class UserInfoDTO {
        private Long id;
        private String username;
        private String nickname;
        private Integer userType; // 1:企业用户, 2:平台超级管理员
        private Long companyId; // 仅企业用户有
        private String companyName; // 冗余，方便前端显示
        private Integer companyRole; // 仅企业用户有 (1:普通员工, 2:企业管理员)
    }
}