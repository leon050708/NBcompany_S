package org.example.nbcompany.dto.UserDto;

import lombok.Data;

@Data
public class AdminUpdateUserDTO extends UpdateUserDTO {
    private Integer userType;      // 可以修改用户为企业用户或平台超级管理员
    private Long companyId;   // 更改所属企业
    private Integer companyRole;   // 更改企业内部角色
    private String password;
    private Integer status;
}