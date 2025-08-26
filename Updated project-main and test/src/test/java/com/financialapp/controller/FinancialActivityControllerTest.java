package com.financialapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financialapp.dto.FinancialActivityDTO;
import com.financialapp.entity.ActivityType;
import com.financialapp.service.FinancialActivityService;
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
import static org.mockito.ArgumentMatchers.any;   // <-- Mockito matchers only
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FinancialActivityController.class)
class FinancialActivityControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private FinancialActivityService activityService;

    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/activities -> create activity")
    void create_returnsCreated() throws Exception {
        FinancialActivityDTO request = new FinancialActivityDTO(
                null, 1, ActivityType.REFERRAL, LocalDateTime.of(2025, 8, 20, 9, 0)
        );
        FinancialActivityDTO response = new FinancialActivityDTO(
                10, 1, ActivityType.REFERRAL, request.getActivityDate()
        );

        Mockito.when(activityService.create(any(FinancialActivityDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.activityId", is(10)))
               .andExpect(jsonPath("$.userId", is(1)))
               .andExpect(jsonPath("$.activityType", is("REFERRAL")));

        verify(activityService).create(any(FinancialActivityDTO.class));
    }

    @Test
    @DisplayName("GET /api/activities/{id} -> returns activity by id")
    void getById_returnsOne() throws Exception {
        FinancialActivityDTO dto = new FinancialActivityDTO(
                15, 2, ActivityType.SALARY_CREDIT, LocalDateTime.of(2025, 1, 1, 10, 0)
        );
        Mockito.when(activityService.getById(15)).thenReturn(dto);

        mockMvc.perform(get("/api/activities/{id}", 15))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.activityId", is(15)))
               .andExpect(jsonPath("$.userId", is(2)))
               .andExpect(jsonPath("$.activityType", is("SALARY_CREDIT")));

        verify(activityService).getById(15);
    }

    @Test
    @DisplayName("GET /api/activities -> returns list")
    void getAll_returnsList() throws Exception {
        FinancialActivityDTO a = new FinancialActivityDTO(1, 100, ActivityType.PROFILE_COMPLETION, null);
        FinancialActivityDTO b = new FinancialActivityDTO(2, 100, ActivityType.HOLIDAY_BONUS, null);

        Mockito.when(activityService.getAll()).thenReturn(List.of(a, b));

        mockMvc.perform(get("/api/activities"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].activityId", is(1)))
               .andExpect(jsonPath("$[0].activityType", is("PROFILE_COMPLETION")))
               .andExpect(jsonPath("$[1].activityId", is(2)))
               .andExpect(jsonPath("$[1].activityType", is("HOLIDAY_BONUS")));

        verify(activityService).getAll();
    }

    @Test
    @DisplayName("PUT /api/activities/{id} -> updates and returns activity")
    void update_returnsUpdated() throws Exception {
        FinancialActivityDTO request = new FinancialActivityDTO(
                null, 3, ActivityType.PROMOTIONAL_EVENT, LocalDateTime.of(2025, 3, 3, 14, 0)
        );
        FinancialActivityDTO response = new FinancialActivityDTO(
                30, 3, ActivityType.PROMOTIONAL_EVENT, request.getActivityDate()
        );

        Mockito.when(activityService.update(eq(30), any(FinancialActivityDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/activities/{id}", 30)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.activityId", is(30)))
               .andExpect(jsonPath("$.userId", is(3)))
               .andExpect(jsonPath("$.activityType", is("PROMOTIONAL_EVENT")));

        verify(activityService).update(eq(30), any(FinancialActivityDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/activities/{id} -> deletes activity")
    void delete_deletes() throws Exception {
        doNothing().when(activityService).delete(44);

        mockMvc.perform(delete("/api/activities/{id}", 44))
               .andExpect(status().isOk());

        verify(activityService).delete(44);
    }
}
