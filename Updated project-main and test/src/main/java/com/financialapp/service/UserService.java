package com.financialapp.service;

import com.financialapp.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Integer userId);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Integer userId, UserDTO userDTO);
    void deleteUser(Integer userId);
    UserDTO updatePoints(Integer userId, Integer points);
}
