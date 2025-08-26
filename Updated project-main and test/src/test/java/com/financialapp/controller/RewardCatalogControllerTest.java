package com.financialapp.controller;

import com.financialapp.dto.RewardCatalogDTO;
import com.financialapp.service.RewardCatalogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardCatalogController.class)
class RewardCatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardCatalogService catalogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateRewardCatalog() throws Exception {
        RewardCatalogDTO request = new RewardCatalogDTO(1, "Reward1", "GiftCard", 500, 101, "Config", true, 30);
        Mockito.when(catalogService.createRewardCatalog(any(RewardCatalogDTO.class))).thenReturn(request);

        mockMvc.perform(post("/api/reward-catalog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catalogItemId", is(1)))
                .andExpect(jsonPath("$.name", is("Reward1")))
                .andExpect(jsonPath("$.rewardType", is("GiftCard")))
                .andExpect(jsonPath("$.pointsRequired", is(500)))
                .andExpect(jsonPath("$.partnerId", is(101)))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.validityDuration", is(30)));
    }

    @Test
    void testGetById() throws Exception {
        RewardCatalogDTO response = new RewardCatalogDTO(2, "Reward2", "Voucher", 1000, 202, "Config2", false, 60);
        Mockito.when(catalogService.getRewardCatalogById(2)).thenReturn(response);

        mockMvc.perform(get("/api/reward-catalog/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catalogItemId", is(2)))
                .andExpect(jsonPath("$.name", is("Reward2")))
                .andExpect(jsonPath("$.rewardType", is("Voucher")))
                .andExpect(jsonPath("$.pointsRequired", is(1000)))
                .andExpect(jsonPath("$.partnerId", is(202)))
                .andExpect(jsonPath("$.active", is(false)))
                .andExpect(jsonPath("$.validityDuration", is(60)));
    }

    @Test
    void testGetAll() throws Exception {
        List<RewardCatalogDTO> rewardList = Arrays.asList(
                new RewardCatalogDTO(1, "Reward1", "GiftCard", 500, 101, "Config1", true, 30),
                new RewardCatalogDTO(2, "Reward2", "Voucher", 1000, 202, "Config2", false, 60)
        );

        Mockito.when(catalogService.getAllRewardCatalogs()).thenReturn(rewardList);

        mockMvc.perform(get("/api/reward-catalog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Reward1")))
                .andExpect(jsonPath("$[1].name", is("Reward2")));
    }

    @Test
    void testUpdateRewardCatalog() throws Exception {
        RewardCatalogDTO request = new RewardCatalogDTO(1, "Updated Reward", "GiftCard", 700, 303, "Updated Config", true, 45);
        Mockito.when(catalogService.updateRewardCatalog(eq(1), any(RewardCatalogDTO.class))).thenReturn(request);

        mockMvc.perform(put("/api/reward-catalog/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Reward")))
                .andExpect(jsonPath("$.pointsRequired", is(700)))
                .andExpect(jsonPath("$.partnerId", is(303)))
                .andExpect(jsonPath("$.validityDuration", is(45)));
    }

    @Test
    void testDeleteRewardCatalog() throws Exception {
        Mockito.doNothing().when(catalogService).deleteRewardCatalog(1);

        mockMvc.perform(delete("/api/reward-catalog/1"))
                .andExpect(status().isOk());
    }
}
