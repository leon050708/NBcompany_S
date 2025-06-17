package org.example.nbcompany.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dao.SysCompanyDao;
import org.example.nbcompany.dao.SysUserDao;
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
import org.example.nbcompany.entity.SysCompany;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysCompanyDao sysCompanyDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 辅助方法：将 SysUser 转换为 UserDTO（包含 companyName）
    @Override
    public UserDTO convertToUserDTOWithCompany(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(sysUser, userDTO);

        if (sysUser.getCompanyId() != null) {
            SysCompany company = sysCompanyDao.findById(sysUser.getCompanyId());
            if (company != null) {
                userDTO.setCompanyId(company.getId()); // 添加 companyName 字段
            }
        }
        return userDTO;
    }

    // 辅助方法：将 SysUser 转换为 CompanyMemberDTO
    private CompanyMemberDTO convertToCompanyMemberDTO(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        CompanyMemberDTO memberDTO = new CompanyMemberDTO();
        BeanUtils.copyProperties(sysUser, memberDTO);
        return memberDTO;
    }

    @Override
    @Transactional
    public UserDTO registerUser(RegisterUserDTO registerUserDTO) {
        if (sysUserDao.findByUsername(registerUserDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(registerUserDTO, sysUser);

        // 密码加密
        sysUser.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        sysUser.setUserType(1); // 默认为企业用户
        sysUser.setCompanyRole(1); // 默认为普通员工
        sysUser.setStatus(1); // 默认正常状态

        // 检查 companyId 是否有效
        if (registerUserDTO.getCompanyId() != null) {
            SysCompany company = sysCompanyDao.findById(registerUserDTO.getCompanyId());
            if (company == null || company.getStatus() == 0) { // 企业不存在或未激活
                throw new RuntimeException("所属企业不存在或未通过审核");
            }
        } else {
            // 如果用户注册时没有选择企业，可以设定为 null 或抛出错误
            throw new RuntimeException("注册用户必须选择所属企业");
        }

        sysUserDao.insert(sysUser);
        return convertToUserDTOWithCompany(sysUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        SysUser sysUser = sysUserDao.findById(id);
        return convertToUserDTOWithCompany(sysUser);
    }

    @Override
    public UserDTO getCurrentUser(Long userId) {
        SysUser sysUser = sysUserDao.findById(userId);
        return convertToUserDTOWithCompany(sysUser);
    }

    @Override
    @Transactional
    public UserDTO updateCurrentUserProfile(Long userId, UpdateProfileDTO updateProfileDTO) {
        SysUser existingUser = sysUserDao.findById(userId);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        BeanUtils.copyProperties(updateProfileDTO, existingUser);
        sysUserDao.update(existingUser);
        return convertToUserDTOWithCompany(existingUser);
    }

    @Override
    @Transactional
    public void updateCurrentUserPassword(Long userId, UpdatePasswordDTO updatePasswordDTO) {
        SysUser existingUser = sysUserDao.findById(userId);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), existingUser.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }
        if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmNewPassword())) {
            throw new RuntimeException("两次输入的新密码不一致");
        }

        existingUser.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        sysUserDao.update(existingUser);
    }

    @Override
    public PageInfo<UserDTO> getAdminUsers(Long companyId, Integer companyRole, Integer userType, String username, String phoneNumber, Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> userList = sysUserDao.findUsersByCriteria(companyId, companyRole, userType, username, phoneNumber, status);
        PageInfo<SysUser> userPageInfo = new PageInfo<>(userList);

        List<UserDTO> userDtoList = userList.stream()
                .map(this::convertToUserDTOWithCompany)
                .collect(Collectors.toList());

        PageInfo<UserDTO> userDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(userPageInfo, userDtoPageInfo);
        userDtoPageInfo.setList(userDtoList);
        return userDtoPageInfo;
    }

    @Override
    @Transactional
    public UserDTO createAdminUser(AdminCreateUserDTO adminCreateUserDTO) {
        if (sysUserDao.findByUsername(adminCreateUserDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(adminCreateUserDTO, sysUser);
        sysUser.setPassword(passwordEncoder.encode(adminCreateUserDTO.getPassword()));

        if (adminCreateUserDTO.getUserType() == null) {
            throw new RuntimeException("用户类型不能为空");
        }
        if (adminCreateUserDTO.getUserType() == 1 && adminCreateUserDTO.getCompanyId() == null) {
            throw new RuntimeException("企业用户必须指定所属企业ID");
        }
        if (adminCreateUserDTO.getUserType() == 1 && adminCreateUserDTO.getCompanyId() != null) {
            SysCompany company = sysCompanyDao.findById(adminCreateUserDTO.getCompanyId());
            if (company == null) {
                throw new RuntimeException("指定的企业ID不存在");
            }
        }
        if (adminCreateUserDTO.getUserType() == 2) { // 平台管理员不属于任何公司
            sysUser.setCompanyId(null);
            sysUser.setCompanyRole(null);
        }

        sysUserDao.insert(sysUser);
        return convertToUserDTOWithCompany(sysUser);
    }

    @Override
    @Transactional
    public UserDTO updateAdminUser(Long userId, AdminUpdateUserDTO adminUpdateUserDTO) {
        SysUser existingUser = sysUserDao.findById(userId);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 可以修改密码
        if (adminUpdateUserDTO.getPassword() != null && !adminUpdateUserDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(adminUpdateUserDTO.getPassword()));
        }

        // 复制其他属性，确保不覆盖id和username
        String originalUsername = existingUser.getUsername(); // 保存原用户名
        Long originalId = existingUser.getId(); // 保存原ID
        BeanUtils.copyProperties(adminUpdateUserDTO, existingUser);
        existingUser.setUsername(originalUsername); // 恢复原用户名
        existingUser.setId(originalId); // 恢复原ID

        // 特殊处理userType和companyId, companyRole
        if (adminUpdateUserDTO.getUserType() != null) {
            if (adminUpdateUserDTO.getUserType() == 1) { // 设为企业用户
                if (adminUpdateUserDTO.getCompanyId() == null) {
                    throw new RuntimeException("企业用户必须指定所属企业ID");
                }
                SysCompany company = sysCompanyDao.findById(adminUpdateUserDTO.getCompanyId());
                if (company == null) {
                    throw new RuntimeException("指定的企业ID不存在");
                }
                existingUser.setCompanyId(adminUpdateUserDTO.getCompanyId());
                existingUser.setCompanyRole(adminUpdateUserDTO.getCompanyRole() != null ? adminUpdateUserDTO.getCompanyRole() : 1); // 默认为普通员工
            } else if (adminUpdateUserDTO.getUserType() == 2) { // 设为平台超级管理员
                existingUser.setCompanyId(null);
                existingUser.setCompanyRole(null);
            }
            existingUser.setUserType(adminUpdateUserDTO.getUserType());
        } else { // 如果userType没有在请求中明确指定，则保留原有userType，并根据其类型处理companyId/Role
            if (existingUser.getUserType() == 1) { // 如果是企业用户
                if (adminUpdateUserDTO.getCompanyId() != null) { // 如果提供了新的公司ID
                    SysCompany company = sysCompanyDao.findById(adminUpdateUserDTO.getCompanyId());
                    if (company == null) {
                        throw new RuntimeException("指定的企业ID不存在");
                    }
                    existingUser.setCompanyId(adminUpdateUserDTO.getCompanyId());
                }
                if (adminUpdateUserDTO.getCompanyRole() != null) {
                    existingUser.setCompanyRole(adminUpdateUserDTO.getCompanyRole());
                }
            }
        }
        sysUserDao.update(existingUser);
        return convertToUserDTOWithCompany(existingUser);
    }


    @Override
    public PageInfo<CompanyMemberDTO> getCompanyMembers(Long companyId, String username, Integer companyRole, Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> memberList = sysUserDao.findCompanyMembers(companyId, username, companyRole, status);
        PageInfo<SysUser> memberPageInfo = new PageInfo<>(memberList);

        List<CompanyMemberDTO> memberDtoList = memberList.stream()
                .map(this::convertToCompanyMemberDTO)
                .collect(Collectors.toList());

        PageInfo<CompanyMemberDTO> memberDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(memberPageInfo, memberDtoPageInfo);
        memberDtoPageInfo.setList(memberDtoList);
        return memberDtoPageInfo;
    }

    @Override
    @Transactional
    public UserDTO createCompanyMember(Long companyId, CompanyCreateMemberDTO createMemberDTO) {
        if (sysUserDao.findByUsername(createMemberDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        SysCompany company = sysCompanyDao.findById(companyId);
        if (company == null || company.getStatus() == 0) {
            throw new RuntimeException("指定企业不存在或未激活");
        }

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(createMemberDTO, sysUser);
        sysUser.setPassword(passwordEncoder.encode(createMemberDTO.getPassword()));
        sysUser.setCompanyId(companyId);
        sysUser.setUserType(1); // 默认创建企业用户

        // 默认值处理
        if (sysUser.getCompanyRole() == null) sysUser.setCompanyRole(1); // 普通员工
        if (sysUser.getStatus() == null) sysUser.setStatus(1); // 正常

        sysUserDao.insert(sysUser);
        return convertToUserDTOWithCompany(sysUser);
    }

    @Override
    @Transactional
    public void updateCompanyMemberRole(Long companyId, Long memberId, CompanyUpdateMemberRoleDTO updateRoleDTO) {
        SysUser member = sysUserDao.findById(memberId);
        if (member == null || !member.getCompanyId().equals(companyId)) {
            throw new RuntimeException("成员不存在或不属于该企业");
        }
        if (member.getUserType() == 2) { // 不能修改平台超级管理员
            throw new RuntimeException("无法修改平台超级管理员的角色");
        }
        // 检查是否是修改自己的角色
        // long currentUserId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSysUser().getId();
        // if (memberId.equals(currentUserId)) {
        //     throw new RuntimeException("不能修改自己的企业角色");
        // }

        member.setCompanyRole(updateRoleDTO.getCompanyRole());
        sysUserDao.update(member);
    }

    @Override
    @Transactional
    public UserDTO updateCompanyMember(Long companyId, Long memberId, CompanyUpdateMemberDTO updateMemberDTO) {
        SysUser member = sysUserDao.findById(memberId);
        if (member == null || !member.getCompanyId().equals(companyId)) {
            throw new RuntimeException("成员不存在或不属于该企业");
        }
        if (member.getUserType() == 2) { // 不能修改平台超级管理员
            throw new RuntimeException("无法修改平台超级管理员信息");
        }
        // 检查是否是修改自己
        // long currentUserId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSysUser().getId();
        // if (memberId.equals(currentUserId)) {
        //     throw new RuntimeException("不能通过此接口修改自己的信息");
        // }

        BeanUtils.copyProperties(updateMemberDTO, member);
        sysUserDao.update(member);
        return convertToUserDTOWithCompany(member);
    }

    @Override
    @Transactional
    public void deleteCompanyMember(Long companyId, Long memberId) {
        SysUser member = sysUserDao.findById(memberId);
        if (member == null || !member.getCompanyId().equals(companyId)) {
            throw new RuntimeException("成员不存在或不属于该企业");
        }
        if (member.getUserType() == 2) { // 不能删除平台超级管理员
            throw new RuntimeException("无法删除平台超级管理员");
        }
        // TODO: 考虑是否是企业内唯一的企业管理员，如果删除则会导致企业无管理员
        // 你可以通过查询该企业下的企业管理员数量来判断
        sysUserDao.deleteById(memberId);
    }
}