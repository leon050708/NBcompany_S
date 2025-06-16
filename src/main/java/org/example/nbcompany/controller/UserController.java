package org.example.nbcompany.controller;

import org.example.nbcompany.dto.CreateUserDTO;
import org.example.nbcompany.dto.UpdateUserDTO;
import org.example.nbcompany.dto.UserDTO;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.nbcompany.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 创建用户 (Create)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        UserDTO createdUser = userService.createUser(createUserDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // 根据ID获取用户 (Read)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return userDTO != null ? ResponseEntity.ok(userDTO) : ResponseEntity.notFound().build();
    }

    // 获取用户列表（分页）(Read)
    @GetMapping
    public ResponseEntity<PageInfo<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<UserDTO> userPageInfo = userService.getAllUsers(pageNum, pageSize);
        return ResponseEntity.ok(userPageInfo);
    }


    // 更新用户 (Update)
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id, updateUserDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除用户 (Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // 返回 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取当前登录的用户信息
     * @param userDetails Spring Security 会自动注入包含了当前用户信息的 Principal 对象
     * @return UserDTO
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 通过 @AuthenticationPrincipal 注解，可以直接获取到 CustomUserDetails 对象
        if (userDetails == null) {
            // 理论上，如果 Spring Security 配置正确，匿名用户无法访问此接口，所以这里是额外的保险
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 从 CustomUserDetails 中获取原始的 SysUser 对象
        SysUser currentUser = userDetails.getSysUser();

        // 将 SysUser 转换为 UserDTO 并返回
        UserDTO userDTO = new UserDTO();
        userDTO.setId(currentUser.getId());
        userDTO.setUsername(currentUser.getUsername());
        userDTO.setNickname(currentUser.getNickname());
        userDTO.setPhoneNumber(currentUser.getPhoneNumber());
        userDTO.setEmail(currentUser.getEmail());
        userDTO.setGender(currentUser.getGender());
        userDTO.setUserType(currentUser.getUserType());
        userDTO.setCompanyId(currentUser.getCompanyId());
        userDTO.setCompanyRole(currentUser.getCompanyRole());
        userDTO.setStatus(currentUser.getStatus());
        userDTO.setCreatedAt(currentUser.getCreatedAt());

        return ResponseEntity.ok(userDTO);
    }
}