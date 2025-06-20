package org.example.nbcompany.controller;

import org.example.nbcompany.dto.request.CompanyMemberRoleRequest;
import org.example.nbcompany.dto.response.*;
import org.example.nbcompany.entity.SysCompany;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.CompanyService;
import org.example.nbcompany.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    // 公开接口：获取企业列表
    @GetMapping("/companies")
    public ApiResponse<PageResponse<SysCompany>> listCompanies(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<SysCompany> response = companyService.listCompanies(keyword, page, size);
        return ApiResponse.success("获取成功", response);
    }

    // 企业成员管理接口
    @GetMapping("/company/{companyId}/members")
    public ApiResponse<PageResponse<SysUser>> listCompanyMembers(
            @PathVariable Long companyId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer companyRole,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<SysUser> response = userService.listCompanyMembers(companyId, username,
                companyRole, status, page, size);
        return ApiResponse.success("获取成功", response);
    }

    @PostMapping("/company/{companyId}/members")
    public ApiResponse<Void> createCompanyMember(@PathVariable Long companyId,
                                               @RequestBody SysUser user) {
        userService.createCompanyMember(companyId, user);
        return ApiResponse.success("成员创建成功", null);
    }

    @PutMapping("/company/{companyId}/members/{memberId}/role")
    public ApiResponse<Void> updateCompanyMemberRole(
            @PathVariable Long companyId,
            @PathVariable Long memberId,
            @RequestBody CompanyMemberRoleRequest request) {
        userService.updateCompanyMemberRole(companyId, memberId, request.getCompanyRole());
        return ApiResponse.success("成员权限修改成功", null);
    }

    @PutMapping("/company/{companyId}/members/{memberId}")
    public ApiResponse<Void> updateCompanyMember(
            @PathVariable Long companyId,
            @PathVariable Long memberId,
            @RequestBody SysUser user) {
        userService.updateCompanyMember(companyId, memberId, user);
        return ApiResponse.success("成员信息修改成功", null);
    }

    @DeleteMapping("/company/{companyId}/members/{memberId}")
    public ApiResponse<Void> deleteCompanyMember(
            @PathVariable Long companyId,
            @PathVariable Long memberId) {
        userService.deleteCompanyMember(companyId, memberId);
        return ApiResponse.success("成员删除成功", null);
    }
} 