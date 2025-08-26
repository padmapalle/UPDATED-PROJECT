package com.financialapp.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RewardCatalogDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        RewardCatalogDTO dto = new RewardCatalogDTO(
                1,
                "Reward Name",
                "GiftCard",
                500,
                101,
                "Config Data",
                true,
                30
        );

        assertEquals(1, dto.getCatalogItemId());
        assertEquals("Reward Name", dto.getName());
        assertEquals("GiftCard", dto.getRewardType());
        assertEquals(500, dto.getPointsRequired());
        assertEquals(101, dto.getPartnerId());
        assertEquals("Config Data", dto.getConfiguration());
        assertTrue(dto.isActive());
        assertEquals(30, dto.getValidityDuration());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        RewardCatalogDTO dto = new RewardCatalogDTO();
        dto.setCatalogItemId(2);
        dto.setName("Test Reward");
        dto.setRewardType("Voucher");
        dto.setPointsRequired(1000);
        dto.setPartnerId(202);
        dto.setConfiguration("Test Config");
        dto.setActive(false);
        dto.setValidityDuration(60);

        assertEquals(2, dto.getCatalogItemId());
        assertEquals("Test Reward", dto.getName());
        assertEquals("Voucher", dto.getRewardType());
        assertEquals(1000, dto.getPointsRequired());
        assertEquals(202, dto.getPartnerId());
        assertEquals("Test Config", dto.getConfiguration());
        assertFalse(dto.isActive());
        assertEquals(60, dto.getValidityDuration());
    }

    @Test
    void testEqualsAndHashCode() {
        RewardCatalogDTO dto1 = new RewardCatalogDTO(1, "Reward", "GiftCard", 500, 101, "Config", true, 30);
        RewardCatalogDTO dto2 = new RewardCatalogDTO(1, "Reward", "GiftCard", 500, 101, "Config", true, 30);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToStringNotNull() {
        RewardCatalogDTO dto = new RewardCatalogDTO(1, "Reward", "GiftCard", 500, 101, "Config", true, 30);
        assertNotNull(dto.toString());
    }

    @Test
    void testCustomMethods() {
        RewardCatalogDTO dto = new RewardCatalogDTO();

        assertNull(dto.getId()); // Currently always returns null

        dto.setId(10); // does nothing, but should not throw exception
        dto.setDescription("Some description"); // does nothing, but should not throw exception
    }
}
