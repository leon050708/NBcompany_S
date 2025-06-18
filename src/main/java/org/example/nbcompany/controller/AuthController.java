package org.example.nbcompany.controller;

import org.example.nbcompany.dto.request.*;
import org.example.nbcompany.dto.response.*;
import org.example.nbcompany.service.CompanyService;
import org.example.nbcompany.service.UserService;
import org.example.nbcompany.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @PostMapping("/register/company")
    public ApiResponse<CompanyRegisterResponse> registerCompany(@RequestBody CompanyRegisterRequest request) {
        CompanyRegisterResponse response = companyService.register(request);
        return ApiResponse.success("企业注册成功，请等待平台管理员审核", response);
    }

    @PostMapping("/register/user")
    public ApiResponse<Void> registerUser(@RequestBody UserRegisterRequest request) {
        userService.register(request);
        return ApiResponse.success("用户注册成功，请等待企业管理员分配权限", null);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        String token = JwtUtil.generateToken(response.getUserInfo().getId(), response.getUserInfo().getUsername());
        response.setToken(token);
        return ApiResponse.success("登录成功", response);
    }
} 