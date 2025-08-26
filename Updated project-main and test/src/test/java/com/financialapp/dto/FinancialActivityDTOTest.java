package com.financialapp.dto;

import com.financialapp.entity.ActivityType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FinancialActivityDTOTest {

    @Test
    @DisplayName("Default constructor initializes nulls")
    void testDefaultConstructor() {
        FinancialActivityDTO dto = new FinancialActivityDTO();

        assertNull(dto.getActivityId());
        assertNull(dto.getUserId());
        assertNull(dto.getActivityType());
        assertNull(dto.getActivityDate());
    }

    @Test
    @DisplayName("All-args constructor sets fields")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        FinancialActivityDTO dto = new FinancialActivityDTO(
                1, 101, ActivityType.REFERRAL, now
        );

        assertEquals(1, dto.getActivityId());
        assertEquals(101, dto.getUserId());
        assertEquals(ActivityType.REFERRAL, dto.getActivityType());
        assertEquals(now, dto.getActivityDate());
    }

    @Test
    @DisplayName("Getters and Setters work correctly")
    void testGettersSetters() {
        LocalDateTime date = LocalDateTime.of(2025, 8, 20, 12, 30);
        FinancialActivityDTO dto = new FinancialActivityDTO();

        dto.setActivityId(2);
        dto.setUserId(202);
        dto.setActivityType(ActivityType.SALARY_CREDIT);
        dto.setActivityDate(date);

        assertEquals(2, dto.getActivityId());
        assertEquals(202, dto.getUserId());
        assertEquals(ActivityType.SALARY_CREDIT, dto.getActivityType());
        assertEquals(date, dto.getActivityDate());
    }

    @Test
    @DisplayName("equals and hashCode from Lombok")
    void testEqualsAndHashCode() {
        LocalDateTime date = LocalDateTime.now();
        FinancialActivityDTO a = new FinancialActivityDTO(3, 303, ActivityType.HOLIDAY_BONUS, date);
        FinancialActivityDTO b = new FinancialActivityDTO(3, 303, ActivityType.HOLIDAY_BONUS, date);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("toString contains field values")
    void testToString() {
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 10, 0);
        FinancialActivityDTO dto = new FinancialActivityDTO(4, 404, ActivityType.ACCOUNT_ANNIVERSARY, date);

        String str = dto.toString();
        assertTrue(str.contains("activityId=4"));
        assertTrue(str.contains("userId=404"));
        assertTrue(str.contains("activityType=ACCOUNT_ANNIVERSARY"));
    }

    @Test
    @DisplayName("Jackson serialization works with LocalDateTime")
    void testJsonSerialization() throws Exception {
        LocalDateTime date = LocalDateTime.of(2025, 8, 20, 15, 45);
        FinancialActivityDTO dto = new FinancialActivityDTO(5, 505, ActivityType.PROMOTIONAL_EVENT, date);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // for LocalDateTime

        String json = mapper.writeValueAsString(dto);

        assertTrue(json.contains("\"activityId\":5"));
        assertTrue(json.contains("\"userId\":505"));
        assertTrue(json.contains("\"activityType\":\"PROMOTIONAL_EVENT\""));
        assertTrue(json.contains("2025-08-20T15:45"));
    }
}
