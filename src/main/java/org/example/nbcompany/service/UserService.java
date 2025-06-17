package org.example.nbcompany.service;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.UserDto.AdminCreateUserDTO;
import org.example.nbcompany.dto.UserDto.AdminUpdateUserDTO;
import org.example.nbcompany.dto.CompanyDto.CompanyCreateMemberDTO;
import org.example.nbcompany.dto.CompanyDto.CompanyMemberDTO;
import org.example.nbcompany.dto.CompanyDto.CompanyUpdateMemberDTO;
import org.example.nbcompany.dto.CompanyDto.CompanyUpdateMemberRoleDTO;
import org.example.nbcompany.dto.UserDto.RegisterUserDTO;
import org.example.nbcompany.dto.UserDto.UpdatePasswordDTO;
import org.example.nbcompany.dto.UserDto.UpdateProfileDTO;
import org.example.nbcompany.dto.UserDto.UserDTO;
import org.example.nbcompany.entity.SysUser;

public interface UserService {

    // 用户注册
    UserDTO registerUser(RegisterUserDTO registerUserDTO);

    // 根据ID获取用户
    UserDTO getUserById(Long id);

    // 获取当前用户信息
    UserDTO getCurrentUser(Long userId);

    // 修改当前用户基本资料
    UserDTO updateCurrentUserProfile(Long userId, UpdateProfileDTO updateProfileDTO);

    // 修改当前用户密码
    void updateCurrentUserPassword(Long userId, UpdatePasswordDTO updatePasswordDTO);

    // 平台管理员获取用户列表
    PageInfo<UserDTO> getAdminUsers(Long companyId, Integer companyRole, Integer userType, String username, String phoneNumber, Integer status, int pageNum, int pageSize);

    // 平台管理员创建用户
    UserDTO createAdminUser(AdminCreateUserDTO adminCreateUserDTO);

    // 平台管理员修改用户信息
    UserDTO updateAdminUser(Long userId, AdminUpdateUserDTO adminUpdateUserDTO);

    // 企业管理员获取成员列表
    PageInfo<CompanyMemberDTO> getCompanyMembers(Long companyId, String username, Integer companyRole, Integer status, int pageNum, int pageSize);

    // 企业管理员创建成员
    UserDTO createCompanyMember(Long companyId, CompanyCreateMemberDTO createMemberDTO);

    // 企业管理员修改成员角色
    void updateCompanyMemberRole(Long companyId, Long memberId, CompanyUpdateMemberRoleDTO updateRoleDTO);

    // 企业管理员修改成员信息
    UserDTO updateCompanyMember(Long companyId, Long memberId, CompanyUpdateMemberDTO updateMemberDTO);

    // 企业管理员删除成员
    void deleteCompanyMember(Long companyId, Long memberId);

    // 工具方法：根据SysUser获取包含companyName的UserDTO
    UserDTO convertToUserDTOWithCompany(SysUser sysUser);
}