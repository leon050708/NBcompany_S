package org.example.nbcompany.controller;

import org.example.nbcompany.dto.UserDto.LoginRequestDTO;
import org.example.nbcompany.dto.UserDto.LoginResponseDTO;
import org.example.nbcompany.dto.UserDto.RegisterCompanyDTO;
import org.example.nbcompany.dto.UserDto.RegisterUserDTO;
import org.example.nbcompany.dto.CompanyDto.CompanyDTO;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.security.CustomUserDetails;
import org.example.nbcompany.service.CompanyService;
import org.example.nbcompany.service.UserService;
import org.example.nbcompany.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AuthenticationManager authenticationManager; // 用于手动认证

    // 1.1 新增：企业注册
    @PostMapping("/register/company")
    public Result<CompanyDTO> registerCompany(@RequestBody RegisterCompanyDTO registerCompanyDTO) {
        CompanyDTO createdCompany = companyService.registerCompany(registerCompanyDTO);
        return Result.success("企业注册成功，请等待平台管理员审核", createdCompany);
    }

    // 1.3 用户注册 (修改：选择所属企业)
    @PostMapping("/register/user")
    public Result<Void> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        userService.registerUser(registerUserDTO);
        return Result.success("用户注册成功，请等待企业管理员分配权限");
    }

    // 1.4 用户登录
    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            // 使用 AuthenticationManager 进行认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
            );

            // 认证成功，将认证信息存入 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 获取 CustomUserDetails 对象
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            SysUser sysUser = userDetails.getSysUser();

            // 构建 LoginResponseDTO
            LoginResponseDTO response = new LoginResponseDTO();
            // TODO: 这里可以生成JWT Token并设置到response.setToken(jwtToken);
            response.setToken("your_jwt_token_here"); // 示例，实际需要生成JWT

            LoginResponseDTO.UserInfoDTO userInfo = new LoginResponseDTO.UserInfoDTO();
            userInfo.setId(sysUser.getId());
            userInfo.setUsername(sysUser.getUsername());
            userInfo.setNickname(sysUser.getNickname());
            userInfo.setUserType(sysUser.getUserType());
            userInfo.setCompanyId(sysUser.getCompanyId());
            userInfo.setCompanyRole(sysUser.getCompanyRole());

            if (sysUser.getCompanyId() != null) {
                CompanyDTO company = companyService.getCompanyById(sysUser.getCompanyId());
                if (company != null) {
                    userInfo.setCompanyName(company.getCompanyName());
                }
            }
            response.setUserInfo(userInfo);

            return Result.success("登录成功", response);

        } catch (Exception e) {
            // 认证失败
            return Result.fail(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误");
        }
    }
}