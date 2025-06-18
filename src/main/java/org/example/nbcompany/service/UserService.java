package org.example.nbcompany.service;

import org.example.nbcompany.dto.request.*;
import org.example.nbcompany.dto.response.*;
import org.example.nbcompany.entity.SysUser;

public interface UserService {
    void register(UserRegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserProfileResponse getProfile(Long userId);
    void updateProfile(Long userId, UpdateProfileRequest request);
    void updatePassword(Long userId, UpdatePasswordRequest request);
    PageResponse<SysUser> listUsers(Long companyId, Integer companyRole, Integer userType, 
                                  String username, String phoneNumber, Integer status, 
                                  int page, int size);
    void createUser(SysUser user);
    void updateUser(Long userId, SysUser user);
    PageResponse<SysUser> listCompanyMembers(Long companyId, String username, 
                                           Integer companyRole, Integer status, 
                                           int page, int size);
    void createCompanyMember(Long companyId, SysUser user);
    void updateCompanyMemberRole(Long companyId, Long memberId, Integer companyRole);
    void updateCompanyMember(Long companyId, Long memberId, SysUser user);
    void deleteCompanyMember(Long companyId, Long memberId);
} 