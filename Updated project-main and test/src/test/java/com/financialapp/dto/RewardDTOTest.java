package com.financialapp.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RewardDTOTest {

    @Test
    @DisplayName("Default constructor initializes nulls")
    void testDefaultConstructor() {
        RewardDTO dto = new RewardDTO();
        assertNull(dto.getRewardId());
        assertNull(dto.getUserId());
        assertNull(dto.getGoalId());
        assertNull(dto.getActivityId());
        assertNull(dto.getCatalogItemId());
        assertNull(dto.getPoints());
        assertNull(dto.getEarnedAt());
    }

    @Test
    @DisplayName("All-args constructor sets fields correctly")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.of(2025, 8, 20, 15, 0);
        RewardDTO dto = new RewardDTO(1L, 2, 3, 4, 5, 100, now);

        assertEquals(1L, dto.getRewardId());
        assertEquals(2, dto.getUserId());
        assertEquals(3, dto.getGoalId());
        assertEquals(4, dto.getActivityId());
        assertEquals(5, dto.getCatalogItemId());
        assertEquals(100, dto.getPoints());
        assertEquals(now, dto.getEarnedAt());
    }

    @Test
    @DisplayName("Setters and Getters work correctly")
    void testSettersAndGetters() {
        RewardDTO dto = new RewardDTO();
        LocalDateTime when = LocalDateTime.now();

        dto.setRewardId(10L);
        dto.setUserId(20);
        dto.setGoalId(30);
        dto.setActivityId(40);
        dto.setCatalogItemId(50);
        dto.setPoints(200);
        dto.setEarnedAt(when);

        assertEquals(10L, dto.getRewardId());
        assertEquals(20, dto.getUserId());
        assertEquals(30, dto.getGoalId());
        assertEquals(40, dto.getActivityId());
        assertEquals(50, dto.getCatalogItemId());
        assertEquals(200, dto.getPoints());
        assertEquals(when, dto.getEarnedAt());
    }

    @Test
    @DisplayName("equals and hashCode should match for identical objects")
    void testEqualsAndHashCode() {
        LocalDateTime when = LocalDateTime.now();
        RewardDTO a = new RewardDTO(1L, 2, 3, 4, 5, 100, when);
        RewardDTO b = new RewardDTO(1L, 2, 3, 4, 5, 100, when);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("toString should include key fields")
    void testToString() {
        LocalDateTime when = LocalDateTime.of(2025, 1, 1, 12, 0);
        RewardDTO dto = new RewardDTO(99L, 1, 2, 3, 4, 500, when);

        String s = dto.toString();
        assertTrue(s.contains("rewardId=99"));
        assertTrue(s.contains("userId=1"));
        assertTrue(s.contains("points=500"));
    }

    @Test
    @DisplayName("Jackson serialization should include all fields")
    void testJacksonSerialization() throws Exception {
        LocalDateTime when = LocalDateTime.of(2025, 1, 1, 12, 0);
        RewardDTO dto = new RewardDTO(1L, 2, 3, 4, 5, 250, when);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        String json = om.writeValueAsString(dto);

        assertTrue(json.contains("\"rewardId\":1"));
        assertTrue(json.contains("\"userId\":2"));
        assertTrue(json.contains("\"goalId\":3"));
        assertTrue(json.contains("\"activityId\":4"));
        assertTrue(json.contains("\"catalogItemId\":5"));
        assertTrue(json.contains("\"points\":250"));
        assertTrue(json.contains("2025-01-01T12:00"));
    }
}
