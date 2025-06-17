package org.example.nbcompany.security;

import org.example.nbcompany.dao.SysUserDao;
import org.example.nbcompany.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

// 注意：这个类不直接实现 PermissionEvaluator 接口
// 而是提供用于 @PreAuthorize 表达式的方法
@Component("permissionEvaluator")
public class PermissionEvaluator {

    @Autowired
    private SysUserDao sysUserDao;

    // 检查当前认证用户是否是指定 companyId 的企业管理员
    public boolean isCompanyAdminForCompany(Long companyId, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return false;
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        SysUser currentUser = userDetails.getSysUser();

        // 确保是企业用户，并且是企业管理员，且 companyId 匹配
        return currentUser.getUserType() == 1 && // 企业用户
                currentUser.getCompanyRole() == 2 && // 企业管理员
                currentUser.getCompanyId() != null &&
                currentUser.getCompanyId().equals(companyId);
    }

    // 检查企业管理员是否可以修改或删除某个成员
    public boolean canEditCompanyMember(Long companyId, Long memberId, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return false;
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        SysUser currentUser = userDetails.getSysUser();

        // 1. 确保当前用户是该企业的管理员
        if (!isCompanyAdminForCompany(companyId, authentication)) {
            return false;
        }

        // 2. 获取目标成员信息
        SysUser targetMember = sysUserDao.findById(memberId);
        if (targetMember == null) {
            return false; // 目标成员不存在
        }

        // 3. 目标成员必须属于当前管理员的企业
        if (!targetMember.getCompanyId().equals(companyId)) {
            return false;
        }

        // 4. 不能修改或删除平台超级管理员
        if (targetMember.getUserType() == 2) {
            return false;
        }

        // 5. 不能修改或删除自己 (企业管理员不能通过此接口管理自己)
        if (targetMember.getId().equals(currentUser.getId())) {
            return false;
        }

        // 6. 如果是删除操作，且目标成员是唯一的企业管理员，则禁止删除
        // TODO: 这里需要更复杂的逻辑，例如查询该企业下还有多少个company_role=2的用户
        // 为简化示例，暂时不在此处实现唯一管理员的校验，这通常在Service层做业务校验更合适

        return true;
    }
}