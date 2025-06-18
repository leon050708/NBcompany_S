package org.example.nbcompany.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.nbcompany.dao.SysUserMapper;
import org.example.nbcompany.entity.SysUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.stereotype.Component;

/**
 * 用户上下文工具类，用于获取当前用户信息
 */
@Component
public class UserContext {
    
    /**
     * 获取当前用户ID
     * @return 用户ID，如果未登录则返回null
     */
    public static Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Object userId = request.getAttribute("userId");
            if (userId != null) {
                return (Long) userId;
            }
        }
        return null;
    }
    
    /**
     * 获取当前用户完整信息
     * @return 用户信息，如果未登录则返回null
     */
    public static SysUser getCurrentUser() {
        Long userId = getCurrentUserId();
        if (userId != null) {
            // 通过SpringContextHolder获取SysUserMapper
            SysUserMapper userMapper = SpringContextHolder.getBean(SysUserMapper.class);
            return userMapper.selectById(userId);
        }
        return null;
    }
    
    /**
     * 检查当前用户是否为平台管理员
     * @return true表示是平台管理员，false表示不是
     */
    public static boolean isPlatformAdmin() {
        SysUser currentUser = getCurrentUser();
        return currentUser != null && currentUser.getUserType() == 2;
    }
    
    /**
     * 检查当前用户是否为企业管理员
     * @return true表示是企业管理员，false表示不是
     */
    public static boolean isCompanyAdmin() {
        SysUser currentUser = getCurrentUser();
        return currentUser != null && currentUser.getCompanyRole() == 2;
    }
    
    /**
     * 检查当前用户是否为指定企业的管理员
     * @param companyId 企业ID
     * @return true表示是指定企业的管理员，false表示不是
     */
    public static boolean isCompanyAdmin(Long companyId) {
        SysUser currentUser = getCurrentUser();
        return currentUser != null && 
               currentUser.getCompanyId() != null && 
               currentUser.getCompanyId().equals(companyId) && 
               currentUser.getCompanyRole() == 2;
    }
    
    /**
     * 获取当前用户所属企业ID
     * @return 企业ID，如果未登录或不属于任何企业则返回null
     */
    public static Long getCurrentUserCompanyId() {
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getCompanyId() : null;
    }
} 