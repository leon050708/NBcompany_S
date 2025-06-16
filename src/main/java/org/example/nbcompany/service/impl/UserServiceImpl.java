package org.example.nbcompany.service.impl;

import org.example.nbcompany.dao.SysUserDao;
import org.example.nbcompany.dto.CreateUserDTO;
import org.example.nbcompany.dto.UpdateUserDTO;
import org.example.nbcompany.dto.UserDTO;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        // 1. 检查用户名是否已存在
        if (sysUserDao.findByUsername(createUserDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. DTO 转换为 Entity
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(createUserDTO, sysUser);

        // 3. 密码加密
        sysUser.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        // 4. 插入数据库
        sysUserDao.insert(sysUser);

        // 5. Entity 转换为 DTO 返回
        return convertToDto(sysUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        SysUser sysUser = sysUserDao.findById(id);
        if (sysUser == null) {
            return null; // 或抛出异常
        }
        return convertToDto(sysUser);
    }

    @Override
    public PageInfo<UserDTO> getAllUsers(int pageNum, int pageSize) {
        // 1. 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        // 2. 执行查询
        List<SysUser> userList = sysUserDao.findAll();
        // 3. 转换为 PageInfo 对象
        PageInfo<SysUser> userPageInfo = new PageInfo<>(userList);

        // 4. 将 List<SysUser> 转换为 List<UserDTO>
        List<UserDTO> userDtoList = userList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 5. 创建新的 PageInfo<UserDTO>
        PageInfo<UserDTO> userDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(userPageInfo, userDtoPageInfo); // 复制分页信息
        userDtoPageInfo.setList(userDtoList); // 设置转换后的列表

        return userDtoPageInfo;
    }


    @Override
    @Transactional
    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        // 1. 查找现有用户
        SysUser existingUser = sysUserDao.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 将 DTO 中的非空属性复制到 Entity 中
        BeanUtils.copyProperties(updateUserDTO, existingUser);

        // 3. 更新数据库
        sysUserDao.update(existingUser);

        return convertToDto(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        // 检查用户是否存在
        if (sysUserDao.findById(id) == null) {
            throw new RuntimeException("用户不存在");
        }
        sysUserDao.deleteById(id);
    }

    // 公用的转换方法
    private UserDTO convertToDto(SysUser sysUser) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(sysUser, userDTO);
        return userDTO;
    }
}
