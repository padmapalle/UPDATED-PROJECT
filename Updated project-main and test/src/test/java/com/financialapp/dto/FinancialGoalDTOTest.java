package com.financialapp.dto;

import com.financialapp.entity.GoalType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinancialGoalDTOTest {

    @Test
    @DisplayName("Default constructor sets fields to null/false")
    void testDefaultConstructor() {
        FinancialGoalDTO dto = new FinancialGoalDTO();
        assertNull(dto.getGoalId());
        assertNull(dto.getUserId());
        assertNull(dto.getGoalType());      // field name is 'GoalType' -> getter is getGoalType()
        assertNull(dto.getCustomAttrs());
        assertFalse(dto.isAchieved());      // boolean getter is isAchieved()
    }

    @Test
    @DisplayName("All-args constructor sets all fields")
    void testAllArgsConstructor() {
        FinancialGoalDTO dto = new FinancialGoalDTO(
                10,
                1,
                GoalType.GROCERY,
                "{\"target\":5000}",
                true
        );

        assertEquals(10, dto.getGoalId());
        assertEquals(1, dto.getUserId());
        assertEquals(GoalType.GROCERY, dto.getGoalType());
        assertEquals("{\"target\":5000}", dto.getCustomAttrs());
        assertTrue(dto.isAchieved());
    }

    @Test
    @DisplayName("Getters and Setters work for all fields")
    void testGettersAndSetters() {
        FinancialGoalDTO dto = new FinancialGoalDTO();

        dto.setGoalId(22);
        dto.setUserId(5);
        dto.setGoalType(GoalType.INSURANCE);   // setter is setGoalType(...)
        dto.setCustomAttrs("{\"k\":\"v\"}");
        dto.setAchieved(true);                 // setter is setAchieved(...)

        assertEquals(22, dto.getGoalId());
        assertEquals(5, dto.getUserId());
        assertEquals(GoalType.INSURANCE, dto.getGoalType());
        assertEquals("{\"k\":\"v\"}", dto.getCustomAttrs());
        assertTrue(dto.isAchieved());

        // mutate again
        dto.setGoalType(GoalType.INVESTMENT);
        assertEquals(GoalType.INVESTMENT, dto.getGoalType());
    }

    @Test
    @DisplayName("equals and hashCode depend on all fields (Lombok @Data)")
    void testEqualsAndHashCode() {
        FinancialGoalDTO a = new FinancialGoalDTO(1, 100, GoalType.GROCERY, "{}", false);
        FinancialGoalDTO b = new FinancialGoalDTO(1, 100, GoalType.GROCERY, "{}", false);
        FinancialGoalDTO c = new FinancialGoalDTO(2, 100, GoalType.GROCERY, "{}", false);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    @DisplayName("toString contains key fields")
    void testToString() {
        FinancialGoalDTO dto = new FinancialGoalDTO(7, 3, GoalType.INVESTMENT, "{\"x\":1}", true);
        String s = dto.toString();
        assertTrue(s.contains("goalId=7"));
        assertTrue(s.contains("userId=3"));
        assertTrue(s.contains("INVESTMENT"));
        assertTrue(s.contains("isAchieved=true"));
    }

    @Test
    @DisplayName("@JsonProperty ensures JSON key is exactly 'isAchieved'")
    void testJsonPropertyIsAchieved() throws Exception {
        FinancialGoalDTO dto = new FinancialGoalDTO(9, 2, GoalType.GROCERY, "{}", true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dto);
        JsonNode node = mapper.readTree(json);

        // key must be literally "isAchieved"
        assertTrue(node.has("isAchieved"));
        assertTrue(node.get("isAchieved").asBoolean());

        // sanity on other fields
        assertEquals(9, node.get("goalId").asInt());
        assertEquals(2, node.get("userId").asInt());
        assertEquals("GROCERY", node.get("goalType").asText()); // Lombok getter name -> JSON key "goalType"
    }

    @Test
    @DisplayName("Null/edge values are handled")
    void testNullAndEdgeValues() {
        FinancialGoalDTO dto = new FinancialGoalDTO();

        dto.setGoalId(null);
        dto.setUserId(null);
        dto.setGoalType(null);
        dto.setCustomAttrs("");
        dto.setAchieved(false);

        assertNull(dto.getGoalId());
        assertNull(dto.getUserId());
        assertNull(dto.getGoalType());
        assertEquals("", dto.getCustomAttrs());
        assertFalse(dto.isAchieved());
    }
}
