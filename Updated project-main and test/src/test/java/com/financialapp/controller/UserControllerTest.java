package com.financialapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financialapp.dto.UserDTO;
import com.financialapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService; // from TestConfig

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        UserDTO request = new UserDTO(null, "test@example.com", false, 100);
        UserDTO response = new UserDTO(1, "test@example.com", false, 100);

        Mockito.when(userService.createUser(any(UserDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UserDTO response = new UserDTO(1, "get@example.com", false, 200);

        Mockito.when(userService.getUserById(1)).thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("get@example.com"));
    }

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        List<UserDTO> users = List.of(
                new UserDTO(1, "a@example.com", false, 100),
                new UserDTO(2, "b@example.com", true, 200)
        );

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value("a@example.com"))
                .andExpect(jsonPath("$[1].email").value("b@example.com"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UserDTO request = new UserDTO(null, "update@example.com", true, 300);
        UserDTO response = new UserDTO(1, "update@example.com", true, 300);

        Mockito.when(userService.updateUser(eq(1), any(UserDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("update@example.com"))
                .andExpect(jsonPath("$.points").value(300));
    }

    @Test
    void deleteUser_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(userService).deleteUser(1);
    }

    @Test
    void updatePoints_ShouldReturnUserWithUpdatedPoints() throws Exception {
        UserDTO response = new UserDTO(1, "points@example.com", false, 500);

        Mockito.when(userService.updatePoints(1, 500)).thenReturn(response);

        mockMvc.perform(patch("/api/users/1/points")
                        .param("points", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(500));
    }
}
