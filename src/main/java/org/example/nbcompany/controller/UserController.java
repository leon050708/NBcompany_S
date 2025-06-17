package org.example.nbcompany.controller;

import org.example.nbcompany.dto.UserDto.UserDTO;
import org.example.nbcompany.service.UserService;
import org.example.nbcompany.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users") // 修改路径以符合新规范
public class UserController {

    @Autowired
    private UserService userService;

    // 根据ID获取用户 (Read) - 可能需要认证，或者根据业务决定是否公开
    // 例如，普通用户可以获取自己的信息，管理员可以获取所有用户信息
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // 示例：需要认证才能访问
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            return Result.fail(HttpStatus.NOT_FOUND.value(), "用户不存在");
        }
        return Result.success("获取成功", userDTO);
    }

    // 注意：
    // 原来的 createUser 移到 AuthController.registerUser
    // 原来的 getAllUsers 移到 CompanyController.getAdminUsers
    // 原来的 updateUser 移到 CompanyController.updateAdminUser 或 CompanyController.updateCompanyMember
    // 原来的 deleteUser 需要根据权限和角色进行拆分和管理
    // 原来的 getCurrentUser 移到 CompanyController.getCurrentUserProfile
}