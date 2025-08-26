package com.financialapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financialapp.dto.FinancialGoalDTO;
import com.financialapp.entity.GoalType;
import com.financialapp.service.FinancialGoalService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;   // ✅ only Mockito’s ArgumentMatchers
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FinancialGoalController.class)
class FinancialGoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinancialGoalService goalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateGoal() throws Exception {
        FinancialGoalDTO request = new FinancialGoalDTO(null, 1, GoalType.GROCERY, "Test Attrs", false);
        FinancialGoalDTO response = new FinancialGoalDTO(1, 1, GoalType.GROCERY, "Test Attrs", false);

        Mockito.when(goalService.createGoal(any(FinancialGoalDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.goalId", notNullValue()))
            .andExpect(jsonPath("$.goalType", is("GROCERY")))
            .andExpect(jsonPath("$.customAttrs", is("Test Attrs")));
    }

    @Test
    void testGetGoalById() throws Exception {
        FinancialGoalDTO response = new FinancialGoalDTO(1, 1, GoalType.GROCERY, "Test Attrs", false);

        Mockito.when(goalService.getGoalById(1)).thenReturn(response);

        mockMvc.perform(get("/api/goals/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.goalId", is(1)))
            .andExpect(jsonPath("$.goalType", is("GROCERY")))
            .andExpect(jsonPath("$.customAttrs", is("Test Attrs")));
    }

    @Test
    void testGetAllGoals() throws Exception {
        FinancialGoalDTO goal1 = new FinancialGoalDTO(1, 1, GoalType.GROCERY, "Attrs1", false);
        FinancialGoalDTO goal2 = new FinancialGoalDTO(2, 1, GoalType.INVESTMENT, "Attrs2", true);

        Mockito.when(goalService.getAllGoals()).thenReturn(Arrays.asList(goal1, goal2));

        mockMvc.perform(get("/api/goals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].goalType", is("GROCERY")))
            .andExpect(jsonPath("$[1].goalType", is("INVESTMENT")));
    }

    @Test
    void testUpdateGoal() throws Exception {
        FinancialGoalDTO request = new FinancialGoalDTO(null, 1, GoalType.INSURANCE, "Updated Attrs", true);
        FinancialGoalDTO response = new FinancialGoalDTO(10, 1, GoalType.INSURANCE, "Updated Attrs", true);

        Mockito.when(goalService.updateGoal(eq(10), any(FinancialGoalDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/goals/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.goalId", is(10)))
            .andExpect(jsonPath("$.goalType", is("INSURANCE")))
            .andExpect(jsonPath("$.customAttrs", is("Updated Attrs")))
            .andExpect(jsonPath("$.isAchieved", is(true)));
    }

    @Test
    void testMarkGoalAsAchieved() throws Exception {
        FinancialGoalDTO response = new FinancialGoalDTO(5, 1, GoalType.GROCERY, "Attrs", true);

        Mockito.when(goalService.markGoalAsAchieved(5)).thenReturn(response);

        mockMvc.perform(put("/api/goals/5/achieve"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.goalId", is(5)))
            .andExpect(jsonPath("$.isAchieved", is(true)));
    }

    @Test
    void testDeleteGoal() throws Exception {
        doNothing().when(goalService).deleteGoal(99);

        mockMvc.perform(delete("/api/goals/99"))
            .andExpect(status().isOk());

        verify(goalService).deleteGoal(99);
    }
}
