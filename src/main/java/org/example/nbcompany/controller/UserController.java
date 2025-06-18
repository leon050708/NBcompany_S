package org.example.nbcompany.controller;

import org.example.nbcompany.dto.request.*;
import org.example.nbcompany.dto.response.*;
import org.example.nbcompany.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ApiResponse<UserProfileResponse> getProfile(@RequestAttribute Long userId) {
        UserProfileResponse response = userService.getProfile(userId);
        return ApiResponse.success("获取成功", response);
    }

    @PutMapping("/profile")
    public ApiResponse<Void> updateProfile(@RequestAttribute Long userId,
                                         @RequestBody UpdateProfileRequest request) {
        userService.updateProfile(userId, request);
        return ApiResponse.success("修改成功", null);
    }

    @PutMapping("/password")
    public ApiResponse<Void> updatePassword(@RequestAttribute Long userId,
                                          @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request);
        return ApiResponse.success("密码修改成功", null);
    }
} 