package org.example.nbcompany.controller;

import org.example.nbcompany.dto.request.CompanyStatusRequest;
import org.example.nbcompany.dto.request.UpdateMeetingStatusRequest;
import org.example.nbcompany.dto.response.*;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.CompanyService;
import org.example.nbcompany.service.MeetingService;
import org.example.nbcompany.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MeetingService meetingService;


    @GetMapping("/users")
    public ApiResponse<PageResponse<SysUser>> listUsers(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Integer companyRole,
            @RequestParam(required = false) Integer userType,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<SysUser> response = userService.listUsers(companyId, companyRole, userType,
                username, phoneNumber, status, page, size);
        return ApiResponse.success("获取成功", response);
    }

    @PostMapping("/users")
    public ApiResponse<Void> createUser(@RequestBody SysUser user) {
        userService.createUser(user);
        return ApiResponse.success("用户创建成功", null);
    }

    @PutMapping("/users/{userId}")
    public ApiResponse<Void> updateUser(@PathVariable Long userId, @RequestBody SysUser user) {
        userService.updateUser(userId, user);
        return ApiResponse.success("用户信息修改成功", null);
    }

    @PutMapping("/companies/{companyId}/status")
    public ApiResponse<Void> updateCompanyStatus(@PathVariable Long companyId,
                                               @RequestBody CompanyStatusRequest request) {
        companyService.updateCompanyStatus(companyId, request.getStatus());
        return ApiResponse.success("企业状态修改成功", null);
    }

    @PutMapping("/meetings/{meetingId}/status")
    public ApiResponse<Void> updateMeetingStatus(
            @PathVariable Long meetingId,
            @RequestBody UpdateMeetingStatusRequest request,
            @RequestAttribute Long userId
    ) {
        meetingService.updateMeetingStatus(meetingId, request.getStatus(), userId);
        return ApiResponse.success("会议审核成功", null);
    }
} 