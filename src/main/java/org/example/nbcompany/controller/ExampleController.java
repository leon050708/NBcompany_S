package org.example.nbcompany.controller;

import org.example.nbcompany.dto.response.ApiResponse;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.util.UserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例Controller，展示如何使用UserContext工具类
 */
@RestController
@RequestMapping("/api/v1/example")
public class ExampleController {
    
    /**
     * 获取当前用户信息示例
     */
    @GetMapping("/current-user")
    public ApiResponse<Map<String, Object>> getCurrentUserInfo() {
        // 获取当前用户ID
        Long userId = UserContext.getCurrentUserId();
        
        // 获取当前用户完整信息
        SysUser currentUser = UserContext.getCurrentUser();
        
        // 检查用户权限
        boolean isPlatformAdmin = UserContext.isPlatformAdmin();
        boolean isCompanyAdmin = UserContext.isCompanyAdmin();
        Long companyId = UserContext.getCurrentUserCompanyId();
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("currentUser", currentUser);
        result.put("isPlatformAdmin", isPlatformAdmin);
        result.put("isCompanyAdmin", isCompanyAdmin);
        result.put("companyId", companyId);
        
        return ApiResponse.success(result);
    }
    
    /**
     * 权限检查示例
     */
    @GetMapping("/admin-only")
    public ApiResponse<String> adminOnlyEndpoint() {
        // 检查是否为平台管理员
        if (!UserContext.isPlatformAdmin()) {
            return ApiResponse.error(403, "权限不足，需要平台管理员权限");
        }
        
        return ApiResponse.success("欢迎，平台管理员！");
    }
    
    /**
     * 企业管理员权限检查示例
     */
    @GetMapping("/company-admin-only")
    public ApiResponse<String> companyAdminOnlyEndpoint() {
        // 检查是否为企业管理员
        if (!UserContext.isCompanyAdmin()) {
            return ApiResponse.error(403, "权限不足，需要企业管理员权限");
        }
        
        Long companyId = UserContext.getCurrentUserCompanyId();
        return ApiResponse.success("欢迎，企业管理员！您的企业ID是：" + companyId);
    }
    
    /**
     * 根据用户类型执行不同逻辑示例
     */
    @GetMapping("/user-type-action")
    public ApiResponse<String> userTypeAction() {
        SysUser currentUser = UserContext.getCurrentUser();
        
        if (currentUser == null) {
            return ApiResponse.error(401, "用户未登录");
        }
        
        switch (currentUser.getUserType()) {
            case 1: // 企业用户
                if (currentUser.getCompanyRole() == 2) {
                    return ApiResponse.success("执行企业管理员操作");
                } else {
                    return ApiResponse.success("执行普通企业用户操作");
                }
            case 2: // 平台超级管理员
                return ApiResponse.success("执行平台管理员操作");
            default:
                return ApiResponse.error(400, "未知用户类型");
        }
    }
} 