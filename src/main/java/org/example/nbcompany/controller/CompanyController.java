package org.example.nbcompany.controller;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.UserDto.AdminCreateUserDTO;
import org.example.nbcompany.dto.UserDto.AdminUpdateUserDTO;
import org.example.nbcompany.dto.CompanyDto.*;
import org.example.nbcompany.dto.UserDto.UpdateCompanyStatusDTO;
import org.example.nbcompany.dto.UserDto.UpdatePasswordDTO;
import org.example.nbcompany.dto.UserDto.UpdateProfileDTO;
import org.example.nbcompany.dto.UserDto.UserDTO;
import org.example.nbcompany.security.CustomUserDetails;
import org.example.nbcompany.service.CompanyService;
import org.example.nbcompany.service.UserService;
import org.example.nbcompany.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    // 1.2 新增：获取企业列表 (用于用户注册时选择)
    @GetMapping("/companies")
    public Result<PageInfo<CompanyDTO>> getAllCompanies(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageInfo<CompanyDTO> pageInfo = companyService.getAllCompanies(keyword, page, size);
        return Result.success("获取成功", pageInfo);
    }

    // 1.5 获取当前用户个人信息 (修改：包含企业ID、企业名称和企业角色)
    @GetMapping("/user/profile")
    @PreAuthorize("isAuthenticated()")
    public Result<UserDTO> getCurrentUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return Result.fail(HttpStatus.UNAUTHORIZED.value(), "用户未登录");
        }
        UserDTO userDTO = userService.getCurrentUser(userDetails.getSysUser().getId());
        return Result.success("获取成功", userDTO);
    }

    // 1.6 修改当前用户基本资料 (不变)
    @PutMapping("/user/profile")
    @PreAuthorize("isAuthenticated()")
    public Result<Void> updateCurrentUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody UpdateProfileDTO updateProfileDTO) {
        if (userDetails == null) {
            return Result.fail(HttpStatus.UNAUTHORIZED.value(), "用户未登录");
        }
        userService.updateCurrentUserProfile(userDetails.getSysUser().getId(), updateProfileDTO);
        return Result.success("修改成功");
    }

    // 1.7 修改当前用户密码 (不变)
    @PutMapping("/user/password")
    @PreAuthorize("isAuthenticated()")
    public Result<Void> updateCurrentUserPassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        if (userDetails == null) {
            return Result.fail(HttpStatus.UNAUTHORIZED.value(), "用户未登录");
        }
        userService.updateCurrentUserPassword(userDetails.getSysUser().getId(), updatePasswordDTO);
        return Result.success("密码修改成功");
    }

    // 1.8 获取用户列表 (平台超级管理员) (修改：增加 company_id 过滤)
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<PageInfo<UserDTO>> getAdminUsers(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Integer companyRole,
            @RequestParam(required = false) Integer userType,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageInfo<UserDTO> pageInfo = userService.getAdminUsers(companyId, companyRole, userType, username, phoneNumber, status, page, size);
        return Result.success("获取成功", pageInfo);
    }

    // 1.9 创建用户 (平台超级管理员) (修改：需指定所属企业和企业角色)
    @PostMapping("/admin/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<Void> createAdminUser(@RequestBody AdminCreateUserDTO adminCreateUserDTO) {
        userService.createAdminUser(adminCreateUserDTO);
        return Result.success("用户创建成功");
    }

    // 1.10 修改用户信息 (平台超级管理员) (修改：可以修改 user_type 和 company_role)
    @PutMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<Void> updateAdminUser(@PathVariable Long userId,
                                        @RequestBody AdminUpdateUserDTO adminUpdateUserDTO) {
        userService.updateAdminUser(userId, adminUpdateUserDTO);
        return Result.success("用户信息修改成功");
    }

    // 1.12 新增：审核企业状态 (平台超级管理员)
    @PutMapping("/admin/companies/{companyId}/status")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<Void> updateCompanyStatus(@PathVariable Long companyId,
                                            @RequestBody UpdateCompanyStatusDTO updateCompanyStatusDTO) {
        companyService.updateCompanyStatus(companyId, updateCompanyStatusDTO);
        return Result.success("企业状态修改成功");
    }

    // 1.11 新增：企业成员管理 (企业管理员)
    // 获取企业成员列表
    @GetMapping("/company/{companyId}/members")
    @PreAuthorize("hasRole('COMPANY_ADMIN') and @permissionEvaluator.isCompanyAdminForCompany(#companyId)")
    public Result<PageInfo<CompanyMemberDTO>> getCompanyMembers(
            @PathVariable Long companyId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer companyRole,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageInfo<CompanyMemberDTO> pageInfo = userService.getCompanyMembers(companyId, username, companyRole, status, page, size);
        return Result.success("获取成功", pageInfo);
    }

    // 创建企业成员
    @PostMapping("/company/{companyId}/members")
    @PreAuthorize("hasRole('COMPANY_ADMIN') and @permissionEvaluator.isCompanyAdminForCompany(#companyId)")
    public Result<Void> createCompanyMember(@PathVariable Long companyId,
                                            @RequestBody CompanyCreateMemberDTO createMemberDTO) {
        userService.createCompanyMember(companyId, createMemberDTO);
        return Result.success("成员创建成功");
    }

    // 修改企业成员角色
    @PutMapping("/company/{companyId}/members/{memberId}/role")
    @PreAuthorize("hasRole('COMPANY_ADMIN') and @permissionEvaluator.isCompanyAdminForCompany(#companyId) and @permissionEvaluator.canEditCompanyMember(#companyId, #memberId)")
    public Result<Void> updateCompanyMemberRole(@PathVariable Long companyId,
                                                @PathVariable Long memberId,
                                                @RequestBody CompanyUpdateMemberRoleDTO updateRoleDTO) {
        userService.updateCompanyMemberRole(companyId, memberId, updateRoleDTO);
        return Result.success("成员权限修改成功");
    }

    // 修改企业成员信息
    @PutMapping("/company/{companyId}/members/{memberId}")
    @PreAuthorize("hasRole('COMPANY_ADMIN') and @permissionEvaluator.isCompanyAdminForCompany(#companyId) and @permissionEvaluator.canEditCompanyMember(#companyId, #memberId)")
    public Result<Void> updateCompanyMember(@PathVariable Long companyId,
                                            @PathVariable Long memberId,
                                            @RequestBody CompanyUpdateMemberDTO updateMemberDTO) {
        userService.updateCompanyMember(companyId, memberId, updateMemberDTO);
        return Result.success("成员信息修改成功");
    }

    // 删除企业成员
    @DeleteMapping("/company/{companyId}/members/{memberId}")
    @PreAuthorize("hasRole('COMPANY_ADMIN') and @permissionEvaluator.isCompanyAdminForCompany(#companyId) and @permissionEvaluator.canEditCompanyMember(#companyId, #memberId)")
    public Result<Void> deleteCompanyMember(@PathVariable Long companyId,
                                            @PathVariable Long memberId) {
        userService.deleteCompanyMember(companyId, memberId);
        return Result.success("成员删除成功");
    }
}