package com.financialapp.service.impl;


import com.financialapp.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    @Transactional
    void testCreateUser() {
        UserDTO dto = new UserDTO(null, "integration_create@example.com", false, 100);
        UserDTO saved = userService.createUser(dto);

        assertThat(saved.getUserId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("integration_create@example.com");
    }

    @Test
    void testGetUserById() {
        // assumes a user with ID 1 exists in DB
        UserDTO user = userService.getUserById(1);
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(1);
    }

    @Test
    void testGetAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        assertThat(users).isNotEmpty();
    }

    @Test
    @Transactional
    void testUpdateUser() {
        UserDTO dto = new UserDTO(null, "update_user@example.com", false, 50);
        UserDTO saved = userService.createUser(dto);

        UserDTO updated = userService.updateUser(saved.getUserId(),
                new UserDTO(saved.getUserId(), "updated_user@example.com", true, 200));

        assertThat(updated.getEmail()).isEqualTo("updated_user@example.com");
        assertThat(updated.getAppAdmin()).isTrue();
        assertThat(updated.getPoints()).isEqualTo(200);
    }

    @Test
    @Transactional
    void testUpdatePoints() {
        UserDTO dto = new UserDTO(null, "points_user@example.com", false, 100);
        UserDTO saved = userService.createUser(dto);

        UserDTO updated = userService.updatePoints(saved.getUserId(), 500);

        assertThat(updated.getPoints()).isEqualTo(500);
    }

    @Test
    @Transactional
    void testDeleteUser() {
        UserDTO dto = new UserDTO(null, "delete_user@example.com", false, 10);
        UserDTO saved = userService.createUser(dto);

        userService.deleteUser(saved.getUserId());

        // fetch should fail
        boolean exists = userService.getAllUsers()
                                    .stream()
                                    .anyMatch(u -> u.getUserId().equals(saved.getUserId()));
        assertThat(exists).isFalse();
    }
}
