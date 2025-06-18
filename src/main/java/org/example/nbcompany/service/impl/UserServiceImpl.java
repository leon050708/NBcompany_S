package org.example.nbcompany.service.impl;

import org.example.nbcompany.dao.SysUserMapper;
import org.example.nbcompany.dto.request.*;
import org.example.nbcompany.dto.response.*;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(UserRegisterRequest request) {
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());
        user.setUserType(1); // 企业用户
        user.setCompanyId(request.getCompanyId());
        user.setCompanyRole(1); // 普通员工
        user.setStatus(1); // 正常状态
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = userMapper.selectByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        // TODO: 生成JWT token

        LoginResponse response = new LoginResponse();
        response.setToken("TODO: JWT token");
        
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setUserType(user.getUserType());
        userInfo.setCompanyId(user.getCompanyId());
        userInfo.setCompanyRole(user.getCompanyRole());
        response.setUserInfo(userInfo);

        return response;
    }

    @Override
    public UserProfileResponse getProfile(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        response.setGender(user.getGender());
        response.setUserType(user.getUserType());
        response.setCompanyId(user.getCompanyId());
        response.setCompanyRole(user.getCompanyRole());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest request) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setNickname(request.getNickname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("两次输入的新密码不一致");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.updateById(user);
    }

    @Override
    public PageResponse<SysUser> listUsers(Long companyId, Integer companyRole, Integer userType,
                                         String username, String phoneNumber, Integer status,
                                         int page, int size) {
        PageResponse<SysUser> response = new PageResponse<>();
        response.setCurrent(page);
        response.setRecords(userMapper.selectByConditions(companyId, companyRole, userType,
                username, phoneNumber, status, (page - 1) * size, size));
        response.setTotal(userMapper.countByConditions(companyId, companyRole, userType,
                username, phoneNumber, status));
        response.setPages((int) Math.ceil((double) response.getTotal() / size));
        return response;
    }

    @Override
    @Transactional
    public void createUser(SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    @Transactional
    public void updateUser(Long userId, SysUser user) {
        SysUser existingUser = userMapper.selectById(userId);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setId(userId);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    public PageResponse<SysUser> listCompanyMembers(Long companyId, String username,
                                                  Integer companyRole, Integer status,
                                                  int page, int size) {
        PageResponse<SysUser> response = new PageResponse<>();
        response.setCurrent(page);
        response.setRecords(userMapper.selectByCompanyId(companyId, username, companyRole,
                status, (page - 1) * size, size));
        response.setTotal(userMapper.countByCompanyId(companyId, username, companyRole, status));
        response.setPages((int) Math.ceil((double) response.getTotal() / size));
        return response;
    }

    @Override
    @Transactional
    public void createCompanyMember(Long companyId, SysUser user) {
        user.setCompanyId(companyId);
        user.setUserType(1); // 企业用户
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    @Transactional
    public void updateCompanyMemberRole(Long companyId, Long memberId, Integer companyRole) {
        SysUser user = userMapper.selectById(memberId);
        if (user == null || !user.getCompanyId().equals(companyId)) {
            throw new RuntimeException("成员不存在");
        }

        user.setCompanyRole(companyRole);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void updateCompanyMember(Long companyId, Long memberId, SysUser user) {
        SysUser existingUser = userMapper.selectById(memberId);
        if (existingUser == null || !existingUser.getCompanyId().equals(companyId)) {
            throw new RuntimeException("成员不存在");
        }

        user.setId(memberId);
        user.setCompanyId(companyId);
        user.setUserType(1); // 企业用户
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void deleteCompanyMember(Long companyId, Long memberId) {
        SysUser user = userMapper.selectById(memberId);
        if (user == null || !user.getCompanyId().equals(companyId)) {
            throw new RuntimeException("成员不存在");
        }

        // 检查是否是最后一个企业管理员
        if (user.getCompanyRole() == 2) {
            long adminCount = userMapper.countByCompanyId(companyId, null, 2, 1);
            if (adminCount <= 1) {
                throw new RuntimeException("不能删除最后一个企业管理员");
            }
        }

        userMapper.deleteById(memberId);
    }
} 