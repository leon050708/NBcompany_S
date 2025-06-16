package org.example.nbcompany.service;

import org.example.nbcompany.dto.CreateUserDTO;
import org.example.nbcompany.dto.UpdateUserDTO;
import org.example.nbcompany.dto.UserDTO;
import com.github.pagehelper.PageInfo;

public interface UserService {

    UserDTO createUser(CreateUserDTO createUserDTO);

    UserDTO getUserById(Long id);

    PageInfo<UserDTO> getAllUsers(int pageNum, int pageSize);

    UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO);

    void deleteUser(Long id);
}