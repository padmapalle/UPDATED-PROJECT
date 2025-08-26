package com.financialapp.dto;

import com.financialapp.entity.RedemptionStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RedemptionDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        LocalDateTime redeemedAt = LocalDateTime.now();
        LocalDateTime expiryDate = redeemedAt.plusDays(7);

        RedemptionDTO dto = new RedemptionDTO(
                1, 101, 2001, redeemedAt,
                RedemptionStatus.PENDING, "Details here", "None",
                expiryDate, "CODE123"
        );

        assertEquals(1, dto.getRedemptionId());
        assertEquals(101, dto.getUserId());
        assertEquals(2001, dto.getCatalogItemId());
        assertEquals(redeemedAt, dto.getRedeemedAt());
        assertEquals(RedemptionStatus.PENDING, dto.getStatus());
        assertEquals("Details here", dto.getFulfillmentDetails());
        assertEquals("None", dto.getFailureReason());
        assertEquals(expiryDate, dto.getExpiryDate());
        assertEquals("CODE123", dto.getRedemptionCode());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        RedemptionDTO dto = new RedemptionDTO();

        dto.setRedemptionId(2);
        dto.setUserId(102);
        dto.setCatalogItemId(2002);
        dto.setRedeemedAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        dto.setStatus(RedemptionStatus.SUCCESS);
        dto.setFulfillmentDetails("Delivered");
        dto.setFailureReason(null);
        dto.setExpiryDate(LocalDateTime.of(2025, 1, 10, 10, 0));
        dto.setRedemptionCode("CODE456");

        assertEquals(2, dto.getRedemptionId());
        assertEquals(102, dto.getUserId());
        assertEquals(2002, dto.getCatalogItemId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0), dto.getRedeemedAt());
        assertEquals(RedemptionStatus.SUCCESS, dto.getStatus());
        assertEquals("Delivered", dto.getFulfillmentDetails());
        assertNull(dto.getFailureReason());
        assertEquals(LocalDateTime.of(2025, 1, 10, 10, 0), dto.getExpiryDate());
        assertEquals("CODE456", dto.getRedemptionCode());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        RedemptionDTO dto1 = new RedemptionDTO(1, 101, 2001, now,
                RedemptionStatus.PENDING, "Details", null, now.plusDays(7), "CODE123");

        RedemptionDTO dto2 = new RedemptionDTO(1, 101, 2001, now,
                RedemptionStatus.PENDING, "Details", null, now.plusDays(7), "CODE123");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RedemptionDTO dto = new RedemptionDTO();
        dto.setRedemptionId(3);
        dto.setUserId(103);

        String str = dto.toString();
        assertTrue(str.contains("redemptionId=3"));
        assertTrue(str.contains("userId=103"));
    }
}
