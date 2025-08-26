package com.financialapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financialapp.dto.RewardDTO;
import com.financialapp.service.RewardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/rewards should create a reward")
    void testCreateReward() throws Exception {
        RewardDTO request = new RewardDTO(null, 1, null, 2, 3, 100, LocalDateTime.of(2025, 1, 1, 10, 0));
        RewardDTO response = new RewardDTO(10L, 1, null, 2, 3, 100, request.getEarnedAt());

        Mockito.when(rewardService.createReward(any(RewardDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/rewards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rewardId", is(10)))
            .andExpect(jsonPath("$.userId", is(1)))
            .andExpect(jsonPath("$.activityId", is(2)))
            .andExpect(jsonPath("$.catalogItemId", is(3)))
            .andExpect(jsonPath("$.points", is(100)));

        verify(rewardService).createReward(any(RewardDTO.class));
    }

    @Test
    @DisplayName("GET /api/rewards/{id} should return reward by id")
    void testGetRewardById() throws Exception {
        RewardDTO dto = new RewardDTO(20L, 2, 5, null, 3, 200, LocalDateTime.of(2025, 2, 2, 12, 0));
        Mockito.when(rewardService.getRewardById(20L)).thenReturn(dto);

        mockMvc.perform(get("/api/rewards/{id}", 20L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rewardId", is(20)))
            .andExpect(jsonPath("$.userId", is(2)))
            .andExpect(jsonPath("$.goalId", is(5)))
            .andExpect(jsonPath("$.catalogItemId", is(3)))
            .andExpect(jsonPath("$.points", is(200)));

        verify(rewardService).getRewardById(20L);
    }

    @Test
    @DisplayName("GET /api/rewards should return all rewards")
    void testGetAllRewards() throws Exception {
        RewardDTO r1 = new RewardDTO(1L, 1, null, 2, 3, 100, null);
        RewardDTO r2 = new RewardDTO(2L, 2, 4, null, 5, 200, null);

        Mockito.when(rewardService.getAllRewards()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/rewards"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].rewardId", is(1)))
            .andExpect(jsonPath("$[1].rewardId", is(2)));

        verify(rewardService).getAllRewards();
    }

    @Test
    @DisplayName("PUT /api/rewards/{id} should update reward")
    void testUpdateReward() throws Exception {
        RewardDTO request = new RewardDTO(null, 3, null, 2, 3, 300, LocalDateTime.of(2025, 3, 3, 15, 0));
        RewardDTO response = new RewardDTO(30L, 3, null, 2, 3, 300, request.getEarnedAt());

        Mockito.when(rewardService.updateReward(eq(30L), any(RewardDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/rewards/{id}", 30L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rewardId", is(30)))
            .andExpect(jsonPath("$.userId", is(3)))
            .andExpect(jsonPath("$.points", is(300)));

        verify(rewardService).updateReward(eq(30L), any(RewardDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/rewards/{id} should delete reward")
    void testDeleteReward() throws Exception {
        mockMvc.perform(delete("/api/rewards/{id}", 40L))
            .andExpect(status().isOk());

        verify(rewardService).deleteReward(40L);
    }

    @Test
    @DisplayName("GET /api/rewards/user/{userId} should return rewards by user")
    void testGetRewardsByUserId() throws Exception {
        RewardDTO dto = new RewardDTO(50L, 9, null, 2, 3, 400, null);
        Mockito.when(rewardService.getRewardsByUserId(9)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/rewards/user/{userId}", 9))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].userId", is(9)))
            .andExpect(jsonPath("$[0].points", is(400)));

        verify(rewardService).getRewardsByUserId(9);
    }
}
