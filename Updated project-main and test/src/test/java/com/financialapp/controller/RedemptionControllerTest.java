package com.financialapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financialapp.dto.RedemptionDTO;
import com.financialapp.entity.RedemptionStatus;
import com.financialapp.service.RedemptionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RedemptionController.class)
class RedemptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedemptionService redemptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateRedemption() throws Exception {
        RedemptionDTO request = new RedemptionDTO(
                1, 101, 2001, LocalDateTime.now(),
                RedemptionStatus.PENDING, "Shipping soon", null,
                LocalDateTime.now().plusDays(7), "CODE123"
        );

        Mockito.when(redemptionService.createRedemption(any(RedemptionDTO.class)))
                .thenReturn(request);

        mockMvc.perform(post("/api/redemptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redemptionId").value(1))
                .andExpect(jsonPath("$.userId").value(101))
                .andExpect(jsonPath("$.catalogItemId").value(2001))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.fulfillmentDetails").value("Shipping soon"))
                .andExpect(jsonPath("$.redemptionCode").value("CODE123"));
    }

    @Test
    void testGetRedemptionById() throws Exception {
        RedemptionDTO response = new RedemptionDTO(
                1, 101, 2001, LocalDateTime.now(),
                RedemptionStatus.PENDING, "Shipping soon", null,
                LocalDateTime.now().plusDays(7), "CODE123"
        );

        Mockito.when(redemptionService.getRedemptionById(1)).thenReturn(response);

        mockMvc.perform(get("/api/redemptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redemptionId").value(1))
                .andExpect(jsonPath("$.userId").value(101))
                .andExpect(jsonPath("$.catalogItemId").value(2001))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.redemptionCode").value("CODE123"));
    }

    @Test
    void testGetAllRedemptions() throws Exception {
        List<RedemptionDTO> list = Arrays.asList(
                new RedemptionDTO(1, 101, 2001, LocalDateTime.now(),
                        RedemptionStatus.PENDING, "Shipping soon", null,
                        LocalDateTime.now().plusDays(7), "CODE123"),
                new RedemptionDTO(2, 102, 2002, LocalDateTime.now(),
                        RedemptionStatus.SUCCESS, "Delivered", null,
                        LocalDateTime.now().plusDays(5), "CODE456")
        );

        Mockito.when(redemptionService.getAllRedemptions()).thenReturn(list);

        mockMvc.perform(get("/api/redemptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].redemptionId").value(1))
                .andExpect(jsonPath("$[1].status").value("SUCCESS"));
    }

    @Test
    void testUpdateRedemption() throws Exception {
        RedemptionDTO request = new RedemptionDTO(
                1, 101, 2001, LocalDateTime.now(),
                RedemptionStatus.SUCCESS, "Delivered", null,
                LocalDateTime.now().plusDays(7), "CODE789"
        );

        Mockito.when(redemptionService.updateRedemption(eq(1), any(RedemptionDTO.class)))
                .thenReturn(request);

        mockMvc.perform(put("/api/redemptions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redemptionId").value(1))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.redemptionCode").value("CODE789"));
    }

    @Test
    void testDeleteRedemption() throws Exception {
        Mockito.doNothing().when(redemptionService).deleteRedemption(1);

        mockMvc.perform(delete("/api/redemptions/1"))
                .andExpect(status().isOk());
    }
}
